package utilities;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;

/**
 * Created by MegaEduX on 03/11/15.
 */

public class JWTValidator {
    public static boolean acceptToken(String jwt) {
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime() // the JWT must have an expiration time
                .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
                .setRequireSubject() // the JWT must have a subject claim
                .setExpectedIssuer(Config.ServerName) // whom the JWT needs to have been issued by
                .setExpectedAudience("auth") // to whom the JWT is intended for
                .setVerificationKey(Config.getJsonWebKey().getKey()) // verify the signature with the public key
                .build(); // create the JwtConsumer instance

        try {
            JwtClaims jwtClaims = jwtConsumer.processToClaims(jwt);

            System.out.println("JWT validation succeeded! " + jwtClaims);

            return true;
        } catch (InvalidJwtException e) {
            System.out.println("Invalid JWT! " + e);

            return false;
        }
    }
}
