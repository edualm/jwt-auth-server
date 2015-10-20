package models;

import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.DbIdentity;
import com.avaje.ebean.config.dbplatform.IdType;
import com.avaje.ebean.config.dbplatform.PostgresPlatform;
import com.avaje.ebean.event.ServerConfigStartup;

/**
 * Created by MegaEduX on 20/10/15.
 */

public class DatabaseConfigStartup implements ServerConfigStartup {

    public void onStart(ServerConfig serverConfig) {
        serverConfig.setDatabaseSequenceBatchSize(1);

        PostgresPlatform postgresPlatform = new PostgresPlatform();

        DbIdentity dbIdentity = postgresPlatform.getDbIdentity();

        dbIdentity.setSupportsGetGeneratedKeys(false);
        dbIdentity.setSupportsSequence(true);
        dbIdentity.setIdType(IdType.GENERATOR);

        serverConfig.setDatabasePlatform(postgresPlatform);
    }

}
