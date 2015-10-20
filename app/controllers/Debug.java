package controllers;

import com.avaje.ebean.Ebean;
import models.UserData;
import org.jose4j.lang.JoseException;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import utilities.JWTFactory;

import java.util.List;

/**
 * Created by MegaEduX on 20/10/15.
 */
public class Debug extends Controller {
    public Result showAllUsers() {
        List<UserData> users = Ebean.find(UserData.class).findList();

        return ok(Json.stringify(Json.toJson(users)));
    }

    public Result createJWT() {
        try {
            return ok(JWTFactory.createJWT());
        } catch (JoseException e) {
            return internalServerError(e.getMessage());
        }
    }
}
