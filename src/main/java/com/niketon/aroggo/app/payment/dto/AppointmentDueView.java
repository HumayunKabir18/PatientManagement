package com.niketon.aroggo.app.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class AppointmentDueView {

	private final String appointmentCode;
	private final LocalDate appointmentDate;
	private final BigDecimal appointmentFee;
	private final BigDecimal amountCollected;
	private final BigDecimal appointmentDue;
}