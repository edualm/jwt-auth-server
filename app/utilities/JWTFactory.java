package utilities;

import models.UserData;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;

import java.util.Arrays;
import java.util.List;

/**
 * Created by MegaEduX on 20/10/15.
 */
public class JWTFactory {
    public static String createAuthenticationJWT(UserData user, String ip, boolean longToken)
            throws JoseException {
        return createAuthenticationJWT(user, ip, "auth", Config.ServerName, longToken);
    }

    public static String createAuthenticationJWT(UserData user, String ip, String audience, String subject, boolean longToken)
            throws JoseException {
        JwtClaims claims = new JwtClaims();

        claims.setIssuer(Config.ServerName);
        claims.setAudience(audience);
        claims.setExpirationTimeMinutesInTheFuture(longToken ? 20160 : 120); // 2 weeks or 2 hours
        claims.setGeneratedJwtId();
        claims.setIssuedAtToNow();
        claims.setNotBeforeMinutesInThePast(2);
        claims.setSubject(subject);
        claims.setClaim("username", user.username);
        claims.setClaim("id", user.id);
        claims.setClaim("ip", ip);

        JsonWebSignature jws = new JsonWebSignature();

        jws.setPayload(claims.toJson());
        jws.setKey(Config.getJsonWebKey().getPrivateKey());
        jws.setKeyIdHeaderValue(Config.getJsonWebKey().getKeyId());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        return jws.getCompactSerialization();
    }
}
