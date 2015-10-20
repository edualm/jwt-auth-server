package controllers;

import com.avaje.ebean.Model;
import models.UserData;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.*;

import java.util.List;

/**
 * Created by MegaEduX on 19/10/15.
 */

public class Authenticate extends Controller {

    public Result login() {
        DynamicForm form = Form.form().bindFromRequest();

        String user = form.get("username");

        Model.Finder<Long, UserData> find = new Model.Finder<Long, UserData>(UserData.class);

        List<UserData> users = find.where().eq("username", user).findList();

        if (users.size() == 0) {
            return notFound("User not found!");
        }

        return ok(Json.stringify(Json.toJson(users.get(0))));
    }

}
