package com.smaugslair.thitracker.services;

import com.smaugslair.thitracker.data.user.PasswordReset;
import com.smaugslair.thitracker.data.user.PasswordResetRepository;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final Logger log = LoggerFactory.getLogger(PasswordResetService.class);

    private final PasswordResetRepository passwordResetRepository;

    private final JavaMailSender javaMailSender;

    private final ThiProperties thiProperties;

    @Autowired
    public PasswordResetService(PasswordResetRepository passwordResetRepository, JavaMailSender javaMailSender, ThiProperties thiProperties) {
        this.passwordResetRepository = passwordResetRepository;
        this.javaMailSender = javaMailSender;
        this.thiProperties = thiProperties;
    }


    public void createPasswordResetForUser(User user) {

        cleanExpiredResets();

        UUID uuid = UUID.randomUUID();
        log.info("uuid:"+uuid);
        PasswordReset passwordReset = new PasswordReset();
        passwordReset.setCreated(new Date());
        passwordReset.setUserId(user.getId());
        passwordReset.setHash(SecurityUtils.encode(uuid.toString()));

        log.info("hash:"+passwordReset.getHash());

        passwordResetRepository.save(passwordReset);

        sendMail(user, uuid.toString());

    }

    private void cleanExpiredResets() {
        List<PasswordReset> resets = passwordResetRepository.findAll();
        resets.forEach(passwordReset -> {
            if (passwordReset.isExpired()) {
                passwordResetRepository.delete(passwordReset);
            }
        });
    }

    private void sendMail(User user, String uuid) {


        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(user.getEmail());

        msg.setSubject("Password reset for The Hero Instant app");

        StringBuilder sb = new StringBuilder();
        sb.append("The Hero Instant application received a password reset request for this email address.\n");
        sb.append("Follow the link below to complete this reset. This request will expire in 5 minutes. \n\n");
        sb.append(thiProperties.getAppUrl()).append('/').append(thiProperties.getResetEndpoint());
        sb.append("?resetToken=").append(uuid);
        msg.setText(sb.toString());


        javaMailSender.send(msg);
    }

}
