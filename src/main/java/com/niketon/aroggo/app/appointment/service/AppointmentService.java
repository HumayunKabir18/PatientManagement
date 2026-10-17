
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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {


	private final AppointmentRepository appointmentRepository;
	private final AppointmentMedicineRepository medicineRepository;

	/**
	 * Creates a new appointment and saves its medicines.
	 */
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

		Appointment appointment = Appointment.builder()
				.id(appointmentId)
				.patient(patient)
				.appointmentDate(form.getAppointmentDate())
				.appointmentTime(form.getAppointmentTime())
				.appointmentType(form.getAppointmentType())
				.chiefComplaint(form.getChiefComplaint())
				.symptoms(form.getSymptoms())
				.diagnosis(form.getDiagnosis())
				.doctorAdvice(form.getDoctorAdvice())
				.followUpDate(form.getFollowUpDate())
				.notes(form.getNotes())
				.height(form.getHeight())
				.weight(form.getWeight())
				.bmi(form.getBmi())
				.bloodPressure(form.getBloodPressure())
				.diabetes(form.getDiabetes())
				.pulseRate(form.getPulseRate())
				.createdAt(LocalDateTime.now())
				.build();

		Appointment savedAppointment =
				appointmentRepository.save(appointment);

		saveMedicines(savedAppointment, form);

		return savedAppointment;
	}

	/**
	 * Checks whether an appointment is within its 3-hour editing window.
	 *
	 * The deadline is calculated from appointmentDate + appointmentTime,
	 * not createdAt.
	 */
	@Transactional(readOnly = true)
	public boolean canEditAppointment(AppointmentId appointmentId) {
		Appointment appointment = getAppointment(appointmentId);

		return canEditAppointment(appointment);
	}

	/**
	 * Checks the editing deadline for an already-loaded appointment.
	 */
	private boolean canEditAppointment(Appointment appointment) {

		if (appointment.getAppointmentDate() == null
				|| appointment.getAppointmentTime() == null) {
			return false;
		}

		LocalDateTime appointmentDateTime = LocalDateTime.of(
				appointment.getAppointmentDate(),
				appointment.getAppointmentTime()
		);

		LocalDateTime editingDeadline =
				appointmentDateTime.plusHours(3);

		// The appointment cannot be edited after the deadline.
		// A future appointment is editable until 3 hours after its time.
		return LocalDateTime.now().isBefore(editingDeadline);
	}

	/**
	 * Updates an existing appointment and replaces its medicines.
	 *
	 * The original createdAt value is preserved.
	 */
	@Transactional
	public Appointment updateAppointment(
			Patient patient,
			String appointmentCode,
			AppointmentForm form
	) {
		AppointmentId appointmentId = new AppointmentId(
				appointmentCode,
				patient.getPatientCode()
		);

		Appointment appointment = appointmentRepository
				.findById(appointmentId)
				.orElseThrow(() ->
						new RuntimeException("Appointment Not Found"));

		// Enforce the 3-hour editing window on the server.
		if (!canEditAppointment(appointment)) {
			throw new IllegalStateException(
					"The 3-hour editing window for this appointment has expired."
			);
		}

		// Update appointment details.
		appointment.setAppointmentDate(form.getAppointmentDate());
		appointment.setAppointmentTime(form.getAppointmentTime());
		appointment.setAppointmentType(form.getAppointmentType());
		appointment.setChiefComplaint(form.getChiefComplaint());
		appointment.setSymptoms(form.getSymptoms());
		appointment.setDiagnosis(form.getDiagnosis());
		appointment.setDoctorAdvice(form.getDoctorAdvice());
		appointment.setFollowUpDate(form.getFollowUpDate());
		appointment.setNotes(form.getNotes());
		appointment.setHeight(form.getHeight());
		appointment.setWeight(form.getWeight());
		appointment.setBmi(form.getBmi());
		appointment.setBloodPressure(form.getBloodPressure());
		appointment.setDiabetes(form.getDiabetes());
		appointment.setPulseRate(form.getPulseRate());

		// Do not modify createdAt.
		// The original record creation time must remain unchanged.

		Appointment updatedAppointment =
				appointmentRepository.save(appointment);

		// Remove the old medicine records.
		List<AppointmentMedicine> existingMedicines =
				medicineRepository
						.findByAppointment_IdOrderById_SerialNoAsc(
								appointmentId
						);

		medicineRepository.deleteAll(existingMedicines);

		// Save the updated medicine list.
		saveMedicines(updatedAppointment, form);

		return updatedAppointment;
	}

	/**
	 * Saves the medicines belonging to an appointment.
	 * Empty medicine names are ignored.
	 */
	private void saveMedicines(
			Appointment appointment,
			AppointmentForm form
	) {
		if (form.getMedicines() == null) {
			return;
		}

		int serialNo = 1;

		for (AppointmentMedicineForm medicineForm : form.getMedicines()) {

			if (medicineForm.getMedicineName() == null
					|| medicineForm.getMedicineName().isBlank()) {
				continue;
			}

			AppointmentMedicineId medicineId =
					new AppointmentMedicineId(
							appointment.getId().getAppointmentCode(),
							appointment.getId().getPatientCode(),
							serialNo
					);

			AppointmentMedicine medicine = AppointmentMedicine.builder()
					.id(medicineId)
					.appointment(appointment)
					.medicineName(medicineForm.getMedicineName())
					.originalName(medicineForm.getOriginalName())
					.medicineType(medicineForm.getMedicineType())
					.doses(medicineForm.getDoses())
					.frequency(medicineForm.getFrequency())
					.meal(medicineForm.getMeal())
					.morning(medicineForm.isMorning())
					.noon(medicineForm.isNoon())
					.night(medicineForm.isNight())
					.build();

			medicineRepository.save(medicine);

			serialNo++;
		}
	}

	/**
	 * Returns all appointments for a patient, newest first.
	 */
	public List<Appointment> getPatientAppointments(String patientCode) {
		return appointmentRepository
				.findByPatientPatientCodeOrderByAppointmentDateDescAppointmentTimeDesc(
						patientCode
				);
	}

	/**
	 * Retrieves an appointment by its composite ID.
	 */
	@Transactional(readOnly = true)
	public Appointment getAppointment(AppointmentId appointmentId) {
		return appointmentRepository.findById(appointmentId)
				.orElseThrow(() ->
						new RuntimeException("Appointment Not Found"));
	}

	/**
	 * Retrieves medicines for an appointment, ordered by serial number.
	 */
	@Transactional(readOnly = true)
	public List<AppointmentMedicine> getMedicines(
			AppointmentId appointmentId
	) {
		return medicineRepository
				.findByAppointment_IdOrderById_SerialNoAsc(appointmentId);
	}

	/**
	 * Generates a unique appointment code.
	 */
	private String generateAppointmentCode() {
		return "APT" + LocalDateTime.now()
				.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
	}


}
