package controllers;

import play.mvc.*;

import utilities.Config;
import views.html.*;

public class Application extends Controller {

    public Result index() {
        return ok(index.render("Welcome to " + Config.ServerName + "!"));
    }

}
