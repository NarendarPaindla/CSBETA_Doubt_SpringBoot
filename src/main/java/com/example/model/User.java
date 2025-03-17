package com.example.model;

import lombok.Data;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;

    private String username; // unique
    private String email; // unique
    private String password; // hashed
    private Role role; // STUDENT or TRAINER

    private String gender; // e.g. "Male", "Female", "Other"
    private String programLevel; // e.g. "csbeta", "csalpha", "iotalpha"
    private String degreeLevel; // e.g. "Bachelor's", "Master's", "PhD."
    private String academicYear; // e.g. "1st Year (Freshman)", ...
    private String university; // e.g. "MIT"

    private String resetToken;
    private LocalDateTime resetTokenExpiry;
}
