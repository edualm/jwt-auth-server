package controllers.pub;

import com.avaje.ebean.Ebean;
import models.Password;
import models.UserData;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utilities.Config;
import utilities.JWTFactory;
import views.html.login_failure;
import views.html.login_success;

import java.util.List;

/**
 * Created by MegaEduX on 27/10/15.
 */

public class LoginPerform extends Controller {

    public Result handlePerformLogin() {
        DynamicForm form = Form.form().bindFromRequest();

        String user = form.get("username");
        String pass = form.get("password");

        if (user == null || user == "")
            return ok(login_failure.render(Config.ServerName, "Missing field: \"username\"."));

        if (pass == null || pass == "")
            return ok(login_failure.render(Config.ServerName, "Missing field: \"password\"."));

        List<UserData> users = Ebean.find(UserData.class).where().eq("username", user).findList();

        if (users.size() == 0)
            return ok(login_failure.render(Config.ServerName, "User not found."));

        UserData u = users.get(0);

        if (!u.enabled)
            return ok(login_failure.render(Config.ServerName, "Resource disabled."));

        try {
            Password pi = new Password(u.passwordDigest, u.passwordSalt);

            if (pi.validate(pass)) {
                return ok(login_success.render(Config.ServerName, JWTFactory.createAuthenticationJWT(u, request().remoteAddress(), false)));

                //  return ok("{\"success\": true, \"jwt\": \"" + JWTFactory.createAuthenticationJWT(u, request().remoteAddress(), false) + "\"}");
            } else {
                return ok(login_failure.render(Config.ServerName, "Incorrect username or password!"));
            }
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
    }

}
