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
	@Column(name = "mental_symptoms", length = 2000)
	private String mentalSymptoms;

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

	@Column(name = "diabetes_rate")
	private String diabetesRate;

	// APPOINTMENT
	@Column(name = "appointment_date")
	private LocalDate appointmentDate;

	@Column(name = "doctor_name")
	private String doctorName;

	@Column(name = "visit_type")
	private String visitType;

	@Column(length = 1)
	private boolean diabetes;

	@Column(length = 1)
	private boolean hypertension;

	@Column(length = 1)
	private boolean asthma;

	@Column(length = 1)
	private boolean migraine;

	@Column(length = 1)
	private boolean arthritis;

	@Column(name="rheumatic_arthritis",length = 1)
	private boolean rheumaticArthritis;

	@Column(name="rheumatic_fever",length = 1)
	private boolean rheumaticFever;

	@Column(length = 1)
	private boolean allergy;

	@Column(length = 1)
	private boolean fever;

	@Column(length = 1)
	private boolean cough;

	@Column(length = 1)
	private boolean cold;

	@Column(length = 1)
	private boolean sinusitis;

	@Column(length = 1)
	private boolean eczema;

	@Column(length = 1)
	private boolean constipation;

	@Column(length = 1)
	private boolean diarrhea;

	@Column(length = 1)
	private boolean insomnia;

	@Column(length = 1)
	private boolean anxiety;

	@Column(length = 1)
	private boolean depression;

	@Column(length = 1)
	private boolean obesity;

	@Column(length = 1)
	private boolean copd;

	@Column(length = 1)
	private boolean gerd;

	@Column(length = 1)
	private boolean bronchitis;

	@Column(length = 1)
	private boolean dengue;

	@Column(length = 1)
	private boolean typhoid;

	@Column(length = 1)
	private boolean backPain;

	@Column(length = 1)
	private boolean neckPain;

	@Column(name="thyroid_disorder",length = 1)
	private boolean thyroidDisorder;

	@Column(name="skin_allergy",length = 1)
	private boolean skinAllergy;

	@Column(name="kidney_stone",length = 1)
	private boolean kidneyStone;

	@Column(name="gastric_problem",length = 1)
	private boolean gastricProblem;

	@Column(name="hemorrhoids_piles",length = 1)
	private boolean hemorrhoidsPiles;

	@Column(name="urinary_tract_infectionUti",length = 1)
	private boolean urinaryTractInfectionUti;

	@Column(length = 1)
	private boolean aids;

	@Column(length = 1)
	private boolean tumor;

	@Column(name="lipomas_tumor",length = 1)
	private boolean lipomasTumor;

	@Column(length = 1)
	private boolean dogBite;

	@Column(length = 1)
	private boolean catBite;

	@Column(length = 1)
	private boolean snakeBite;

	@Column(length = 1)
	private boolean centipedesBite;

	@Column(length = 1)
	private boolean otherBites;



}