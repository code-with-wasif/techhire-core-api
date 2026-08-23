package io.github.codewithwasif.techhire.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "resumes")
public class ResumeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String professionalTitle;
    private String skills;
    private String portfolioUrl;
    @Column(columnDefinition = "TEXT")
    private String bio;

    @OneToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private UserEntity candidateDetails;
}
