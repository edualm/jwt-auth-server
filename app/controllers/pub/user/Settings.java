package controllers.pub.user;

import play.mvc.Controller;
import play.mvc.Result;
import utilities.Config;
import views.html.settings;

/**
 * Created by MegaEduX on 27/10/15.
 */

public class Settings extends Controller {

    public Result handleSettings() {
        return ok(settings.render(Config.ServerName));
    }

    public Result handlePasswordChange() {
        return notFound();
    }
}
