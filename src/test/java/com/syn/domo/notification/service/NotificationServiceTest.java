package com.syn.domo.notification.service;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.syn.domo.config.ApplicationBeanConfiguration;
import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.entity.Fee;
import com.syn.domo.model.entity.Role;
import com.syn.domo.model.entity.UserRole;
import com.syn.domo.model.service.RoleServiceModel;
import com.syn.domo.model.service.UserServiceModel;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static com.syn.domo.common.EmailTemplates.*;
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

        String emailTemplate = String.format(FEE_NOTICE_TEMPLATE,
                "John", "Doe", "1", "Block 1", BigDecimal.valueOf(50),
                LocalDate.now().plusMonths(1), "66666");

        helper.setText(emailTemplate, true);
        helper.setTo("test@receiver.com");
        helper.setSubject("test subject");
        helper.setFrom("test@sender.com");

        this.emailSender.send(mimeMessage);

        MimeMessage[] messages = testSmtp.getReceivedMessages();
        assertEquals(1, messages.length);
        assertEquals("test subject", messages[0].getSubject());
        String body = GreenMailUtil.getBody(messages[0]).replaceAll("=\r?\n", "");
        assertEquals(emailTemplate, body);
    }

    @Test
    void test_sendFeePaymentReceipt() throws MessagingException {

        MimeMessage mimeMessage = this.emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        String emailTemplate = String.format(FEE_RECEIPT_TEMPLATE,
                "John", "Doe", "1", "Block 1", BigDecimal.valueOf(50),
                LocalDate.now(), LocalDate.now().plusMonths(1));

        helper.setText(emailTemplate, true);
        helper.setTo("test@receiver.com");
        helper.setSubject("test subject");
        helper.setFrom("test@sender.com");

        this.emailSender.send(mimeMessage);

        MimeMessage[] messages = testSmtp.getReceivedMessages();
        assertEquals(1, messages.length);
        assertEquals("test subject", messages[0].getSubject());
        String body = GreenMailUtil.getBody(messages[0]).replaceAll("=\r?\n", "");
        assertEquals(emailTemplate, body);
    }

    @Test
    void test_sendActivationEmail() throws MessagingException {

        MimeMessage mimeMessage = this.emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        String emailTemplate = String.format(EMAIL_ACTIVATION_TEMPLATE,
                "John", "Doe", "1");

        helper.setText(emailTemplate, true);
        helper.setTo("test@receiver.com");
        helper.setSubject("test subject");
        helper.setFrom("test@sender.com");

        this.emailSender.send(mimeMessage);

        MimeMessage[] messages = testSmtp.getReceivedMessages();
        assertEquals(1, messages.length);
        assertEquals("test subject", messages[0].getSubject());
        String body = GreenMailUtil.getBody(messages[0]).replaceAll("=\r?\n", "");
        assertEquals(emailTemplate, body);
    }
}