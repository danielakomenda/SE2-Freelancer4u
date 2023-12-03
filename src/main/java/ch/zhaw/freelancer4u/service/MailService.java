package ch.zhaw.freelancer4u.service;

import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import ch.zhaw.freelancer4u.model.Mail;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import ch.zhaw.freelancer4u.model.Credentials;

@Service
public class MailService {

    Credentials mailCredentials = new Credentials();

    private String username = mailCredentials.getUsername();
    private String password = mailCredentials.getPassword();

    private static final Logger logger = LoggerFactory.getLogger(MailService.class);
    private static final String FROM_MAIL = "someoneelse@hotmail.ch";

    
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        
        mailSender.setHost("smtp.office365.com");
        mailSender.setPort(587);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    public boolean sendMail(Mail mail) {
        try {

            
            ///////////////////////////////////////////////////////////////
            // You can change your existing Emailadress to an Alias-Adress
            // But the Emails end up in the spam folder
            ///////////////////////////////////////////////////////////////
            
            MimeMessage mimeMessage = getJavaMailSender().createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
            helper.setFrom(new InternetAddress("someoneelse@hotmail.ch", "daniela@freelancer4u.ch"));
            helper.setTo(mail.getTo());
            helper.setSubject(mail.getSubject());
            helper.setText(mail.getMessage(), true);
            getJavaMailSender().send(mimeMessage);
            

            
            /* 
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("Daniela <someoneelse@hotmail.ch>");
            message.setTo(mail.getTo());
            message.setSubject(mail.getSubject());
            message.setText(mail.getMessage());

            var emailSender = getJavaMailSender();
            emailSender.send(message);
            */
            
            return true;
        } catch (Exception e) {
            logger.error("Error sending the mail", e);
        }
        return false;
    }
}

