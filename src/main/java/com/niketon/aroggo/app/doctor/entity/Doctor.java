package com.niketon.aroggo.app.doctor.entity;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter   @Setter
@NoArgsConstructor   @AllArgsConstructor
public class Doctor  {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    private int age;

    private String mobileNumber;

    private String specialization;
    
    private LocalDateTime createdAt = LocalDateTime.now();
}
