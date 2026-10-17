package com.niketon.aroggo.app.patient.controller;

import com.niketon.aroggo.app.appointment.dto.AppointmentForm;
import com.niketon.aroggo.app.appointment.dto.AppointmentMedicineForm;
import com.niketon.aroggo.app.appointment.dto.PatientAppointmentForm;
import com.niketon.aroggo.app.appointment.entity.Appointment;
import com.niketon.aroggo.app.appointment.entity.AppointmentId;
import com.niketon.aroggo.app.appointment.service.AppointmentService;
import com.niketon.aroggo.app.patient.entity.Patient;
import com.niketon.aroggo.app.patient.entity.PatientSearchEntity;
import com.niketon.aroggo.app.patient.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Comparator;

@Controller
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {


    private final PatientService patientService;
    private final AppointmentService appointmentService;


    @GetMapping("/list")
    public String listPage(Model model) {

        model.addAttribute(
                "patient",
                new Patient()
        );

		return "patient/patientList";
	}


    @GetMapping("/register")
    public String registrationPage(Model model) {

        PatientAppointmentForm form =
                new PatientAppointmentForm();

        form.getAppointment()
                .setAppointmentDate(
                        LocalDate.now()
                );

        model.addAttribute(
                "form",
                form
        );

        /*
         * New appointment is editable.
         */
        model.addAttribute(
                "editable",
                true
        );

        return "patient/patientRegistration";
    }


    @PostMapping("/save")
    public String savePatient(
            @ModelAttribute("form")
            PatientAppointmentForm form
    ) {

        Patient savedPatient =
                patientService.save(
                        form.getPatient()
                );

        return "redirect:/patients/details/"
                + savedPatient.getPatientCode();
    }


    @PostMapping("/update")
    public String updatePatient(
            @ModelAttribute("form")
            PatientAppointmentForm form
    ) {

        patientService.update(form.getPatient());

        return "redirect:/patients/details/"
                + form.getPatient().getPatientCode();
    }

    @GetMapping("/delete/{patientCode}")
    public String deletePatient(
            @PathVariable String patientCode) {

        patientService.deletePatientByCode(patientCode);

        return "redirect:/patients/list";
    }


    @GetMapping("/details/{patientCode}")
    public String patientDetails(
            @PathVariable String patientCode,
            Model model
    ) {

        Patient patient =
                patientService.getPatientByCode(
                        patientCode
                );

        List<Appointment> appointments =
                appointmentService.getPatientAppointments(
                        patientCode
                );

        model.addAttribute(
                "patient",
                patient
        );

        model.addAttribute(
                "appointments",
                appointments
        );

        return "patient/patientDetails";
    }

    @GetMapping("/update/details/{patientCode}")
    public String patientUpdateDetails(
            @PathVariable String patientCode,
            Model model
    ) {

        Patient patient =
                patientService.getPatientByCode(
                        patientCode
                );

        PatientAppointmentForm form =
                new PatientAppointmentForm();

        form.setPatient(patient);

        model.addAttribute(
                "form",
                form
        );

        return "patient/patientRegistration";
    }


    /*
     * ============================================================
     * NEW APPOINTMENT
     * ============================================================
     */

//    @GetMapping("/appointment/new/{patientCode}")
//    public String newAppointment(
//            @PathVariable String patientCode,
//            Model model
//    ) {
//
//        Patient patient =
//                patientService.getPatientByCode(
//                        patientCode
//                );
//
//        PatientAppointmentForm form =
//                new PatientAppointmentForm();
//
//        form.setPatient(patient);
//
//        form.getAppointment()
//                .setAppointmentDate(
//                        LocalDate.now()
//                );
//
//        model.addAttribute(
//                "form",
//                form
//        );
//
//        /*
//         * New appointment = editable.
//         */
//        model.addAttribute(
//                "editable",
//                true
//        );
//
//        return "patient/appointmentForm";
//    }


    @GetMapping("/appointment/new/{patientCode}")
    public String newAppointment(
            @PathVariable String patientCode,
            Model model
    ) {

        Patient patient = patientService.getPatientByCode(patientCode);

        PatientAppointmentForm form = new PatientAppointmentForm();
        form.setPatient(patient);
        AppointmentForm appointmentForm = new AppointmentForm();

        // Find the patient's most recent appointment.
        Appointment previousAppointment = appointmentService
                .getPatientAppointments(patientCode)
                .stream()
                .max(
                        Comparator
                                .comparing(
                                        Appointment::getAppointmentDate,
                                        Comparator.nullsFirst(Comparator.naturalOrder())
                                )
                                .thenComparing(
                                        Appointment::getAppointmentTime,
                                        Comparator.nullsFirst(Comparator.naturalOrder())
                                )
                                .thenComparing(
                                        Appointment::getCreatedAt,
                                        Comparator.nullsFirst(Comparator.naturalOrder())
                                )
                )
                .orElse(null);

        // If this patient has a previous appointment, copy its data.
        if (previousAppointment != null) {

            appointmentForm.setAppointmentType(
                    previousAppointment.getAppointmentType()
            );

            appointmentForm.setChiefComplaint(
                    previousAppointment.getChiefComplaint()
            );

            appointmentForm.setSymptoms(
                    previousAppointment.getSymptoms()
            );

            appointmentForm.setDiagnosis(
                    previousAppointment.getDiagnosis()
            );

            appointmentForm.setDoctorAdvice(
                    previousAppointment.getDoctorAdvice()
            );

            appointmentForm.setFollowUpDate(
                    previousAppointment.getFollowUpDate()
            );

            appointmentForm.setNotes(
                    previousAppointment.getNotes()
            );

            appointmentForm.setHeight(previousAppointment.getHeight());
            appointmentForm.setWeight(previousAppointment.getWeight());
            appointmentForm.setBmi(previousAppointment.getBmi());

            appointmentForm.setBloodPressure(
                    previousAppointment.getBloodPressure()
            );

            appointmentForm.setPulseRate(
                    previousAppointment.getPulseRate()
            );

            appointmentForm.setDiabetes(
                    previousAppointment.getDiabetes()
            );

            // Copy all medicines from the previous appointment.
            List<AppointmentMedicineForm> medicines =
                    appointmentService
                            .getMedicines(previousAppointment.getId())
                            .stream()
                            .map(medicine -> {
                                AppointmentMedicineForm medicineForm =
                                        new AppointmentMedicineForm();

                                medicineForm.setMedicineName(
                                        medicine.getMedicineName()
                                );

                                medicineForm.setOriginalName(
                                        medicine.getOriginalName()
                                );

                                medicineForm.setMedicineType(
                                        medicine.getMedicineType()
                                );

                                medicineForm.setDoses(
                                        medicine.getDoses()
                                );

                                medicineForm.setFrequency(
                                        medicine.getFrequency()
                                );

                                medicineForm.setMeal(
                                        medicine.getMeal()
                                );

                                medicineForm.setMorning(
                                        medicine.isMorning()
                                );

                                medicineForm.setNoon(
                                        medicine.isNoon()
                                );

                                medicineForm.setNight(
                                        medicine.isNight()
                                );

                                return medicineForm;
                            })
                            .toList();

            appointmentForm.setMedicines(medicines);
        }

        // Always use today's date for the new appointment,
        // even when previous appointment data was copied.
        appointmentForm.setAppointmentDate(LocalDate.now());
        appointmentForm.setAppointmentTime(
                LocalTime.now().withSecond(0).withNano(0)
        );

        form.setAppointment(appointmentForm);

        model.addAttribute("form", form);
        model.addAttribute("editable", true);
        model.addAttribute("editing", false);
        return "patient/appointmentForm";
    }


    /*
     * ============================================================
     * EXISTING APPOINTMENT
     * ============================================================
     *
     * Appointment history row -> this URL
     *
     * Existing appointment is READ ONLY.
     *
     */

    @GetMapping(
            "/appointment/edit/{patientCode}/{appointmentCode}"
    )
    public String editAppointment(
            @PathVariable String patientCode,
            @PathVariable String appointmentCode,
            Model model
    ) {

        Patient patient =
                patientService.getPatientByCode(
                        patientCode
                );


        AppointmentId appointmentId =
                new AppointmentId(
                        appointmentCode,
                        patientCode
                );


        Appointment appointment =
                appointmentService.getAppointment(
                        appointmentId
                );


        PatientAppointmentForm form =
                new PatientAppointmentForm();

        form.setPatient(patient);


        AppointmentForm appointmentForm =
                new AppointmentForm();


        /*
         * Appointment fields
         */

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        appointmentForm.setAppointmentDate(
                appointment.getAppointmentDate()
        );

        appointmentForm.setAppointmentTime(
                appointment.getAppointmentTime()
        );

        appointmentForm.setAppointmentType(
                appointment.getAppointmentType()
        );

        appointmentForm.setChiefComplaint(
                appointment.getChiefComplaint()
        );

        appointmentForm.setSymptoms(
                appointment.getSymptoms()
        );

        appointmentForm.setDiagnosis(
                appointment.getDiagnosis()
        );

        appointmentForm.setDoctorAdvice(
                appointment.getDoctorAdvice()
        );

        appointmentForm.setFollowUpDate(
                appointment.getFollowUpDate()
        );

        appointmentForm.setNotes(
                appointment.getNotes()
        );
        appointmentForm.setHeight(appointment.getHeight());
        appointmentForm.setWeight(appointment.getWeight());
        appointmentForm.setBmi(appointment.getBmi());
        appointmentForm.setBloodPressure(appointment.getBloodPressure());
        appointmentForm.setPulseRate(appointment.getPulseRate());
        appointmentForm.setDiabetes(appointment.getDiabetes());

        /*
         * Medicines
         */

        List<AppointmentMedicineForm> medicines =
                appointmentService
                        .getMedicines(appointmentId)
                        .stream()
                        .map(medicine -> {

                            AppointmentMedicineForm medicineForm =
                                    new AppointmentMedicineForm();

                            medicineForm.setMedicineName(
                                    medicine.getMedicineName()
                            );

                            medicineForm.setOriginalName(
                                    medicine.getOriginalName()
                            );

                            medicineForm.setMedicineType(
                                    medicine.getMedicineType()
                            );

                            medicineForm.setDoses(
                                    medicine.getDoses()
                            );


							medicineForm.setFrequency(
									medicine.getFrequency()
							);


							medicineForm.setMeal(
									medicine.getMeal()
							);


							medicineForm.setMorning(
									medicine.isMorning()
							);

                            medicineForm.setNoon(
                                    medicine.isNoon()
                            );

                            medicineForm.setNight(
                                    medicine.isNight()
                            );

                            return medicineForm;

                        })
                        .toList();


        appointmentForm.setMedicines(
                medicines
        );


        form.setAppointment(
                appointmentForm
        );


        model.addAttribute(
                "form",
                form
        );


        /*
         * Existing appointment = READ ONLY.
         */
        boolean editable =
                appointmentService.canEditAppointment(appointmentId);

        model.addAttribute("editable", editable);
        model.addAttribute("editing", true);
        model.addAttribute("appointmentCode", appointmentCode);


        return "patient/appointmentForm";
    }

	/*
	 * ============================================================
	 * SAVE APPOINTMENT
	 * ============================================================
	 */

	@PostMapping("/appointment/save")
	public String saveAppointment(
			@ModelAttribute("form")
			PatientAppointmentForm form
	) {

        Patient patient =
                patientService.getPatientByCode(
                        form.getPatient()
                                .getPatientCode()
                );

        Appointment savedAppointment =
                appointmentService.saveAppointment(
                        patient,
                        form.getAppointment()
                );

        return "redirect:/patients/appointment/print/"
                + patient.getPatientCode()
                + "/"
                + savedAppointment.getId().getAppointmentCode();
    }

    @PostMapping("/appointment/update/{patientCode}/{appointmentCode}")
    public String updateAppointment(
            @PathVariable String patientCode,
            @PathVariable String appointmentCode,
            @ModelAttribute("form") PatientAppointmentForm form
    ) {
        Patient patient = patientService.getPatientByCode(patientCode);

        appointmentService.updateAppointment(
                patient,
                appointmentCode,
                form.getAppointment()
        );

        return "redirect:/patients/appointment/print/"
                + patientCode + "/" + appointmentCode;
    }

    /*
     * ============================================================
     * PRINT APPOINTMENT PRESCRIPTION
     * ============================================================
     */

    @GetMapping("/appointment/print/{patientCode}/{appointmentCode}")
    public String printAppointment(
            @PathVariable String patientCode,
            @PathVariable String appointmentCode,
            Model model
    ) {

        Patient patient =
                patientService.getPatientByCode(
                        patientCode
                );


        AppointmentId appointmentId =
                new AppointmentId(
                        appointmentCode,
                        patientCode
                );


        Appointment appointment =
                appointmentService.getAppointment(
                        appointmentId
                );


        List<com.niketon.aroggo.app.appointment.entity.AppointmentMedicine>
                medicines =
                appointmentService.getMedicines(
                        appointmentId
                );


        model.addAttribute(
                "patient",
                patient
        );

        model.addAttribute(
                "appointment",
                appointment
        );

        model.addAttribute(
                "medicines",
                medicines
        );


        return "patient/appointmentPrint";
    }
	/*
	 * ============================================================
	 * PATIENT SEARCH
	 * ============================================================
	 */

	@PostMapping("/search")
	@ResponseBody
	public Page<Patient> search(
			@RequestBody PatientSearchEntity req
	) {

        String name =
                (req.getPatientName() == null
                        || req.getPatientName().isBlank())
                        ? ""
                        : req.getPatientName();

        String patientId =
                (req.getPatientId() == null
                        || req.getPatientId().isBlank())
                        ? ""
                        : req.getPatientId();

        String mobile =
                (req.getMobileNo() == null
                        || req.getMobileNo().isBlank())
                        ? ""
                        : req.getMobileNo();

        String bloodGroup =
                (req.getBloodGroup() == null
                        || req.getBloodGroup().isBlank())
                        ? ""
                        : req.getBloodGroup();

        return patientService.searchPatients(
                patientId,
                name,
                mobile,
                bloodGroup,
                req.getDateOfBirth(),
                req.getPage(),
                req.getSize()
        );
    }


}
