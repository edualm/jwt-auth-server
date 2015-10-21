package utilities;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.lang.JoseException;

/**
 * Created by MegaEduX on 20/10/15.
 */
public class Config {
    static public final String ServerName = "jwt-auth-server";
    static public final Integer MinimumPasswordLength = 8;
    static private RsaJsonWebKey jsonWebKey = null;

    static RsaJsonWebKey getJsonWebKey() {
        if (jsonWebKey == null)
            try {
                jsonWebKey = RsaJwkGenerator.generateJwk(2048);
                jsonWebKey.setKeyId("ldso");
            } catch (JoseException e) {
                System.out.println(e.getMessage());
            }

        return jsonWebKey;
    }
}
