package com.transport.service;

import com.transport.model.Email;

public interface EmailService {
    void sendSimpleMail(Email details);

    void sendMailWithAttachment(Email details);

    void sendTemplateMail(Email details);
}
