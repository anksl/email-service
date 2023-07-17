package com.transport.controller;

import com.transport.model.Email;
import com.transport.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/emails")
@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public void sendMail(@RequestBody Email details) {
        emailService.sendSimpleMail(details);
    }

    @PostMapping("/sendWithAttachment")
    public void sendMailWithAttachment(@RequestBody Email details) {
        emailService.sendMailWithAttachment(details);
    }
}

