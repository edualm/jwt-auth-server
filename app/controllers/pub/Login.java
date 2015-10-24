package controllers.pub;

import play.mvc.Controller;
import play.mvc.Result;
import utilities.Config;
import views.html.login;

/**
 * Created by MegaEduX on 23/10/15.
 */

public class Login extends Controller {

    public Result loginPage() {
        return ok(login.render(Config.ServerName));
    }

}
