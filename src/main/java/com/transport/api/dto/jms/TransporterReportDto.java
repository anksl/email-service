package com.transport.api.dto.jms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TransporterReportDto implements Serializable {
    private String userEmail;
    private Date startDate;
    private Date endDate;
    private Integer amountOfTransportations;
    private Short distance;
    private Short fuelConsumption;
    private Short fuelCost;
    private Short income;
}
