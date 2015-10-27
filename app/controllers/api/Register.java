package controllers.api;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.annotation.Transactional;
import models.*;

import play.mvc.*;
import play.data.*;

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

        return ok("{\"result\": \"success\"}");
    }

}
