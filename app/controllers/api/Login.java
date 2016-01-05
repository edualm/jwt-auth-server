package controllers.api;

import com.avaje.ebean.Ebean;

import models.Password;
import models.UserAttribute;
import models.UserData;

import play.data.DynamicForm;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.*;

import utilities.Config;
import utilities.JWTFactory;

import java.util.Base64;
import java.util.List;

/**
 * Created by MegaEduX on 19/10/15.
 */

public class Login extends Controller {

    public Result login() {
        DynamicForm form = Form.form().bindFromRequest();

        String user = form.get("username").toLowerCase();
        String pass = form.get("password");

        

        if (user == null || user == "")
            return notFound("{\"error\": \"" + Messages.get("login.missingField", "username") + "\"}");

        if (pass == null || pass == "")
            return notFound("{\"error\": \"" + Messages.get("login.missingField", "password") + "\"}");

        List<UserData> users = Ebean.find(UserData.class).where().eq("username", user).findList();

        if (users.size() == 0)
            return notFound("{\"error\": \"" + Messages.get("login.userNotFound") + "\"}");
            //return notFound("{\"error\": \"Utilizador não encontrado.\"}");

        UserData u = users.get(0);

        for (UserAttribute a : u.attributes)
            if (a.key.equals("validation-key"))
                return notFound("{\"error\": \"" + Messages.get("login.accountNotValidated") + "\"}");

        if (!u.enabled)
            return notFound("{\"error\": \"" + Messages.get("login.resourceDisabled") + "\"}");

        try {
            Password pi = new Password(u.passwordDigest, u.passwordSalt);

            if (pi.validate(pass))
                return ok("{\"success\": true, \"jwt\": \"" + JWTFactory.createAuthenticationJWT(u, request().remoteAddress(), false) + "\"}");
            else
                return notFound("{\"error\": \"" + Messages.get("login.wrongPassword") + "\"}");
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
    }

    public Result getJWTPublicKey() {
        String pubKey = Base64.getEncoder().encodeToString(Config.getJsonWebKey().getPublicKey().getEncoded());

        return ok(pubKey);
    }
}
