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
    private Long id;

    // BASIC INFO
    private String firstName;
    private String lastName;
    private String gender;

    private Integer age;

    private LocalDate dateOfBirth;

    private String bloodGroup;

    private String maritalStatus;

    // CONTACT
    private String mobile;
    private String email;

    @Column(length = 1000)
    private String address;

    private String occupation;

    private String emergencyContact;

    // MEDICAL
    @Column(length = 2000)
    private String allergies;

    @Column(length = 3000)
    private String previousMedication;

    // CURRENT VISIT
    @Column(length = 4000)
    private String chiefComplaint;

    @Column(length = 4000)
    private String symptoms;

    @Column(length = 5000)
    private String doctorNotes;

    // VITALS
    private String height;
    private String weight;
    private String bmi;

    private String bloodPressure;

    private String pulseRate;

    private String temperature;

    // APPOINTMENT
    private LocalDate appointmentDate;

    private String doctorName;

    private String visitType;

}