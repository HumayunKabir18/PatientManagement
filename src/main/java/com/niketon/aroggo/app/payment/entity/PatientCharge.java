
package com.niketon.aroggo.app.payment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
		name = "patient_charges",
		uniqueConstraints = {
				@UniqueConstraint(
						name = "uk_charge_appointment",
						columnNames = {"patient_code", "appointment_code"}
				)
		}
)
@Getter
@Setter
@NoArgsConstructor
public class PatientCharge {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "patient_code", nullable = false, length = 20)
	private String patientCode;

	@Column(name = "appointment_code", nullable = false, length = 30)
	private String appointmentCode;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal amount;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@PrePersist
	public void prePersist() {
		if (createdAt == null) {
			createdAt = LocalDateTime.now();
		}
	}
}
