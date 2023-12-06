package org.example;

import interfaces.AdminInterface;
import javax.mail.Session;

import db.connection.DatabaseConnector;
import demandes.Demand;
import services.emailing.SendEmail;
//import services.emailing.SendEmail;
import javax.mail.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;


public class Main {
    public static void main(String[] args) {

         try {
            new SendEmail().sendMail("A new message", """
                        Dear reader,
                                        
                        Hello world.
                                        
                        Best regards,
                        myself
                        """);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Set up mail server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Set up mail session
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("aensate@gmail.com", "qkwm agor oddh xati");
            }
        });

        // Compose the email
        String toEmail = "yassir7t@gmail.com";
        String subject = "Test Subject";
        String body = "<h1>This is a test email</h1><p>Hello, World!</p>";

        // Send the email
//        SendEmail.sendEmail(session, toEmail, subject, body);


        try {
            Connection connection = DatabaseConnector.getConnection();
            if (connection != null) {
                System.out.println("Connected to the database!");
            } else {
                System.err.println("Failed to connect to the database.");
                System.exit(1);
            }
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            System.exit(1);
        }

        // Launch the admin interface
        SwingUtilities.invokeLater(() -> {
            AdminInterface adminInterface = new AdminInterface();
            adminInterface.setSize(800, 600);
            adminInterface.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            adminInterface.setVisible(true);
        });

    }
}


