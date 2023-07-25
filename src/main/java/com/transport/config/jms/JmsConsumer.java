package com.transport.config.jms;

import com.transport.api.dto.jms.TransporterReportDto;
import com.transport.model.Email;
import com.transport.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JmsConsumer {
    private final EmailService emailService;
    private final SpringTemplateEngine templateEngine;

    @JmsListener(destination = "transportation-queue", containerFactory = "jmsListenerContainerFactory")
    public void formEmailForReport(TransporterReportDto transporterReportDto) {
        Email email = new Email();
        email.setSender(transporterReportDto.getUserEmail());
        email.setRecipients(Collections.singletonList(transporterReportDto.getUserEmail()));
        email.setSubject("Transport.com");
        String html = templateEngine.process("report.html", setContext(transporterReportDto));
        email.setMsgBody(html);
        emailService.sendTemplateMail(email);
    }

    private Context setContext(TransporterReportDto transporterReportDto) {
        Context context = new Context();
        context.setVariable("startDate", transporterReportDto.getStartDate());
        context.setVariable("endDate", transporterReportDto.getEndDate());
        context.setVariable("amountOfTransportations", transporterReportDto.getAmountOfTransportations());
        context.setVariable("distance", transporterReportDto.getDistance());
        context.setVariable("fuelConsumption", transporterReportDto.getFuelConsumption());
        context.setVariable("fuelCost", transporterReportDto.getFuelCost());
        context.setVariable("income", transporterReportDto.getIncome());
        return context;
    }
}
