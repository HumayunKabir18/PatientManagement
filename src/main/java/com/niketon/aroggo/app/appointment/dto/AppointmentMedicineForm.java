package com.niketon.aroggo.app.appointment.dto;

import lombok.Data;

@Data
public class AppointmentMedicineForm {

	private String medicineName;

	private String originalName;

	private String medicineType;

	private String doses;

	private String frequency ;

	private String meal ;

	private boolean morning;

	private boolean noon;

	private boolean night;
}