package controllers.pub.user;

import models.Endpoint;
import models.Password;
import models.UserAttribute;
import models.UserData;
import play.data.DynamicForm;
import play.data.Form;
import play.data.validation.Constraints;
import play.mvc.Controller;
import play.mvc.Result;
import utilities.AuthManager;
import utilities.Config;
import utilities.KeyGenerator;
import utilities.Mailer;
import views.html.generic_failure;
import views.html.generic_success;
import views.html.settings;

/**
 * Created by MegaEduX on 27/10/15.
 */

public class Settings extends Controller {

    public Result handleSettings() {
        if (AuthManager.isLoggedIn(request().cookies()))
            return ok(settings.render(Config.ServerName));

        return forbidden(forbidden.render(Config.ServerName, false));
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

    public Result handleEmailChange() {
        DynamicForm form = Form.form().bindFromRequest();

        String newEmail = form.get("new-email");

        if (newEmail == null || newEmail.equals(""))
            return badRequest(generic_failure.render(Config.ServerName, "E-mail can't be blank!"));

        Constraints.EmailValidator val = new Constraints.EmailValidator();

        if (!val.isValid(newEmail))
            return badRequest(generic_failure.render(Config.ServerName, "The inserted e-mail is not valid!"));

        String username = AuthManager.currentUsername(request().cookies());

        if (username == null)
            return forbidden(generic_failure.render(Config.ServerName, "Username not found!"));

        UserData user = UserData.getUserDataFromUsername(username);

        KeyGenerator kg = new KeyGenerator();

        String key = kg.nextKey();

        UserAttribute newEmailAttribute = new UserAttribute(user, "emailChange-newEmail", newEmail);
        UserAttribute emailChangeAttribute = new UserAttribute(user, "emailChange-key", key);

        newEmailAttribute.save();
        emailChangeAttribute.save();

        Mailer m = new Mailer(Config.getServerURL(request()));

        if (m.sendEmailChangeEmail(username, user.emailAddress, newEmail, key)) {
            return ok(generic_success.render(Config.ServerName, "Please check your (old) e-mail address for instructions on how to complete this process."));
        } else {
            return internalServerError(generic_failure.render(Config.ServerName, "An error has occurred while sending an e-mail address."));
        }
    }

    public Result handleEmailChangeConfirmation() {
        DynamicForm form = Form.form().bindFromRequest();

        String username = form.get("username");
        String key = form.get("key");

        UserData user = UserData.getUserDataFromUsername(username);

        if (user == null)
            return internalServerError(generic_failure.render(Config.ServerName, "Your user wasn't found in our servers."));

        UserAttribute ecne = null;
        UserAttribute eck = null;

        for (UserAttribute a : user.attributes) {
            if (a.key.equals("emailChange-newEmail"))
                ecne = a;
            else if (a.key.equals("emailChange-key"))
                eck = a;
        }

        if (!eck.value.equals(key))
            return internalServerError(generic_failure.render(Config.ServerName, "Key mismatch!"));

        user.emailAddress = ecne.value;

        eck.delete();
        ecne.delete();

        user.save();

        return ok(generic_success.render(Config.ServerName, "Successfully changed your e-mail address!"));
    }
}
