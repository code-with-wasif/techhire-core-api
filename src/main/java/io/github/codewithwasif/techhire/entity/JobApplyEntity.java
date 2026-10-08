package io.github.codewithwasif.techhire.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "applicants")
public class JobApplyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String coverLetterMessage;
    private String applicationStatus;
    private Long resumeId;

    @ManyToOne
    @JoinColumn(name = "applicant_id", nullable = false)
    private UserEntity applicantDetails;

    @ManyToOne
    @JoinColumn(name = "job_post_id", nullable = false)
    private JobPostEntity jobDetails;
}
