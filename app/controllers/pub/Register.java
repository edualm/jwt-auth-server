package controllers.pub;

import play.mvc.Controller;
import play.mvc.Result;
import utilities.Config;
import views.html.register;

/**
 * Created by MegaEduX on 23/10/15.
 */

public class Register extends Controller {
    public Result registerPage() {
        return ok(register.render(Config.ServerName));
    }
}
