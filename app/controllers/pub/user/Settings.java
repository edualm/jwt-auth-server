package controllers.pub.user;

import models.Endpoint;
import models.Password;
import models.UserAttribute;
import models.UserData;
import play.data.DynamicForm;
import play.data.Form;
import play.data.validation.Constraints;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import utilities.AuthManager;
import utilities.Config;
import utilities.KeyGenerator;
import utilities.Mailer;
import views.html.forbidden;
import views.html.generic_failure;
import views.html.generic_success;
import views.html.settings;

import java.util.ArrayList;

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
            return forbidden(generic_failure.render(Config.ServerName, true, Messages.get("settings.passwordChange.localMismatch")));

        String username = AuthManager.currentUsername(request().cookies());

        if (username == null)
            return forbidden(generic_failure.render(Config.ServerName, false, Messages.get("login.userNotFound")));

        UserData user = UserData.getUserDataFromUsername(username);

        if (user == null)
            return forbidden(generic_failure.render(Config.ServerName, false, Messages.get("login.userNotFound")));

        try {
            Password oldP = new Password(user.passwordDigest, user.passwordSalt);

            if (!oldP.validate(oldPassword)) {
                return forbidden(generic_failure.render(Config.ServerName, true, Messages.get("settings.passwordChange.oldMismatch")));
            }

            Password newP = new Password(newPassword);

            user.passwordDigest = newP.digest;
            user.passwordSalt = newP.salt;

            user.save();

            return ok(generic_success.render(Config.ServerName, true, Messages.get("settings.passwordChange.success")));
        } catch (Exception e) {
            return internalServerError(generic_failure.render(Config.ServerName, true, Messages.get("generic.internalServerError") + "! - " + e.getMessage()));
        }
    }

    public Result handleEmailChange() {
        DynamicForm form = Form.form().bindFromRequest();

        String newEmail = form.get("new-email");

        if (newEmail == null || newEmail.equals(""))
            return badRequest(generic_failure.render(Config.ServerName, true, Messages.get("settings.emailChange.blank")));

        Constraints.EmailValidator val = new Constraints.EmailValidator();

        if (!val.isValid(newEmail))
            return badRequest(generic_failure.render(Config.ServerName, true, Messages.get("settings.emailChange.invalid")));

        String username = AuthManager.currentUsername(request().cookies());

        if (username == null)
            return forbidden(generic_failure.render(Config.ServerName, true, Messages.get("login.userNotFound")));

        UserData user = UserData.getUserDataFromUsername(username);

        ArrayList<UserAttribute> toRemove = new ArrayList<>();

        for (UserAttribute a : user.attributes)
            if (a.key.equals("emailChange-newEmail") || a.key.equals("emailChange-key"))
                toRemove.add(a);

        while (toRemove.size() > 0) {
            toRemove.get(0).delete();
            toRemove.remove(0);
        }

        KeyGenerator kg = new KeyGenerator();

        String key = kg.nextKey();

        UserAttribute newEmailAttribute = new UserAttribute(user, "emailChange-newEmail", newEmail);
        UserAttribute emailChangeAttribute = new UserAttribute(user, "emailChange-key", key);

        newEmailAttribute.save();
        emailChangeAttribute.save();

        Mailer m = new Mailer(Config.getServerURL(request()));

        if (m.sendEmailChangeEmail(username, user.emailAddress, newEmail, key)) {
            return ok(generic_success.render(Config.ServerName, true, Messages.get("settings.emailChange.success")));
        } else {
            return internalServerError(generic_failure.render(Config.ServerName, true, Messages.get("settings.emailChange.failure")));
        }
    }

    public Result handleEmailChangeConfirmation() {
        DynamicForm form = Form.form().bindFromRequest();

        String username = form.get("username");
        String key = form.get("key");

        UserData user = UserData.getUserDataFromUsername(username);

        if (user == null)
            return internalServerError(generic_failure.render(Config.ServerName, false, Messages.get("login.userNotFound")));

        UserAttribute ecne = null;
        UserAttribute eck = null;

        for (UserAttribute a : user.attributes) {
            if (a.key.equals("emailChange-newEmail"))
                ecne = a;
            else if (a.key.equals("emailChange-key"))
                eck = a;
        }

        if (!eck.value.equals(key))
            return internalServerError(generic_failure.render(Config.ServerName, true, Messages.get("settings.emailChange.keyMismatch")));

        user.emailAddress = ecne.value;

        eck.delete();
        ecne.delete();

        user.save();

        return ok(generic_success.render(Config.ServerName, true, Messages.get("settings.emailChange.finalSuccess")));
    }
}
