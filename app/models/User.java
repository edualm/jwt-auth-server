package models;

import java.util.HashMap;

/**
 * Created by MegaEduX on 19/10/15.
 */

public class User {
    private class Password {
        public String hash;
        public String salt;

        Password(String hash, String salt) {
            this.hash = hash;
            this.salt = salt;
        }

        public boolean validate(String password) {
            return false;
        }
    }

    public String username;

    public Password password;

    public String emailAddress;

    public HashMap<String, String> additional;

    User(String username, String passwordHash, String passwordSalt, String emailAddress) {
        this.username = username;
        this.password = new Password(passwordHash, passwordSalt);
        this.emailAddress = emailAddress;
    }
}
