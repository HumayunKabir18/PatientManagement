package com.niketon.aroggo.app.appointment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AppointmentId implements Serializable {

	@Column(name = "appointment_code", length = 30)
	private String appointmentCode;

	@Column(name = "patient_code", length = 20)
	private String patientCode;
}
