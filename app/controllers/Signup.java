package controllers;

import play.mvc.*;
import play.data.*;

/**
 * Created by MegaEduX on 19/10/15.
 */

public class Signup extends Controller {

    public static Result signup() {
        DynamicForm form = Form.form().bindFromRequest();

        String user = form.get("username");
        String pass = form.get("password");

        String email = form.get("email");

        form.data();

        return ok("Received POST data...");
    }

}
