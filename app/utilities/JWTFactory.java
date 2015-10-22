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
    public static String createJWT() throws JoseException {
        //
        // JSON Web Token is a compact URL-safe means of representing claims/attributes to be transferred between two parties.
        // This example demonstrates producing and consuming a signed JWT
        //

        // Generate an RSA key pair, which will be used for signing and verification of the JWT, wrapped in a JWK
        RsaJsonWebKey rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);

        // Give the JWK a Key ID (kid), which is just the polite thing to do
        rsaJsonWebKey.setKeyId("k1");

        // Create the Claims, which will be the content of the JWT
        JwtClaims claims = new JwtClaims();
        claims.setIssuer(Config.ServerName);  // who creates the token and signs it
        claims.setAudience("Audience"); // to whom the token is intended to be sent
        claims.setExpirationTimeMinutesInTheFuture(10); // time when the token will expire (10 minutes from now)
        claims.setGeneratedJwtId(); // a unique identifier for the token
        claims.setIssuedAtToNow();  // when the token was issued/created (now)
        claims.setNotBeforeMinutesInThePast(2); // time before which the token is not yet valid (2 minutes ago)
        claims.setSubject("subject"); // the subject/principal is whom the token is about
        claims.setClaim("email", "mail@example.com"); // additional claims/attributes about the subject can be added
        List<String> groups = Arrays.asList("group-one", "other-group", "group-three");
        claims.setStringListClaim("groups", groups); // multi-valued claims work too and will end up as a JSON array

        // A JWT is a JWS and/or a JWE with JSON claims as the payload.
        // In this example it is a JWS so we create a JsonWebSignature object.
        JsonWebSignature jws = new JsonWebSignature();

        // The payload of the JWS is JSON content of the JWT Claims
        jws.setPayload(claims.toJson());

        // The JWT is signed using the private key
        jws.setKey(rsaJsonWebKey.getPrivateKey());

        // Set the Key ID (kid) header because it's just the polite thing to do.
        // We only have one key in this example but a using a Key ID helps
        // facilitate a smooth key rollover process
        jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());

        // Set the signature algorithm on the JWT/JWS that will integrity protect the claims
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        // Sign the JWS and produce the compact serialization or the complete JWT/JWS
        // representation, which is a string consisting of three dot ('.') separated
        // base64url-encoded parts in the form Header.Payload.Signature
        // If you wanted to encrypt it, you can simply set this jwt as the payload
        // of a JsonWebEncryption object and set the cty (Content Type) header to "jwt".

        String jwt = jws.getCompactSerialization();

        return jwt + " " + rsaJsonWebKey.getPublicKey() + " " + rsaJsonWebKey.getPrivateKey();
    }

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
