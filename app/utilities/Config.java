package utilities;

import models.Configuration;

import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.keys.RsaKeyUtil;
import org.jose4j.lang.JoseException;
import play.mvc.Http;

import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Created by MegaEduX on 20/10/15.
 */
public class Config {
    private static class NoSavedKeyPairException extends Exception {}

    private static final String kPublicKey = "jwt-publicKey";
    private static final String kPrivateKey = "jwt-privateKey";

    public static final String kSendGridUsername = "";
    public static final String kSendGridPassword = "";

    public static final String kEmailFrom = "";

    private static RsaJsonWebKey jsonWebKey = null;

    static public final String ServerName = "";

    static public final Integer MinimumPasswordLength = 8;

    static public String getServerURL(Http.Request request) {
        return "https://" + getServerURI(request) + "/";
    }

    static public String getServerURI(Http.Request request) {
        return request.headers().get("Host")[0];
    }

    static public RsaJsonWebKey getJsonWebKey() {
        if (jsonWebKey == null)
            try {
                try {
                    loadJWTKeyPair();

                    System.out.println("Loaded JWT Key Pair from database!");
                } catch (Exception e) {
                    generateJWTKeyPair();

                    System.out.println("Failed to load JWT Key Pair. Generated new...");
                }
            } catch (JoseException e) {
                System.out.println(e.getMessage());
            }

        return jsonWebKey;
    }

    static public void setJsonWebKey(RsaJsonWebKey key) {
        jsonWebKey = key;
    }

    static private void generateJWTKeyPair() throws JoseException {
        KeyPair kp = (new RsaKeyUtil()).generateKeyPair(2048);

        String pub = Base64.getEncoder().encodeToString(kp.getPublic().getEncoded());
        String pri = Base64.getEncoder().encodeToString(kp.getPrivate().getEncoded());

        Configuration pubKey = new Configuration(kPublicKey, pub);
        Configuration priKey = new Configuration(kPrivateKey, pri);

        pubKey.save();
        priKey.save();

        setJsonWebKey(createKey(kp.getPublic(), kp.getPrivate()));
    }

    static private void loadJWTKeyPair() throws NoSuchAlgorithmException, IOException,
            InvalidKeySpecException, JoseException, NoSavedKeyPairException {
        KeyFactory kf = KeyFactory.getInstance("RSA");

        Configuration pubKey = Configuration.getConfiguration(kPublicKey);
        Configuration priKey = Configuration.getConfiguration(kPrivateKey);

        if (pubKey == null || priKey == null)
            throw new Config.NoSavedKeyPairException();

        PublicKey pubRecovered = kf.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(pubKey.value)));
        PrivateKey priRecovered = kf.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(priKey.value)));

        Config.setJsonWebKey(createKey(pubRecovered, priRecovered));
    }

    static private RsaJsonWebKey createKey(PublicKey pub, PrivateKey pri) throws JoseException {
        RsaJsonWebKey rsaJwk = (RsaJsonWebKey) PublicJsonWebKey.Factory.newPublicJwk(pub);
        rsaJwk.setPrivateKey(pri);

        return rsaJwk;
    }
}
