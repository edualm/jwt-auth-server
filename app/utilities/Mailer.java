package utilities;

import com.sendgrid.SendGrid;

/**
 * Created by MegaEduX on 31/10/15.
 */

public class Mailer {
    public static boolean sendValidationEmail(String username, String email, String validationKey) {
        SendGrid sg = new SendGrid(Config.kSendGridUsername, Config.kSendGridPassword);

        SendGrid.Email e = new SendGrid.Email();

        e.addTo(email);
        e.setFrom(Config.kEmailFrom);
        e.setSubject("Validate your registration on " + Config.ServerName);
        e.setText("Hello,\n\nThanks for your registration!\n\nPlease validate your account at " +
                Config.ServerURL + "register/validate?username=" + username + "&validationKey=" +
                validationKey +"\n\nThanks,\n" + Config.ServerName);

        try {
            SendGrid.Response r = sg.send(e);

            System.out.println("[SendGrid DEBUG] " + r);

            return true;
        } catch (Exception ex) {
            System.err.println(ex);

            return false;
        }
    }
}
