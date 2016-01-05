package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;

/**
 * Created by MegaEduX on 31/10/15.
 */

@Entity
public class Application extends Model {
    @Id
    @SequenceGenerator(name = "application_id_seq", sequenceName = "application_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "application_id_seq")
    @Column(unique = true)
    public Integer id;

    @Constraints.Required
    @Column(unique = true)
    public String name;

    @Constraints.Required
    public Boolean pub;

    //  Admin Groups
    //  Mod Groups

    //  There's a bit to iterate on here.
}
