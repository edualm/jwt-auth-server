package utilities;

import java.time.Instant;
import java.util.HashMap;

/**
 * Created by MegaEduX on 02/12/15.
 */

public class LoginCooldown {

    private class CooldownStructure {
        private int tries;
        private double lastTry;

        public CooldownStructure() {
            tries = 1;
            lastTry = Instant.now().getEpochSecond();
        }

        public void addTry() {
            tries++;
        }

        public int getCooldown() {
            if (tries >= 3) {
                int cd = (int) (lastTry + CooldownAfterThreeTries - Instant.now().getEpochSecond());

                if (cd < 0)
                    return 0;

                return cd;
            }

            return 0;
        }
    }

    public int CooldownAfterThreeTries = 60;

    private HashMap<String, CooldownStructure> structs = new HashMap<>();

    private static LoginCooldown singleton = null;

    private LoginCooldown() {

    }

    public static LoginCooldown getInstance() {
        if (singleton == null)
            singleton = new LoginCooldown();

        return singleton;
    }

    public void addFailedTryForUsername(String u) {
        CooldownStructure s = structs.get(u);

        if (s == null)
            s = new CooldownStructure();

            structs.put(u, s);
        else
            s.addTry();
    }

    public int getCooldownForUsername(String u) {
        CooldownStructure s = structs.get(u);

        if (s == null)
            return 0;
        else {
            int cd = s.getCooldown();

            if (cd == 0)
                removeCooldown(u);

            return cd;
        }
    }

    public void removeCooldown(String u) {
        if (structs.get(u) != null)
            structs.remove(u);
    }

}
