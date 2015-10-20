package models;

import com.avaje.ebean.Model;

import play.data.validation.Constraints;

import javax.persistence.*;

/**
 * Created by MegaEduX on 19/10/15.
 */

@Entity
public class UserData extends Model {

    @Id
    @SequenceGenerator(name="user_data_id_seq", sequenceName="user_data_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="user_data_id_seq")
    @Column(unique = true)
    public Integer id;

    @Constraints.Required
    @Column(unique = true)
    public String username;

    @Constraints.Required
    public String passwordDigest;

    @Constraints.Required
    public String passwordSalt;

    @Constraints.Required
    @Column(unique = true)
    public String emailAddress;

    //  public HashMap<String, String> additional;

    public UserData(String username, String password, String emailAddress) {
        this.username = username;
        this.emailAddress = emailAddress;

        try {
            Password p = new Password(password);

            this.passwordDigest = p.digest;
            this.passwordSalt = p.salt;
        } catch (Exception e) {

        }
    }
}
