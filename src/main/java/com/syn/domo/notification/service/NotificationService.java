package com.syn.domo.notification.service;

import com.syn.domo.model.service.UserServiceModel;
import com.syn.domo.scheduled.ScheduledFeesGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log =
            LoggerFactory.getLogger(ScheduledFeesGenerator.class);

    private final JavaMailSender javaMailSender;

    @Autowired
    public NotificationService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(UserServiceModel resident) throws MailException {

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(resident.getEmail());
        mail.setFrom("silviaadvert@gmail.com");
        mail.setSubject("New apartment fee");
        mail.setText("Fee total, due date, etc");

        javaMailSender.send(mail);
        log.info("===============   EMAIL SENT  ====================");
    }
}
