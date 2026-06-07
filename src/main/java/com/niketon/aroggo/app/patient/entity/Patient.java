package com.niketon.aroggo.app.patient.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Long patientId;

    // BASIC INFO
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "gender")
    private String gender;

    @Column(name = "age")
    private Integer age;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "blood_group")
    private String bloodGroup;

    @Column(name = "marital_status")
    private String maritalStatus;

    // CONTACT
    @Column(name = "mobile")
    private String mobile;

    @Column(name = "email")
    private String email;

    @Column(name = "address", length = 1000)
    private String address;

    @Column(name = "occupation")
    private String occupation;

    @Column(name = "emergency_contact")
    private String emergencyContact;

    // MEDICAL
    @Column(name = "allergies", length = 2000)
    private String allergies;

    @Column(name = "previous_medication", length = 3000)
    private String previousMedication;

    // CURRENT VISIT
    @Column(name = "chief_complaint", length = 4000)
    private String chiefComplaint;

    @Column(name = "symptoms", length = 4000)
    private String symptoms;

    @Column(name = "doctor_notes", length = 5000)
    private String doctorNotes;

    // VITALS
    @Column(name = "height")
    private String height;

    @Column(name = "weight")
    private String weight;

    @Column(name = "bmi")
    private String bmi;

    @Column(name = "blood_pressure")
    private String bloodPressure;

    @Column(name = "pulse_rate")
    private String pulseRate;

    @Column(name = "temperature")
    private String temperature;

    // APPOINTMENT
    @Column(name = "appointment_date")
    private LocalDate appointmentDate;

    @Column(name = "doctor_name")
    private String doctorName;

    @Column(name = "visit_type")
    private String visitType;
}