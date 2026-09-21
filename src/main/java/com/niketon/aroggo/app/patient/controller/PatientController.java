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
import java.util.List;

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

        if (form.getAppointment() != null
                && form.getAppointment().getAppointmentDate() != null) {

            appointmentService.saveAppointment(
                    savedPatient,
                    form.getAppointment()
            );
        }

        return "redirect:/patients/details/"
                + savedPatient.getPatientCode();
    }


    @PostMapping("/update")
    public String updatePatient(
            @ModelAttribute Patient patient
    ) {

        patientService.update(patient);

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


    /*
     * ============================================================
     * NEW APPOINTMENT
     * ============================================================
     */

    @GetMapping("/appointment/new/{patientCode}")
    public String newAppointment(
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

        form.getAppointment()
                .setAppointmentDate(
                        LocalDate.now()
                );

        model.addAttribute(
                "form",
                form
        );

        /*
         * New appointment = editable.
         */
        model.addAttribute(
                "editable",
                true
        );

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

                            medicineForm.setMedicineType(
                                    medicine.getMedicineType()
                            );

                            medicineForm.setDoses(
                                    medicine.getDoses()
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
        model.addAttribute(
                "editable",
                false
        );


        return "patient/appointmentForm";
    }


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

        appointmentService.saveAppointment(
                patient,
                form.getAppointment()
        );

        return "redirect:/patients/details/"
                + patient.getPatientCode();
    }


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
