package controllers.api;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.annotation.Transactional;
import com.sendgrid.SendGrid;
import models.*;

import play.mvc.*;
import play.data.*;
import utilities.Config;
import utilities.KeyGenerator;

/**
 * Created by MegaEduX on 19/10/15.
 */

public class Register extends Controller {

    @Transactional
    public Result signup() {
        DynamicForm form = Form.form().bindFromRequest();

        String user = form.get("username");
        String pass = form.get("password");
        String email = form.get("email");

        if (user == null || user == "")
            return notFound("{\"error\": \"Missing field: \"username\".\"}");

        if (pass == null || pass == "")
            return notFound("{\"error\": \"Missing field: \"password\".\"}");

        if (email == null || email == "")
            return notFound("{\"error\": \"Missing field: \"email\".\"}");

        if (Ebean.find(UserData.class).where().eq("username", user).findList().size() != 0)
            return notFound("{\"error\": \"A user with this username or e-mail already exists!\"}");

        UserData u = new UserData(user, pass, email);

        u.save();

        KeyGenerator kg = new KeyGenerator();

        UserAttribute val = new UserAttribute(u, "validation-key", kg.nextKey());

        val.save();

        return ok("{\"result\": \"success\"}");
    }

    public boolean sendEmail(String email) {
        SendGrid sg = new SendGrid(Config.kSendGridUsername, Config.kSendGridPassword);

        SendGrid.Email e = new SendGrid.Email();

        e.addTo(email);
        e.setFrom(Config.kEmailFrom);
        e.setSubject("Registration");
        e.setText("<~ validation text ~>");

        try {
            SendGrid.Response r = sg.send(e);

            return true;
        } catch (Exception ex) {
            System.err.println(ex);

            return false;
        }
    }

}
