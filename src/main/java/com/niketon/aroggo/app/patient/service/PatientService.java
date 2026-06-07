package com.niketon.aroggo.app.patient.service;

import com.niketon.aroggo.app.patient.entity.Patient;
import com.niketon.aroggo.app.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {
    @Autowired
  PatientRepository patientRepository;

    // SAVE
    public Patient save(Patient patient){
        return patientRepository.save(patient);
    }

    // GET ALL
    public List<Patient> getAllPatients(){
        return patientRepository.findAll();
    }

    // GET BY ID
    public Patient getPatientById(Long id){
        return patientRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Patient Not Found"));
    }

    // DELETE
    public void deletePatient(Long id){
        patientRepository.deleteById(id);
    }
}
