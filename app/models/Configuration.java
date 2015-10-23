package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.List;

/**
 * Created by MegaEduX on 22/10/15.
 */

@Entity
public class Configuration extends Model {
    @Id
    @SequenceGenerator(name = "configuration_id_seq", sequenceName = "configuration_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "configuration_id_seq")
    @Column(unique = true)
    public Integer id;

    @Constraints.Required
    @Column(unique = true)
    public String key;

    @Constraints.Required
    @Column(unique = true)
    public String value;

    @Nullable
    public static Configuration getConfiguration(String key) {
        List<Configuration> results = Ebean.find(Configuration.class).where().eq("key", key).findList();

        if (results.size() != 0)
            return results.get(0);

        return null;
    }

    public Configuration(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
