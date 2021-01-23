package com.syn.domo.notification.service;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.syn.domo.config.ApplicationBeanConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ApplicationBeanConfiguration.class)
class NotificationServiceTest {

    @Resource
    private JavaMailSenderImpl emailSender;

    private GreenMail testSmtp;

    @BeforeEach
    void testSmtpInit() {
        testSmtp = new GreenMail(ServerSetupTest.SMTP);
        testSmtp.setUser("username", "secret");
        testSmtp.start();

        this.emailSender.setPort(3025);
        this.emailSender.setHost("localhost");
    }

    @AfterEach
    public void cleanup(){
        testSmtp.stop();
    }

    @Test
    void test_sendNewFeeNotificationEmail() throws MessagingException {

        MimeMessage mimeMessage = this.emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setText("test message", true);
        helper.setTo("test@receiver.com");
        helper.setSubject("test subject");
        helper.setFrom("test@sender.com");

        this.emailSender.send(mimeMessage);

        MimeMessage[] messages = testSmtp.getReceivedMessages();
        assertEquals(1, messages.length);
        assertEquals("test subject", messages[0].getSubject());
        String body = GreenMailUtil.getBody(messages[0]).replaceAll("=\r?\n", "");
        assertEquals("test message", body);
    }

    @Test
    void test_sendFeePaymentReceipt() {
    }

    @Test
    void sendActivationEmail() {
    }
}