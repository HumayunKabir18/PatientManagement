package com.niketon.aroggo.app.patient.service;

import com.niketon.aroggo.app.patient.entity.Patient;
import com.niketon.aroggo.app.patient.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    
    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }
    
    public Patient addPatient(Patient patient) {
        return patientRepository.save(patient);
    }
    
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
    
    public Patient getPatientById(UUID id) throws Exception{
        return patientRepository.findById(id).orElseThrow(() -> new Exception("Patient not found"));
    }
    
    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }
}
