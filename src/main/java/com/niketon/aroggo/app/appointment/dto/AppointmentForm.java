package com.niketon.aroggo.app.appointment.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class AppointmentForm {

	private LocalDate appointmentDate;

	private LocalTime appointmentTime;

	private String appointmentType;

	private String chiefComplaint;

	private String symptoms;

	private String diagnosis;

	private String doctorAdvice;

	private LocalDate followUpDate;

	private String notes;

	private List<AppointmentMedicineForm> medicines = new ArrayList<>();
}