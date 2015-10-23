package controllers;

import com.avaje.ebean.Ebean;

import models.Password;
import models.UserData;

import play.data.DynamicForm;
import play.data.Form;
import play.mvc.*;

import utilities.Config;
import utilities.JWTFactory;

import java.util.Base64;
import java.util.List;

/**
 * Created by MegaEduX on 19/10/15.
 */

public class Authenticate extends Controller {

    public Result login() {
        DynamicForm form = Form.form().bindFromRequest();

        String user = form.get("username");
        String pass = form.get("password");

        if (user == null || user == "")
            return notFound("{\"error\": \"Missing field: \"username\".\"}");

        if (pass == null || pass == "")
            return notFound("{\"error\": \"Missing field: \"password\".\"}");

        List<UserData> users = Ebean.find(UserData.class).where().eq("username", user).findList();

        if (users.size() == 0)
            return notFound(("{\"error\": \"User not found.\"}");

        UserData u = users.get(0);

        if (!u.enabled)
            return notFound("{\"error\": \"Resource disabled.\"}");

        try {
            Password pi = new Password(u.passwordDigest, u.passwordSalt);

            if (pi.validate(pass)) {
                return ok("{\"success\": true, \"jwt\": \"" + JWTFactory.createAuthenticationJWT(u, request().remoteAddress(), false) + "\"}");
            } else {
                return notFound("{\"error\": \"Wrong password!\"}");
            }
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
    }

    public Result getJWTPublicKey() {
        String pubKey = Base64.getEncoder().encodeToString(Config.getJsonWebKey().getPublicKey().getEncoded());

        return ok(pubKey);
    }
}
