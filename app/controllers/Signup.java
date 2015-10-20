package controllers;

import com.avaje.ebean.annotation.Transactional;
import models.*;
import play.mvc.*;
import play.data.*;

/**
 * Created by MegaEduX on 19/10/15.
 */

public class Signup extends Controller {

    @Transactional
    public Result signup() {
        DynamicForm form = Form.form().bindFromRequest();

        String user = form.get("username");
        String pass = form.get("password");

        String email = form.get("email");

        UserData u = new UserData(user, pass, email);

        u.save();

        return ok("Signup successful!" );
    }

}
