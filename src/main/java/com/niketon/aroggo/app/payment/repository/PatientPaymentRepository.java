package com.niketon.aroggo.app.payment.repository;

import com.niketon.aroggo.app.payment.entity.PatientPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PatientPaymentRepository
		extends JpaRepository<PatientPayment, Long> {

	@Query("""
        SELECT COALESCE(SUM(p.amount), 0)
        FROM PatientPayment p
        WHERE p.patientCode = :patientCode
        """)
	BigDecimal sumPaymentsByPatientCode(
			@Param("patientCode") String patientCode
	);

	Optional<PatientPayment> findByPatientCodeAndAppointmentCode(
			String patientCode,
			String appointmentCode
	);

	List<PatientPayment> findByPatientCodeOrderByReceivedAtDesc(
			String patientCode
	);
}
