package controllers;

import models.User;
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

        User u = new User(user, pass, email);

        //  Insert in DB?

        return ok("Signup successful!");
    }

}
