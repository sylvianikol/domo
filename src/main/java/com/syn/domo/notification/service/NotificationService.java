package com.syn.domo.notification.service;

import com.syn.domo.model.entity.Fee;
import com.syn.domo.model.service.UserServiceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static com.syn.domo.common.EmailTemplates.*;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final JavaMailSender javaMailSender;

    @Autowired
    public NotificationService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendNewFeeNotificationEmail(UserServiceModel user, Fee fee) throws MailException, MessagingException, InterruptedException {

        log.info("Sleeping...");
        Thread.sleep(10000);

        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        String emailTemplate = String.format(FEE_NOTICE_TEMPLATE,
                user.getFirstName(), user.getLastName(),
                fee.getApartment().getNumber(),
                fee.getApartment().getBuilding().getName(),
                fee.getTotal(), fee.getDueDate(), fee.getId());

        helper.setText(emailTemplate, true);
        helper.setTo(user.getEmail());
        helper.setSubject(NEW_FEE_SUBJECT);
        helper.setFrom(System.getenv("MAIL_USER"));

        log.info("Sending...");
        this.javaMailSender.send(mimeMessage);

        log.info("========== NEW FEE NOTIFICATION SENT ============");
    }

    @Async
    public void sendFeePaymentReceipt(UserServiceModel user, Fee fee) throws MessagingException, InterruptedException {

        log.info("Sleeping...");
        Thread.sleep(10000);

        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        String emailTemplate = String.format(FEE_RECEIPT_TEMPLATE,
                user.getFirstName(), user.getLastName(),
                fee.getApartment().getNumber(),
                fee.getApartment().getBuilding().getName(),
                fee.getTotal(), fee.getIssueDate(), fee.getDueDate());

        helper.setText(emailTemplate, true);
        helper.setTo(user.getEmail());
        helper.setSubject(FEE_RECEIPT_SUBJECT);
        helper.setFrom(System.getenv("MAIL_USER"));

        log.info("Sending...");
        this.javaMailSender.send(mimeMessage);

        log.info("========== FEE RECEIPT SENT ============");
    }

    @Async
    public void sendActivationEmail(UserServiceModel user) throws MailException, MessagingException, InterruptedException {

        log.info("Sleeping...");
        Thread.sleep(10000);

        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        String emailTemplate = String.format(EMAIL_ACTIVATION_TEMPLATE,
                user.getFirstName(), user.getLastName(), user.getId());

        mimeMessage.setContent(emailTemplate, "text/html");
//        helper.setText(emailTemplate, true); // Use this or the above line.

        helper.setTo(user.getEmail());
        helper.setSubject(EMAIL_ACTIVATION_SUBJECT);
        helper.setFrom(System.getenv("MAIL_USER"));

        log.info("Sending...");
        this.javaMailSender.send(mimeMessage);

        log.info("========== EMAIL ACTIVATION SENT ============");
    }
}
