package com.niketon.aroggo.app.payment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_audit")
@Getter
@Setter
@NoArgsConstructor
public class PaymentAudit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "payment_id", nullable = false)
	private Long paymentId;

	@Column(name = "old_amount", nullable = false, precision = 12, scale = 2)
	private BigDecimal oldAmount;

	@Column(name = "new_amount", nullable = false, precision = 12, scale = 2)
	private BigDecimal newAmount;

	@Column(nullable = false, length = 500)
	private String reason;

	@Column(name = "changed_by", length = 100)
	private String changedBy;

	@Column(name = "changed_at", nullable = false)
	private LocalDateTime changedAt;

	@PrePersist
	public void prePersist() {
		if (changedAt == null) {
			changedAt = LocalDateTime.now();
		}
	}
}

