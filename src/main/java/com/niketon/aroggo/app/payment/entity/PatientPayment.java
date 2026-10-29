
package com.niketon.aroggo.app.payment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
		name = "patient_payments",
		uniqueConstraints = {
				@UniqueConstraint(
						name = "uk_payment_code",
						columnNames = "payment_code"
				),
				@UniqueConstraint(
						name = "uk_payment_appointment",
						columnNames = "appointment_code"
				)
		},
		indexes = {
				@Index(name = "idx_payment_patient", columnList = "patient_code")
		}
)
@Getter
@Setter
@NoArgsConstructor
public class PatientPayment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "payment_code", nullable = false, unique = true, length = 40)
	private String paymentCode;

	@Column(name = "patient_code", nullable = false, length = 20)
	private String patientCode;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal amount;

	@Column(name = "payment_method", length = 30)
	private String paymentMethod;

	@Column(name = "received_at", nullable = false)
	private LocalDateTime receivedAt;

	@Column(name = "received_by", length = 100)
	private String receivedBy;

	@Column(length = 500)
	private String note;

	@Column(name = "appointment_code", length = 30)
	private String appointmentCode;

	@PrePersist
	public void prePersist() {
		if (receivedAt == null) {
			receivedAt = LocalDateTime.now();
		}
	}
}
