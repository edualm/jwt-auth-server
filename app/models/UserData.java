package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;

import play.data.validation.Constraints;
import utilities.Config;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by MegaEduX on 19/10/15.
 */

@Entity
public class UserData extends Model {

    //  -- Exceptions

    public class AttributeAlreadyExistsException extends Exception {}
    public class AttributeNotFoundException extends Exception {}

    public class PasswordTooWeakException extends Exception {
        public PasswordTooWeakException(String message) {
            super(message);
        }
    }

    //  -- Exceptions

    @Id
    @SequenceGenerator(name = "user_data_id_seq", sequenceName = "user_data_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_data_id_seq")
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

    @Constraints.Required
    public boolean enabled;

    @Constraints.Required
    public Timestamp signupTime;

    @OneToMany(mappedBy = "user")
    public List<UserAttribute> attributes;

    @ManyToMany(mappedBy = "users")
    public List<Group> groups;

    public UserData(String username, String password, String emailAddress) {
        this.username = username;
        this.emailAddress = emailAddress;

        try {
            Password p = new Password(password);

            this.passwordDigest = p.digest;
            this.passwordSalt = p.salt;
        } catch (Exception e) {

        }

        this.enabled = true;
        this.signupTime = new Timestamp((new Date()).getTime());
    }

    public void addAttribute(String k, String v) throws AttributeAlreadyExistsException {
        List<UserAttribute> exists = Ebean.find(UserAttribute.class).where().eq("user_id", id).eq("key", k).findList();

        if (exists.size() != 0)
            throw new AttributeAlreadyExistsException();

        UserAttribute a = new UserAttribute(this, k, v);

        a.save();
    }

    public void removeAttribute(String k) throws AttributeNotFoundException {
        List<UserAttribute> exists = Ebean.find(UserAttribute.class).where().eq("user_id", id).eq("key", k).findList();

        if (exists.size() == 0)
            throw new AttributeNotFoundException();

        exists.get(0).delete();
    }

    public void changePassword(String password) throws PasswordTooWeakException {
        if (password.length() < Config.MinimumPasswordLength)
            throw new PasswordTooWeakException("Your password is too short! Minimum: "
                    + Config.MinimumPasswordLength + " characters. Current: "
                    + password.length() + " characters.");
        try {
            Password p = new Password(password);

            this.passwordDigest = p.digest;
            this.passwordSalt = p.salt;
        } catch (Exception e) {

        }
    }
}
