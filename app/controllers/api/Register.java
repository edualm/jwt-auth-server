package controllers.api;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.annotation.Transactional;
import com.sendgrid.SendGrid;
import models.*;

import play.i18n.Messages;
import play.mvc.*;
import play.data.*;
import utilities.Config;
import utilities.KeyGenerator;
import utilities.Mailer;

/**
 * Created by MegaEduX on 19/10/15.
 */

public class Register extends Controller {

    @Transactional
    public Result signup() {
        DynamicForm form = Form.form().bindFromRequest();

        String user = form.get("username").toLowerCase();
        String pass = form.get("password");
        String email = form.get("email");
        String firstName = form.get("firstName");
        String lastName = form.get("lastName");
        String toc = form.get("toc");

        if (toc == null || !toc.equals("true"))
            return notFound("{\"error\": \"" + Messages.get("register.tocNotAccepted") + "\"}");

        if (user == null || user == "")
            return notFound("{\"error\": \"" + Messages.get("register.missField", "username") + "\"}");

        if (pass == null || pass == "")
            return notFound("{\"error\": \"" + Messages.get("register.missField", "password") + "\"}");

        if (email == null || email == "")
            return notFound("{\"error\": \"" + Messages.get("register.missField", "email") + "\"}");

        if (firstName == null || firstName == "")
            return notFound("{\"error\": \"" + Messages.get("register.missField", "firstName") + "\"}");

        if (lastName == null || lastName == "")
            return notFound("{\"error\": \"" + Messages.get("register.missField", "lastName") + "\"}");

        if (Ebean.find(UserData.class).where().eq("username", user).findList().size() != 0)
            return notFound("{\"error\": \"" + Messages.get("register.alreadyExists") + "\"}");

        UserData u = new UserData(user, pass, email, firstName, lastName);

        u.save();

        KeyGenerator kg = new KeyGenerator();

        UserAttribute val = new UserAttribute(u, "validation-key", kg.nextKey());

        val.save();

        Mailer m = new Mailer(Config.getServerURL(request()));

        if (m.sendValidationEmail(user, email, val.value))
            return ok("{\"result\": \"" + Messages.get("register.emailSuccess") + "\"}");
        else
            return internalServerError("{\"result\": \"" + Messages.get("register.emailFailure") + "\"}");

    }



}
