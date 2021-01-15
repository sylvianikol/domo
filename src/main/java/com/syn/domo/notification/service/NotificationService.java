package com.syn.domo.notification.service;

import com.syn.domo.model.service.UserServiceModel;
import com.syn.domo.scheduled.ScheduledFeesGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static com.syn.domo.common.EmailTemplates.*;

@Service
public class NotificationService {

    private static final Logger log =
            LoggerFactory.getLogger(ScheduledFeesGenerator.class);

    private final JavaMailSender javaMailSender;

    @Autowired
    public NotificationService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendNewFeeNotificationEmail(UserServiceModel user) throws MailException {

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(user.getEmail());
        mail.setFrom("MAIL_USER");
        mail.setSubject(NEW_FEE_SUBJECT);
        mail.setText("Fee total, due date, etc");

        this.javaMailSender.send(mail);

        log.info("========== EMAIL SENT ============");
    }

    public void sendActivationEmail(UserServiceModel user) throws MailException, MessagingException {

        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        String emailTemplate = String.format(EMAIL_ACTIVATION_TEMPLATE,
                user.getFirstName(), user.getLastName(), user.getId());

//        mimeMessage.setContent(emailTemplate, "text/html");
        helper.setText(emailTemplate, true); // Use this or the above line.

        helper.setTo(user.getEmail());
        helper.setSubject(EMAIL_ACTIVATION_SUBJECT);
        helper.setFrom(System.getenv("MAIL_USER"));

        this.javaMailSender.send(mimeMessage);

        log.info("========== EMAIL SENT ============");
    }
}
