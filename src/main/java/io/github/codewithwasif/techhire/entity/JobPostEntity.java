package io.github.codewithwasif.techhire.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "job_posts")
public class JobPostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String companyName;
    private String description;
    private int minSalary;
    private int maxSalary;
    private List<String> techStack;
    private String status;

    @OneToMany(mappedBy = "jobDetails")
    private List<JobApplyEntity> applications = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "employer_id")
    private UserEntity employerDetails;
}
