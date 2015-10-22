package utilities;

import models.Configuration;
import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.keys.RsaKeyUtil;
import org.jose4j.lang.JoseException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by MegaEduX on 20/10/15.
 */
public class Config {
    private static class NoSavedKeyPairException extends Exception {}

    private static final String kPublicKey = "jwt-publicKey";
    private static final String kPrivateKey = "jwt-privateKey";

    private static RsaJsonWebKey jsonWebKey = null;

    static public final String ServerName = "jwt-auth-server";
    static public final Integer MinimumPasswordLength = 8;

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

        BASE64Encoder encoder = new BASE64Encoder();

        String pub = encoder.encode(kp.getPublic().getEncoded());
        String pri = encoder.encode(kp.getPrivate().getEncoded());

        Configuration pubKey = new Configuration(kPublicKey, pub);
        Configuration priKey = new Configuration(kPrivateKey, pri);

        pubKey.save();
        priKey.save();
    }

    static private void loadJWTKeyPair() throws NoSuchAlgorithmException, IOException,
            InvalidKeySpecException, JoseException, NoSavedKeyPairException {
        KeyFactory kf = KeyFactory.getInstance("RSA");

        BASE64Decoder decoder = new BASE64Decoder();

        Configuration pubKey = Configuration.getConfiguration(kPublicKey);
        Configuration priKey = Configuration.getConfiguration(kPrivateKey);

        if (pubKey == null || priKey == null)
            throw new Config.NoSavedKeyPairException();

        PublicKey pubRecovered = kf.generatePublic(new X509EncodedKeySpec(decoder.decodeBuffer(pubKey.value)));
        PrivateKey priRecovered = kf.generatePrivate(new PKCS8EncodedKeySpec(decoder.decodeBuffer(priKey.value)));

        RsaJsonWebKey rsaJwk = (RsaJsonWebKey) PublicJsonWebKey.Factory.newPublicJwk(pubRecovered);
        rsaJwk.setPrivateKey(priRecovered);

        Config.setJsonWebKey(rsaJwk);
    }
}
