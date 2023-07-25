package com.transport.config.kafka;

import com.transport.api.dto.jms.DebtorsMessageDto;
import com.transport.model.Email;
import com.transport.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {
    private final EmailService emailService;

    @KafkaListener(id = "debtors", topics = {"server.payment"}, containerFactory = "singleFactory")
    public void formEmailForDebtors(DebtorsMessageDto debtorsMessageDto) {
        Email email = new Email();
        email.setRecipients(debtorsMessageDto.getRecipients());
        email.setSubject("Transport.com");
        email.setMsgBody("Reminder about the presence of unpaid transportations!");
        emailService.sendSimpleMail(email);
    }
}
