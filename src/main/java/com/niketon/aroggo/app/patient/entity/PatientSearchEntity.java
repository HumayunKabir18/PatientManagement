package com.niketon.aroggo.app.patient.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientSearchEntity {

	private String patientId;
	private String patientName;
	private String mobileNo;
	private String bloodGroup;
	private LocalDate dateOfBirth;

	private int page;
	private int size;
}