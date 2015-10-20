package controllers;

import models.*;
import play.mvc.*;
import play.data.*;

/**
 * Created by MegaEduX on 19/10/15.
 */

public class Signup extends Controller {

    public Result signup() {
        DynamicForm form = Form.form().bindFromRequest();

        String user = form.get("username");
        String pass = form.get("password");

        String email = form.get("email");

        UserInfo u = new UserInfo(user, pass, email);

        //  u.password.save();

        //  Insert in DB?

        u.save();

        return ok("Signup successful!" );
    }

}
