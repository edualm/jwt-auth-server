package controllers.pub.unauthenticated;

import com.avaje.ebean.Ebean;
import models.UserAttribute;
import models.UserData;
import play.data.DynamicForm;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import utilities.AuthManager;
import utilities.Config;
import utilities.KeyGenerator;
import utilities.Mailer;
import views.html.forbidden;
import views.html.register;
import views.html.register_failure;
import views.html.register_success;

/**
 * Created by MegaEduX on 23/10/15.
 */

public class Register extends Controller {
    public Result registerPage() {
        if (AuthManager.isLoggedIn(request().cookies()))
            return forbidden(forbidden.render(Config.ServerName, true));

        return ok(register.render(Config.ServerName));
    }

    public Result handleRegisterPerform() {
        if (AuthManager.isLoggedIn(request().cookies()))
            return forbidden(forbidden.render(Config.ServerName, true));

        DynamicForm form = Form.form().bindFromRequest();

        String user = form.get("username").toLowerCase();
        String pass = form.get("password");
        String email = form.get("email");
        String firstName = form.get("firstName");
        String lastName = form.get("lastName");
        String toc = form.get("toc");

        if (toc == null || !toc.equals("true"))
            return ok(register_failure.render(Config.ServerName, Messages.get("register.tocNotAccepted")));

        if (user == null || user == "")
            return ok(register_failure.render(Config.ServerName, Messages.get("login.missingField", Messages.get("generic.username"))));

        if (pass == null || pass == "")
            return ok(register_failure.render(Config.ServerName, Messages.get("login.missingField", Messages.get("generic.password"))));

        if (email == null || email == "")
            return ok(register_failure.render(Config.ServerName, Messages.get("login.missingField", Messages.get("generic.emailAddress"))));

        if (firstName == null || firstName == "")
            return ok(register_failure.render(Config.ServerName, Messages.get("login.missingField", Messages.get("generic.firstName"))));

        if (lastName == null || lastName == "")
            return ok(register_failure.render(Config.ServerName, Messages.get("login.missingField", Messages.get("generic.lastName"))));

        if (Ebean.find(UserData.class).where().eq("username", user).findList().size() != 0)
            return ok(register_failure.render(Config.ServerName, Messages.get("register.alreadyExists")));

        UserData u = new UserData(user, pass, email, firstName, lastName);

        u.save();

        KeyGenerator kg = new KeyGenerator();

        UserAttribute val = new UserAttribute(u, "validation-key", kg.nextKey());

        val.save();

        Mailer m = new Mailer(Config.getServerURL(request()));

        if (m.sendValidationEmail(user, email, val.value))
            return ok(register_success.render(Config.ServerName));
        else
            return internalServerError(register_failure.render(Config.ServerName, Messages.get("register.emailFailure")));
    }
}
