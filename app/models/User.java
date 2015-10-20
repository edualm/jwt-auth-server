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
    public Long id;

    @Constraints.Required
    @Column(unique = true)
    public String username;

    @Constraints.Required
    public Password password;

    @Constraints.Required
    @Column(unique = true)
    public String emailAddress;

    public HashMap<String, String> additional;

    public User(String username, String password, String emailAddress) {
        this.username = username;

        try {
            this.password = new Password(password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        this.emailAddress = emailAddress;
    }

    @Transactional
    public void save() {
        //  Uh.
    }
}
