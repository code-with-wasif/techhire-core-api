package io.github.codewithwasif.techhire.entity;

import jakarta.persistence.*;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private String coverLetterMessage;
    private String applicationStatus;

    @ManyToOne
    @JoinColumn(name = "applicant_id", nullable = false)
    private UserEntity applicantDetails;

    @ManyToOne
    @JoinColumn(name = "job_post_id", nullable = false)
    private JobPostEntity jobDetails;
}
