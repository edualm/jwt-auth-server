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

        public int getTries() {
            return tries;
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

        if (s == null) {
            s = new CooldownStructure();

            structs.put(u, s);

            System.out.println("Created failed try object for " + u);
        } else {
            s.addTry();

            System.out.println(u + " now has " + s.getTries() + " tries.");
        }
    }

    public int getCooldownForUsername(String u) {
        CooldownStructure s = structs.get(u);

        if (s == null) {
            System.out.println(u + " has no cooldown struct.");

            return 0;
        } else {
            int cd = s.getCooldown();

            if (cd == 0 && s.getTries() >= 3) {
                System.out.println("Removing cooldown...");
                
                removeCooldown(u);
            }

            System.out.println(u + " has the cooldown " + cd);

            return cd;
        }
    }

    public void removeCooldown(String u) {
        if (structs.get(u) != null)
            structs.remove(u);
    }

}
