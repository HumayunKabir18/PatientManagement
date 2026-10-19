package com.niketon.aroggo.app.prescriber.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class PrescriberForm {

	// Patient information
	private String patientName;
	private String age;
	private String bloodGroup;

	// Appointment information
	private LocalDate appointmentDate;
	private LocalTime appointmentTime;
	private String appointmentType;

	// Consultation
	private String chiefComplaint;
	private String symptoms;

	// Vitals
	private String height;
	private String weight;
	private String bmi;
	private String bloodPressure;
	private String pulseRate;
	private String diabetes;

	// Diagnosis and advice
	private String diagnosis;
	private String doctorAdvice;
	private LocalDate followUpDate;
	private String notes;

	// Medicines
	private List<PrescriberMedicineForm> medicines = new ArrayList<>();
}