package utilities;

import com.sendgrid.SendGrid;

/**
 * Created by MegaEduX on 31/10/15.
 */

public class Mailer {

    private String serverURL = null;

    public Mailer(String serverURL) {
        this.serverURL = serverURL;
    }

    public boolean sendValidationEmail(String username, String email, String validationKey) {
        SendGrid.Email e = makeEmailEasy(email,
                Config.kEmailFrom,
                "Validate your registration on " + Config.ServerName,
                "Hello,\n\nThanks for your registration!\n\nPlease validate your account at " +
                        serverURL + "register/validate?username=" + username + "&validationKey=" +
                        validationKey +"\n\nThanks,\n" + Config.ServerName);

        return send(e);
    }

    public boolean sendEmailChangeEmail(String username, String email, String newEmail, String key) {
        SendGrid.Email e = makeEmailEasy(email,
                Config.kEmailFrom,
                "E-Mail Change Request: " + Config.ServerName,
                "Hello,\n\nA request was made by you or someone on your behalf to change the e-mail address associated with the account " + username + " + to " + newEmail +
                "\n\nIf you wish to perform this change, please accept it at the following address: " +
                serverURL + "settings/confirmChangeEmail?username=" + username + "&key=" +
                key + "\n\nThanks,\n" + Config.ServerName);

        return send(e);
    }

    public boolean sendEmailPasswordChanged(String username, String email, String newPassword) {
        SendGrid.Email e = makeEmailEasy(email,
                Config.kEmailFrom,
                "New Password: " + Config.ServerName,
                "Hello,\n\nA request was made by you or someone on your behalf to change the password address associated with the account " + username + "." +
                        "\n\nYour new password is: " + newPassword +
                        "\n\nThanks,\n" + Config.ServerName);

        return send(e);
    }

    private SendGrid.Email makeEmailEasy(String to, String from, String subject, String text) {
        SendGrid.Email e = new SendGrid.Email();

        e.addTo(to);
        e.setFrom(from);
        e.setSubject(subject);
        e.setText(text);

        return e;
    }

    private boolean send(SendGrid.Email e) {
        SendGrid sg = new SendGrid(Config.kSendGridUsername, Config.kSendGridPassword);

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
