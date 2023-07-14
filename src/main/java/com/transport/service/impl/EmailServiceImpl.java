package com.transport.service.impl;

import com.transport.api.exception.EmailSendingException;
import com.transport.model.Email;
import com.transport.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;

    public void sendSimpleMail(Email details) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(details.getSender());
        mailMessage.setTo(details.getRecipients().toArray(String[]::new));
        mailMessage.setText(details.getMsgBody());
        mailMessage.setSubject(details.getSubject());
        javaMailSender.send(mailMessage);
    }

    public void sendMailWithAttachment(Email details) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(details.getSender());
            mimeMessageHelper.setTo(details.getRecipients().toArray(String[]::new));
            mimeMessageHelper.setText(details.getMsgBody());
            mimeMessageHelper.setSubject(details.getSubject());
            FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));
            mimeMessageHelper.addAttachment(Objects.requireNonNull(file.getFilename()), file);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Error while sending mail!");
            throw new EmailSendingException("Error while sending mail!");
        }
    }

    public void sendTemplateMail(Email details) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(details.getSender());
            mimeMessageHelper.setTo(details.getRecipients().toArray(String[]::new));
            mimeMessageHelper.setText(details.getMsgBody(), true);
            mimeMessageHelper.setSubject(details.getSubject());
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Error while sending mail!");
            throw new EmailSendingException("Error while sending mail!");
        }
    }
}
