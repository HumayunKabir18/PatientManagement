
package com.niketon.aroggo.app.payment.repository;

import com.niketon.aroggo.app.payment.entity.PatientCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


import java.math.BigDecimal;

public interface PatientChargeRepository
		extends JpaRepository<PatientCharge, Long> {

	@Query("""
			SELECT COALESCE(SUM(c.amount), 0)
			FROM PatientCharge c
			WHERE c.patientCode = :patientCode
			""")
	BigDecimal sumChargesByPatientCode(
			@Param("patientCode") String patientCode
	);

	Optional<PatientCharge> findByPatientCodeAndAppointmentCode(
			String patientCode,
			String appointmentCode
	);
}