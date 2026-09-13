package com.niketon.aroggo.app.patient.repository;

import com.niketon.aroggo.app.patient.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

	List<Patient> findByFirstNameContainingIgnoreCase(String firstName);

	List<Patient> findByMobileContaining(String mobile);

	Optional<Patient> findByPatientCode(String patientCode);

	@Query("SELECT MAX(p.patientCode) FROM Patient p")
	String findLastPatientCode();

	@Query("""
    SELECT p
    FROM Patient p
    WHERE
        (
            :name IS NULL
            OR :name = ''
            OR LOWER(p.firstName) LIKE LOWER(CONCAT('%', :name, '%'))
            OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :name, '%'))
        )
        AND (
            :mobile IS NULL
            OR :mobile = ''
            OR LOWER(p.mobile) LIKE LOWER(CONCAT('%', :mobile, '%'))
        )
        AND (
            :patientId IS NULL
            OR :patientId = ''
            OR LOWER(p.patientCode) LIKE LOWER(CONCAT('%', :patientId, '%'))
        )
        AND (
            :bloodGroup IS NULL
            OR :bloodGroup = ''
            OR p.bloodGroup = :bloodGroup
        )
        AND (
            :dob IS NULL
            OR p.dateOfBirth = :dob
        )
        ORDER BY p.patientCode ASC
""")
	Page<Patient> searchPatients(
			@Param("patientId") String patientId,
			@Param("name") String name,
			@Param("mobile") String mobile,
			@Param("bloodGroup") String bloodGroup,
			@Param("dob") LocalDate dob,
			Pageable pageable
	);
}