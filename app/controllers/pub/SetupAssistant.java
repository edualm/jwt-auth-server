package controllers.pub;

import models.Configuration;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.setup_assistant;

/**
 * Created by MegaEduX on 27/10/15.
 */

public class SetupAssistant extends Controller {
    static final String ConfiguredKey = "ServerConfigured";

    public Result setupAssistantView() {
        Configuration conf = Configuration.getConfiguration(ConfiguredKey);

        if (conf != null && conf.value.equals("true")) {
            return forbidden();
        }

        return ok(setup_assistant.render());
    }
}
