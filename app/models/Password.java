package models;

import com.avaje.ebean.Model;
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

/**
 * Created by MegaEduX on 20/10/15.
 */

@Entity
public class Password extends Model {
    private static int kIterationCount = 1024;

    //  Partially stolen from https://www.owasp.org/index.php/Hashing_Java

    @Id
    @Column(unique = true)
    //  @SequenceGenerator(name="password_gen", sequenceName = "password_idcolumn_seq", allocationSize = 1)
    //  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "password_gen")
    @GeneratedValue(strategy = GenerationType.TABLE)
    public Long id;

    @Constraints.Required
    public String digest;

    @Constraints.Required
    public String salt;

    public Password(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

        byte[] bSalt = new byte[8];

        random.nextBytes(bSalt);

        byte[] bDigest = getHash(kIterationCount, password, bSalt);

        this.digest = byteToBase64(bDigest);
        this.salt = byteToBase64(bSalt);
    }

    public Password(String digest, String salt) {
        this.id = Integer.toUnsignedLong(20);
        this.digest = digest;
        this.salt = salt;
    }

    public boolean validate(String password) throws IOException, NoSuchAlgorithmException {
        byte[] digest = base64ToByte(this.digest);
        byte[] salt = base64ToByte(this.salt);

        byte[] hash = getHash(kIterationCount, password, salt);

        return Arrays.equals(digest, hash);
    }

    private byte[] getHash(int iterationNb, String password, byte[] salt)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");

        digest.reset();
        digest.update(salt);

        byte[] input = digest.digest(password.getBytes("UTF-8"));

        for (int i = 0; i < iterationNb; i++) {
            digest.reset();
            input = digest.digest(input);
        }

        return input;
    }

    private byte[] base64ToByte(String data) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        return decoder.decodeBuffer(data);
    }

    private String byteToBase64(byte[] data){
        BASE64Encoder encoder = new BASE64Encoder();

        return encoder.encode(data);
    }
}
