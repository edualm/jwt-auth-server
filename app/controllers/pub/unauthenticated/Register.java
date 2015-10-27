package controllers.pub.unauthenticated;

import com.avaje.ebean.Ebean;
import models.UserData;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utilities.Config;
import views.html.register;
import views.html.register_failure;
import views.html.register_success;

/**
 * Created by MegaEduX on 23/10/15.
 */

public class Register extends Controller {
    public Result registerPage() {
        return ok(register.render(Config.ServerName));
    }

    public Result handleRegisterPerform() {
        DynamicForm form = Form.form().bindFromRequest();

        String user = form.get("username");
        String pass = form.get("password");
        String email = form.get("email");
        String name = form.get("name");

        if (user == null || user == "")
            return ok(register_failure.render(Config.ServerName, "Missing field: \"username\"."));

        if (pass == null || pass == "")
            return ok(register_failure.render(Config.ServerName, "Missing field: \"password\"."));

        if (email == null || email == "")
            return ok(register_failure.render(Config.ServerName, "Missing field: \"e-mail address\"."));

        if (name == null || name == "")
            return ok(register_failure.render(Config.ServerName, "Missing file: \"name\"."));

        if (Ebean.find(UserData.class).where().eq("username", user).findList().size() != 0)
            return ok(register_failure.render(Config.ServerName, "A user with this username or e-mail already exists!"));

        UserData u = new UserData(user, pass, email);

        //  missing fn

        u.save();

        return ok(register_success.render(Config.ServerName));
    }
}
