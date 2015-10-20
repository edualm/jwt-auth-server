package controllers;

import com.avaje.ebean.Model;
import models.User;
import play.libs.Json;
import play.mvc.*;

/**
 * Created by MegaEduX on 19/10/15.
 */
public class Authenticate extends Controller {

    public Result login() {
        Model.Finder<Long, User> find = new Model.Finder<Long, User>(User.class);

        return ok(Json.stringify(Json.toJson(find.findList())));
    }

}
