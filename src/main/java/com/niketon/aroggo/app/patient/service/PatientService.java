package com.niketon.aroggo.app.patient.service;

import com.niketon.aroggo.app.patient.entity.Patient;
import com.niketon.aroggo.app.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.time.format.DateTimeFormatter;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {
    @Autowired
    PatientRepository patientRepository;

    // SAVE / UPDATE
    public Patient save(Patient patient) {

        if (patient.getPatientId() == null) {

            String lastPatientCode = patientRepository.findLastPatientCode();

            long nextNumber = 1;

            if (lastPatientCode != null && !lastPatientCode.isBlank()) {
                String sequencePart = lastPatientCode.substring(8);
                nextNumber = Long.parseLong(sequencePart) + 1;
            }

            String currentDate = LocalDate.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            String patientCode = currentDate +
                    String.format("%04d", nextNumber);

            patient.setPatientCode(patientCode);
        }

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

    public Page<Patient> searchPatients(
            String patientId,
            String name,
            String mobile,
            String bloodGroup,
            LocalDate dob,
            int page,
            int size
    ){

        Pageable pageable = PageRequest.of(page, size);
        Page<Patient> patients = patientRepository.searchPatients(
                patientId,
                name,
                mobile,
                bloodGroup,
                dob,
                pageable
        );

        return patients;
    }


public Patient getPatientByCode(String patientCode) {
    return patientRepository.findByPatientCode(patientCode)
            .orElseThrow(() ->
                    new RuntimeException("Patient Not Found"));
}

    public Patient update(Patient patient) {

        Patient existingPatient = patientRepository.findById(patient.getPatientId())
                .orElseThrow(() ->
                        new RuntimeException("Patient Not Found"));

        patient.setPatientCode(existingPatient.getPatientCode());

        return patientRepository.save(patient);
    }
}
