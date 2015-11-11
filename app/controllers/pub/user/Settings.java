package controllers.pub.user;

import models.Password;
import models.UserData;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utilities.AuthManager;
import utilities.Config;
import views.html.generic_failure;
import views.html.generic_success;
import views.html.settings;

/**
 * Created by MegaEduX on 27/10/15.
 */

public class Settings extends Controller {

    public Result handleSettings() {
        return ok(settings.render(Config.ServerName));
    }

    public Result handlePasswordChange() {
        DynamicForm form = Form.form().bindFromRequest();

        String oldPassword = form.get("old-pw");
        String newPassword = form.get("new-pw");
        String newPasswordConfirm = form.get("new-pw-confirm");

        if (!newPassword.equals(newPasswordConfirm))
            return forbidden(generic_failure.render(Config.ServerName, "The new password and its confirmation don't match!"));

        String username = AuthManager.currentUsername(request().cookies());

        if (username == null)
            return forbidden(generic_failure.render(Config.ServerName, "Username not found!"));

        UserData user = UserData.getUserDataFromUsername(username);

        if (user == null)
            return forbidden(generic_failure.render(Config.ServerName, "User object not found!"));

        try {
            Password oldP = new Password(user.passwordDigest, user.passwordSalt);

            if (!oldP.validate(oldPassword)) {
                return forbidden(generic_failure.render(Config.ServerName, "Old password mismatch!"));
            }

            Password newP = new Password(newPassword);

            user.passwordDigest = newP.digest;
            user.passwordSalt = newP.salt;

            user.save();

            return ok(generic_success.render(Config.ServerName, "Your password was successfully changed!"));
        } catch (Exception e) {
            return internalServerError(generic_failure.render(Config.ServerName, "Internal Server Error! " + e.getMessage()));
        }
    }
}
