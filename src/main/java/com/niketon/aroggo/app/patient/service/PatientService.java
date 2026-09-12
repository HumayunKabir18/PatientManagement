package com.niketon.aroggo.app.patient.service;

import com.niketon.aroggo.app.patient.entity.Patient;
import com.niketon.aroggo.app.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Page<Patient> searchPatients(
            String name,
            String mobile,
            String bloodGroup,
            LocalDate dob,
            int page,
            int size
    ){

        Pageable pageable = PageRequest.of(page, size);
        Page<Patient> patients = patientRepository.searchPatients(
                 name,
                mobile,
                bloodGroup,
                dob,
                pageable
        );

        return patients;
    }

//    public Page<Patient> searchPatients(
//            String name,
//            String mobile,
//            String bloodGroup,
//            LocalDate dob,
//            int page,
//            int size
//    ) {
//
////        Pageable pageable = PageRequest.of(page, size);
//
//        Pageable pageable = PageRequest.of(page, size, Sort.by("patientId").descending());
//        return patientRepository.searchPatients(
//                (name == null || name.isEmpty()) ? null : name,
//                (mobile == null || mobile.isEmpty()) ? null : mobile,
//                (bloodGroup == null || bloodGroup.isEmpty()) ? null : bloodGroup,
//                dob,
//                pageable
//        );
//    }
}
