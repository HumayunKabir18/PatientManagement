package com.niketon.aroggo.app.appointment.repository;

import com.niketon.aroggo.app.appointment.entity.Appointment;
import com.niketon.aroggo.app.appointment.entity.AppointmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository
		extends JpaRepository<Appointment, AppointmentId> {

	List<Appointment> findByPatientPatientCodeOrderByAppointmentDateDescAppointmentTimeDesc(
			String patientCode
	);
}