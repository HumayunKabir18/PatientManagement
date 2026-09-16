package com.niketon.aroggo.app.appointment.entity;

import com.niketon.aroggo.app.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "appointment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

	@EmbeddedId
	private AppointmentId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "patient_code",
			referencedColumnName = "patient_code",
			nullable = false,
			insertable = false,
			updatable = false
	)
	private Patient patient;

	@Column(name = "appointment_date", nullable = false)
	private LocalDate appointmentDate;

	@Column(name = "appointment_time")
	private LocalTime appointmentTime;

	@Column(name = "appointment_type", length = 50)
	private String appointmentType;

	@Column(name = "chief_complaint", length = 4000)
	private String chiefComplaint;

	@Column(name = "symptoms", length = 4000)
	private String symptoms;

	@Column(name = "diagnosis", length = 4000)
	private String diagnosis;

	@Column(name = "doctor_advice", length = 5000)
	private String doctorAdvice;

	@Column(name = "follow_up_date")
	private LocalDate followUpDate;

	@Column(name = "notes", length = 5000)
	private String notes;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
}