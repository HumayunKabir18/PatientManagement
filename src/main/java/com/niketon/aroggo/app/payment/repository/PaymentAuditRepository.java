package com.niketon.aroggo.app.payment.repository;

import com.niketon.aroggo.app.payment.entity.PaymentAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentAuditRepository
		extends JpaRepository<PaymentAudit, Long> {

	List<PaymentAudit> findByPaymentIdOrderByChangedAtDesc(Long paymentId);
}
