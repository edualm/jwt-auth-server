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
public class User extends Model {
    @Id
    @Column(unique = true)
    //  @SequenceGenerator(name="user_gen", sequenceName = "user_idcolumn_seq", allocationSize = 1)
    //  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_gen")
    @GeneratedValue(strategy = GenerationType.TABLE)
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
