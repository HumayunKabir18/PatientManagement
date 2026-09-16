package com.niketon.aroggo.app.appointment.dto;

import com.niketon.aroggo.app.patient.entity.Patient;
import lombok.Data;

@Data
public class PatientAppointmentForm {

	private Patient patient = new Patient();

	private AppointmentForm appointment = new AppointmentForm();
}