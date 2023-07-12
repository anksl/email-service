package com.transport.config;

import com.transport.api.dto.jms.DebtorsMessageDto;
import com.transport.api.dto.jms.TransporterReportDto;
import com.transport.model.Email;
import com.transport.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class JmsConsumer {
    private final EmailService emailService;

    @JmsListener(destination = "payment-queue", containerFactory = "jmsListenerContainerFactory")
    public void formEmailForDebtors(DebtorsMessageDto debtorsMessageDto) {
        Email email = new Email();
        email.setRecipients(debtorsMessageDto.getRecipients());
        email.setSubject("Transport.com");
        email.setMsgBody("Reminder about the presence of unpaid transportations!");
        emailService.sendSimpleMail(email);
    }

    @JmsListener(destination = "transportation-queue", containerFactory = "jmsListenerContainerFactory")
    public void formEmailForReport(TransporterReportDto transporterReportDto) {
        Email email = new Email();
        email.setRecipients(Collections.singletonList(transporterReportDto.getUserEmail()));
        email.setSubject("Transport.com");
        email.setMsgBody(transporterReportDto.getReport());
        emailService.sendSimpleMail(email);
    }
}
