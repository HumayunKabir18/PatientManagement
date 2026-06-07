package com.niketon.aroggo.app.patient.repository;

import com.niketon.aroggo.app.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

	List<Patient> findByFirstNameContainingIgnoreCase(String firstName);

	List<Patient> findByMobileContaining(String mobile);
}