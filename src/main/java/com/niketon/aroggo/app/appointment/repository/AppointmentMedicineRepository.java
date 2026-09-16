package com.niketon.aroggo.app.appointment.repository;

import com.niketon.aroggo.app.appointment.entity.AppointmentId;
import com.niketon.aroggo.app.appointment.entity.AppointmentMedicine;
import com.niketon.aroggo.app.appointment.entity.AppointmentMedicineId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentMedicineRepository
		extends JpaRepository<AppointmentMedicine, AppointmentMedicineId> {

	List<AppointmentMedicine> findByAppointment_IdOrderById_SerialNoAsc(
			AppointmentId appointmentId
	);
}
