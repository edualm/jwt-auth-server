package controllers.pub.user;

import play.mvc.Controller;
import play.mvc.Result;
import utilities.AuthManager;
import utilities.Config;
import views.html.forbidden;
import views.html.logout_success;

/**
 * Created by MegaEduX on 03/11/15.
 */

public class Logout extends Controller {
    public Result handleLogout() {
        if (AuthManager.isLoggedIn(request().cookies())) {
            response().discardCookie("jwt");

            return ok(logout_success.render(Config.ServerName));
        } else
            return forbidden(forbidden.render(Config.ServerName));
    }
}
