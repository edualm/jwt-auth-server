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

        //  User u = new User(user, pass, email);

        /*public User(String username, String password, String emailAddress) {
        this.id = Integer.toUnsignedLong(10);

        this.username = username;

        try {
            this.password = new Password(password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        this.emailAddress = emailAddress;
    }*/

        User u = new User();

        u.setUser(user);

        try {
            u.setPassword(new Password(pass));
        } catch (Exception e) {

        }

        u.setEmailAddress(email);

        //  Insert in DB?

        u.save();

        return ok("Signup successful!" );
    }

}
