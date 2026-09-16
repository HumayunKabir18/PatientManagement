package com.niketon.aroggo.app.appointment.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "appointment_medicine")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentMedicine {

	@EmbeddedId
	private AppointmentMedicineId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(
					name = "appointment_code",
					referencedColumnName = "appointment_code",
					insertable = false,
					updatable = false
			),
			@JoinColumn(
					name = "patient_code",
					referencedColumnName = "patient_code",
					insertable = false,
					updatable = false
			)
	})
	private Appointment appointment;

	@Column(name = "medicine_name", nullable = false, length = 255)
	private String medicineName;

	@Column(name = "medicine_type", length = 50)
	private String medicineType;

	@Column(name = "doses", length = 100)
	private String doses;

	@Column(name = "morning", nullable = false)
	private boolean morning;

	@Column(name = "noon", nullable = false)
	private boolean noon;

	@Column(name = "night", nullable = false)
	private boolean night;
}