package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.Transactional;

import play.data.validation.Constraints;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.persistence.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by MegaEduX on 19/10/15.
 */

@Entity
public class UserInfo extends Model {

    @Id
    @SequenceGenerator(name="user_id_seq", sequenceName="user_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="user_id_seq")
    @Column(unique = true)
    public Long id;

    @Constraints.Required
    @Column(unique = true)
    public String username;

    /*@OneToOne
    @Constraints.Required
    public Password password;*/

    @Constraints.Required
    @Column(unique = true)
    public String emailAddress;

    //  public HashMap<String, String> additional;

    public UserInfo(String username, String password, String emailAddress) {
        this.id = Integer.toUnsignedLong(10);

        this.username = username;

        /*try {
            this.password = new Password(password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }*/

        this.emailAddress = emailAddress;
    }
}
