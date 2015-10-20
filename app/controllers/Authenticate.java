package controllers;

import com.avaje.ebean.Model;
import models.UserData;
import play.libs.Json;
import play.mvc.*;

/**
 * Created by MegaEduX on 19/10/15.
 */
public class Authenticate extends Controller {

    public Result login() {
        Model.Finder<Long, UserData> find = new Model.Finder<Long, UserData>(UserData.class);

        return ok(Json.stringify(Json.toJson(find.findList())));
    }

}
