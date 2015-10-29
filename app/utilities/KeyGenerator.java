package utilities;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by MegaEduX on 29/10/15.
 */

public final class KeyGenerator {
    private SecureRandom random = new SecureRandom();

    public String nextKey() {
        return new BigInteger(130, random).toString(32);
    }
}
