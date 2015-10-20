package models;

import com.avaje.ebean.Model;

import play.data.validation.Constraints;

import javax.persistence.*;

/**
 * Created by MegaEduX on 19/10/15.
 */

@Entity
public class User extends Model {

    @Id
    @SequenceGenerator(name="user_id_seq", sequenceName="user_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="user_id_seq")
    @Column(unique = true)
    public Long id;

    @Constraints.Required
    @Column(unique = true)
    public String username;

    @OneToOne
    @Constraints.Required
    public Password password;

    @Constraints.Required
    @Column(unique = true)
    public String emailAddress;

    //  public HashMap<String, String> additional;

    public User(String username, String password, String emailAddress) {
        this.id = Integer.toUnsignedLong(10);

        this.username = username;

        try {
            this.password = new Password(password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        this.emailAddress = emailAddress;
    }
}
