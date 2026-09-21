package com.niketon.aroggo.app.appointment.service;

import com.niketon.aroggo.app.appointment.dto.AppointmentForm;
import com.niketon.aroggo.app.appointment.dto.AppointmentMedicineForm;
import com.niketon.aroggo.app.appointment.entity.Appointment;
import com.niketon.aroggo.app.appointment.entity.AppointmentId;
import com.niketon.aroggo.app.appointment.entity.AppointmentMedicine;
import com.niketon.aroggo.app.appointment.entity.AppointmentMedicineId;
import com.niketon.aroggo.app.appointment.repository.AppointmentMedicineRepository;
import com.niketon.aroggo.app.appointment.repository.AppointmentRepository;
import com.niketon.aroggo.app.patient.entity.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {


	private final AppointmentRepository appointmentRepository;
	private final AppointmentMedicineRepository medicineRepository;


	@Transactional
	public Appointment saveAppointment(
			Patient patient,
			AppointmentForm form
	) {

		String appointmentCode =
				generateAppointmentCode();

		AppointmentId appointmentId =
				new AppointmentId(
						appointmentCode,
						patient.getPatientCode()
				);

		Appointment appointment =
				Appointment.builder()
						.id(appointmentId)
						.patient(patient)
						.appointmentDate(
								form.getAppointmentDate()
						)
						.appointmentTime(
								form.getAppointmentTime()
						)
						.appointmentType(
								form.getAppointmentType()
						)
						.chiefComplaint(
								form.getChiefComplaint()
						)
						.symptoms(
								form.getSymptoms()
						)
						.diagnosis(
								form.getDiagnosis()
						)
						.doctorAdvice(
								form.getDoctorAdvice()
						)
						.followUpDate(
								form.getFollowUpDate()
						)
						.notes(
								form.getNotes()
						)
						.createdAt(
								LocalDateTime.now()
						)
						.build();

		Appointment savedAppointment =
				appointmentRepository.save(
						appointment
				);

		saveMedicines(
				savedAppointment,
				form
		);

		return savedAppointment;
	}


	private void saveMedicines(
			Appointment appointment,
			AppointmentForm form
	) {

		if (form.getMedicines() == null) {
			return;
		}

		int serialNo = 1;

		for (AppointmentMedicineForm medicineForm :
				form.getMedicines()) {

			if (medicineForm.getMedicineName() == null
					|| medicineForm.getMedicineName().isBlank()) {

				continue;
			}

			AppointmentMedicineId medicineId =
					new AppointmentMedicineId(
							appointment.getId()
									.getAppointmentCode(),

							appointment.getId()
									.getPatientCode(),

							serialNo
					);

			AppointmentMedicine medicine =
					AppointmentMedicine.builder()
							.id(medicineId)
							.appointment(appointment)
							.medicineName(
									medicineForm.getMedicineName()
							)
							.medicineType(
									medicineForm.getMedicineType()
							)
							.doses(
									medicineForm.getDoses()
							)
							.morning(
									medicineForm.isMorning()
							)
							.noon(
									medicineForm.isNoon()
							)
							.night(
									medicineForm.isNight()
							)
							.build();

			medicineRepository.save(medicine);

			serialNo++;
		}
	}


	public List<Appointment> getPatientAppointments(
			String patientCode
	) {

		return appointmentRepository
				.findByPatientPatientCodeOrderByAppointmentDateDescAppointmentTimeDesc(
						patientCode
				);
	}


	/*
	 * Get one appointment together with all of
	 * its medicines while the transaction is open.
	 */
	@Transactional(readOnly = true)
	public Appointment getAppointment(
			AppointmentId appointmentId
	) {

		return appointmentRepository
				.findById(appointmentId)
				.orElseThrow(() ->
						new RuntimeException(
								"Appointment Not Found"
						)
				);
	}


	/*
	 * Important:
	 * Medicines are loaded inside a transaction.
	 */
	@Transactional(readOnly = true)
	public List<AppointmentMedicine> getMedicines(
			AppointmentId appointmentId
	) {

		return medicineRepository
				.findByAppointment_IdOrderById_SerialNoAsc(
						appointmentId
				);
	}


	private String generateAppointmentCode() {

		return "APT" +
				LocalDateTime.now()
						.format(
								DateTimeFormatter.ofPattern(
										"yyyyMMddHHmmssSSS"
								)
						);
	}


}
