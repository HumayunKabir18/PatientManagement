package com.niketon.aroggo.app.patient.controller;

import com.niketon.aroggo.app.patient.entity.Patient;
import com.niketon.aroggo.app.patient.entity.PatientSearchEntity;
import com.niketon.aroggo.app.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/patients")
public class PatientController  {

    @Autowired
    PatientService patientService;

    // LIST PAGE
    @GetMapping("/list")
    public String listPage(Model model){

        model.addAttribute("patient", new Patient());

        return "/patient/patientList";
    }

    // REGISTRATION PAGE
    @GetMapping("/register")
    public String registrationPage(Model model){

        model.addAttribute("patient", new Patient());

        return "/patient/patientRegistration";
    }

    // SAVE PATIENT
    @PostMapping("/save")
    public String savePatient(@ModelAttribute Patient patient){

        Patient savedPatient = patientService.save(patient);

        return "redirect:/patients/list";
    }

    // DETAILS PAGE
    @GetMapping("/details/{id}")
    public String patientDetails(@PathVariable Long id, Model model){

        Patient patient = patientService.getPatientById(id);

        model.addAttribute("patient", patient);

        return "patient/patientDetails";
    }

    @PostMapping("/search")
    @ResponseBody
    public Page<Patient> search(@RequestBody PatientSearchEntity req) {
        String name =
                (req.getPatientName() == null || req.getPatientName().isBlank())
                        ? ""
                        : req.getPatientName();

        String mobile =
                (req.getMobileNo() == null || req.getMobileNo().isBlank())
                        ? ""
                        : req.getMobileNo();

        String bloodGroup =
                (req.getBloodGroup() == null || req.getBloodGroup().isBlank())
                        ? ""
                        : req.getBloodGroup();

        return patientService.searchPatients(
                name,
                mobile,
                bloodGroup,
                req.getDateOfBirth(),
                req.getPage(),
                req.getSize()
        );
    }

}
