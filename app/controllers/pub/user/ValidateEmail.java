package controllers.pub.user;

import models.UserAttribute;
import models.UserData;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utilities.AuthManager;
import utilities.Config;
import views.html.forbidden;
import views.html.validate_failure;
import views.html.validate_success;

/**
 * Created by MegaEduX on 27/10/15.
 */

public class ValidateEmail extends Controller {
    public Result handleValidation() {
        if (AuthManager.isLoggedIn(request().cookies()))
            return forbidden(forbidden.render(Config.ServerName, true));

        DynamicForm form = Form.form().bindFromRequest();

        String user = form.get("username");
        String key = form.get("validationKey");

        UserData u = UserData.getUserDataFromUsername(user);

        if (u == null)
            return ok(validate_failure.render(Config.ServerName, "User not found!"));

        for (UserAttribute ua : u.attributes)
            if (ua.key.equals("validation-key")) {
                if (ua.value.equals(key)) {
                    //try {
                        ua.delete();
                        //u.removeAttribute("validation-key");
                        //u.save();
                    /*} catch (UserData.AttributeNotFoundException e) {
                        //  This can't happen.
                    }*/

                    return ok(validate_success.render(Config.ServerName));
                } else
                    return ok(validate_failure.render(Config.ServerName, "Wrong validation key."));
            }

        return ok(validate_failure.render(Config.ServerName, "This account appears to have already been validated."));
    }
}
