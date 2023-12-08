package services.pdfGen;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class pdfGen {
    public static void main(String[] args) {
        String pdfContent = "Hello, Nossair kitty!";
        String studentEmail = "yassir7t@gmail.com";

        generateAndSendPDF(pdfContent, studentEmail);
    }

    private static void generateAndSendPDF(String pdfContent, String studentEmail) {
        // Load the template PDF
        try (PDDocument document = new PDDocument()) {
            // Create a new page
            PDPage page = new PDPage();
            document.addPage(page);

            // Create a content stream for the page
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Set font and size
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);

            // Write text to the page
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText(pdfContent);
            contentStream.endText();

            // Close the content stream
            contentStream.close();

            // Save the modified PDF
            document.save("output.pdf");

            // Send the PDF as an email attachment
            sendEmailWithAttachment(studentEmail, "aensate@gmail.com", "PDF Attachment", "Please find the attached PDF.", "output.pdf");

            // Close the document
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendEmailWithAttachment(String recipient, String sender, String subject, String body, String attachmentPath) {
        // Set up mail server properties
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        // Create a session with authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("aensate@gmail.com", "qkwm agor oddh xati");
            }
        });

        try {
            // Create a new message
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subject);

            // Create the message body
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);

            // Create the attachment
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(new File(attachmentPath));

            // Create a multipart message and add the parts
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentPart);

            // Set the content of the message to the multipart
            message.setContent(multipart);

            // Send the message with animation
            sendEmailWithAnimation(message);
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendEmailWithAnimation(MimeMessage message) {
        // Create a Swing frame
        JFrame frame = new JFrame("Sending Email");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new FlowLayout());

        // Create a label for the animation
        JLabel animationLabel = new JLabel("Sending email...");

        // Create a button to send the email
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Disable the send button
                sendButton.setEnabled(false);

                // Start the animation
                startAnimation(animationLabel);

                // Send the email in a separate thread
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Send the message
                            Transport.send(message);

                            // Stop the animation
                            stopAnimation(animationLabel);

                            // Show a success message
                            JOptionPane.showMessageDialog(frame, "Email sent successfully!");
                        } catch (MessagingException ex) {
                            ex.printStackTrace();
                            // Stop the animation
                            stopAnimation(animationLabel);

                            // Show an error message
                            JOptionPane.showMessageDialog(frame, "Failed to send email.");
                        }
                    }
                }).start();
            }
        });

        // Add components to the frame
        frame.add(animationLabel);
        frame.add(sendButton);

        // Show the frame
        frame.setVisible(true);
    }

    private static void startAnimation(JLabel label) {
        // Start the animation by changing the label text
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        label.setText("Sending email...");
                        Thread.sleep(500);
                        label.setText("Sending email   ");
                        Thread.sleep(500);
                        label.setText("Sending email.  ");
                        Thread.sleep(500);
                        label.setText("Sending email.. ");
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private static void stopAnimation(JLabel label) {
        // Stop the animation by setting the label text
        label.setText("Email sent");
    }
}
