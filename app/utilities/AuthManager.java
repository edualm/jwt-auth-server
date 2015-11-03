package utilities;

import play.mvc.Http;

/**
 * Created by MegaEduX on 03/11/15.
 */

public class AuthManager {
    public static boolean isLoggedIn(Http.Cookies allCookies) {
        Http.Cookie authCookie = allCookies.get("auth");

        if (authCookie == null)
            return false;

        return (JWTValidator.acceptToken(authCookie.value()));
    }
}
