package com.niketon.aroggo.app.patient.controller;

import com.niketon.aroggo.app.patient.entity.Patient;
import com.niketon.aroggo.app.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
public class PatientController  {
    private final PatientService patientService;
    
    @Autowired
    public PatientController(PatientService patientService)  {
        this.patientService = patientService;
    }
    
    @PostMapping
    public ResponseEntity<Patient> addPatient(@RequestBody Patient patient)  {
        return ResponseEntity.ok(patientService.addPatient(patient));
    }
    
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients()  {
        return ResponseEntity.ok(patientService.getAllPatients());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable UUID id) throws Exception  {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity deletePatient(@PathVariable UUID id)  {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
