package controllers;

import com.avaje.ebean.Ebean;
import models.Category;
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

    public Result populateWithTestData() {
        try {
            UserData u = new UserData("foo", "bar", "foo@bar.com");
            Category cat = new Category("Admin Category", Category.Type.Administrator);

            u.addAttribute("fus, ro...", "DAH!");
            u.categories.add(cat);

            cat.addUser(u);

            u.save();
            cat.save();

            return ok("{\"success\": \"true\"}");
        } catch (Exception e) {
            return ok("{\"success\": \"false\", \"debug\": \"" + e + "\"}");
        }

    }
}
