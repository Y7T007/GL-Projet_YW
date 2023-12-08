


package services.emailing;


import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {

    public static void main(String[] args) {
        String toEmail = "yassir7t@gmail.com";
        String subject = "Hello";
        String message = "Good day!";

        try {
            EmailSender.sendEmail(toEmail, subject, message);
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            System.out.println("Failed to send email. Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void sendEmail(String toEmail, String subject, String messageText) throws MessagingException {
        // Replace the following with your actual email configuration
        String host = "smtp.gmail.com";
        String port = "587";  // Typically 587 for TLS
        String username = "aensate@gmail.com";
        String password = "qkwm agor oddh xati";

        // Set up email properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
//        properties.put("mail.smtp.ssl.trust", "smtp.your-email-provider.com");


        // Create a session with the email server
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {


                return new PasswordAuthentication(username, password);
            }
        });

        // Create a MimeMessage object
        Message message = new MimeMessage(session);

        // Set the sender address
        message.setFrom(new InternetAddress(username));

        // Set the recipient address
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));

        // Set the email subject
        message.setSubject(subject);

        // Set the email content
        message.setText(messageText);

        // Send the email
        Transport.send(message);
    }
}
