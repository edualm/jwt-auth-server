package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.annotation.Transactional;
import models.*;
import play.mvc.*;
import play.data.*;

import javax.inject.Inject;

/**
 * Created by MegaEduX on 19/10/15.
 */

public class Signup extends Controller {

    @Transactional
    public Result signup() {
        /*DynamicForm form = Form.form().bindFromRequest();

        String user = form.get("username");
        String pass = form.get("password");

        String email = form.get("email");

        User u = new User(user, pass, email);

        //  u.password.save();

        //  Insert in DB?

        u.save();*/

        Demo d = new Demo();

        d.foo = "123";

        //  Ebean.save(d);

        d.save();

        return ok("Signup successful!" );
    }

}
