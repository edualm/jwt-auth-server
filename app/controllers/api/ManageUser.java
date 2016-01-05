package controllers.api;

import com.avaje.ebean.Ebean;
import models.UserData;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import play.data.DynamicForm;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.*;
import utilities.Config;

import java.util.List;

/**
 * Created by MegaEduX on 22/10/15.
 */

public class ManageUser extends Controller {
    public Result changePassword() {
        DynamicForm form = Form.form().bindFromRequest();

        if (request().hasHeader("Authorization")) {
            String auth = request().getHeader("Authorization");

            String jwt = auth.replaceAll("Bearer ", "");

            String pass = form.get("password");

            if (pass == null || pass.equals(""))
                return notFound("{\"error\": \"" + Messages.get("manage.missField","password") + "\"}");

            try {
                UserData u = getUserFromSession(jwt);

                u.changePassword(pass);

                u.save();
            } catch (Exception e) {
                return notFound("{\"error\": \"" + e.getMessage() + "\"}");
            }
        } else {
            return notFound("{\"error\": \" " + Messages.get("manage.missHead", "authorization") + "\"}");
        }


        return ok();
    }

    public UserData getUserFromSession(String jwt) throws InvalidJwtException {
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setAllowedClockSkewInSeconds(30)
                .setRequireSubject()
                .setExpectedIssuer(Config.ServerName)
                .setExpectedAudience(Config.ServerName)
                .setVerificationKey(Config.getJsonWebKey().getPublicKey())
                .build();

        JwtClaims claims = jwtConsumer.processToClaims(jwt);

        Long id = (Long) claims.getClaimValue("id");

        List<UserData> users = Ebean.find(UserData.class).where().eq("id", id).findList();

        return users.get(0);
    }
}
