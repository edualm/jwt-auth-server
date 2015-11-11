package controllers.pub.unauthenticated;

import com.avaje.ebean.Ebean;
import models.Password;
import models.UserAttribute;
import models.UserData;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utilities.AuthManager;
import utilities.Config;
import utilities.JWTFactory;
import views.html.forbidden;
import views.html.login;
import views.html.login_failure;
import views.html.login_success;

import java.util.List;

/**
 * Created by MegaEduX on 23/10/15.
 */

public class Login extends Controller {

    public Result loginPage() {
        if (AuthManager.isLoggedIn(request().cookies()))
            return forbidden(forbidden.render(Config.ServerName, true));

        return ok(login.render(Config.ServerName));
    }

    public Result handlePerformLogin() {
        if (AuthManager.isLoggedIn(request().cookies()))
            return forbidden(forbidden.render(Config.ServerName, true));

        DynamicForm form = Form.form().bindFromRequest();

        String user = form.get("username").toLowerCase();
        String pass = form.get("password");
        String callback = form.get("callback");

        //  Need to implement callback.

        if (user == null || user == "")
            return ok(login_failure.render(Config.ServerName, "Missing field: \"username\"."));

        if (pass == null || pass == "")
            return ok(login_failure.render(Config.ServerName, "Missing field: \"password\"."));

        List<UserData> users = Ebean.find(UserData.class).where().eq("username", user).findList();

        if (users.size() == 0)
            return ok(login_failure.render(Config.ServerName, "User not found: " + user));

        UserData u = users.get(0);

        for (UserAttribute a : u.attributes)
            if (a.key.equals("validation-key"))
                return forbidden(login_failure.render(Config.ServerName, "Account not validated!"));

        if (!u.enabled)
            return ok(login_failure.render(Config.ServerName, "Resource disabled."));

        try {
            Password pi = new Password(u.passwordDigest, u.passwordSalt);

            if (pi.validate(pass)) {
                if (callback != null && callback != "")
                    return ok(login_success.render(Config.ServerName, callback + "?jwt=" + JWTFactory.createAuthenticationJWT(u, request().remoteAddress(), false)));
                else {
                    response().setCookie(
                            "jwt",
                            JWTFactory.createAuthenticationJWT(u, request().remoteAddress(), Config.ServerName, "auth", true),
                            3600,
                            "/",
                            Config.ServerURI,
                            true,
                            true
                    );

                    return ok(login_success.render(Config.ServerName, ""));
                }
            } else {
                return ok(login_failure.render(Config.ServerName, "Incorrect username or password!"));
            }
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
    }

}
