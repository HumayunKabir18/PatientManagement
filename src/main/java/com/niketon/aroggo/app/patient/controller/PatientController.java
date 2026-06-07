package com.niketon.aroggo.app.patient.controller;

import com.niketon.aroggo.app.patient.entity.Patient;
import com.niketon.aroggo.app.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
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

        return "redirect:/patients/details/" + savedPatient.getId();
    }

    // DETAILS PAGE
    @GetMapping("/details/{id}")
    public String patientDetails(@PathVariable Long id, Model model){

        Patient patient = patientService.getPatientById(id);

        model.addAttribute("patient", patient);

        return "patient/patient-details";
    }

}
