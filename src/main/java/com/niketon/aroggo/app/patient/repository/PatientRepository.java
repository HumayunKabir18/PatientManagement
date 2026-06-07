package com.niketon.aroggo.app.patient.repository;

import com.niketon.aroggo.app.patient.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

	List<Patient> findByFirstNameContainingIgnoreCase(String firstName);

	List<Patient> findByMobileContaining(String mobile);

	@Query("""
    SELECT p FROM Patient p
    WHERE (:name IS NULL OR LOWER(p.firstName) LIKE CONCAT('%', : name, '%') OR :name IS NULL OR LOWER(p.lastName) LIKE CONCAT('%', : name, '%') )
        AND(:mobile IS NULL OR LOWER(p.mobile) LIKE CONCAT('%', : mobile, '%'))""")
	Page<Patient> searchPatients(
			@Param("name") String name,
			@Param("mobile") String mobile,
			@Param("bloodGroup") String bloodGroup,
			@Param("dob") LocalDate dob,
			Pageable pageable
	);
}