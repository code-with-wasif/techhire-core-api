package io.github.codewithwasif.techhire.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "job_posts")
public class JobPostEntity {
    @Id
    private String id;
    private String title;
    private String companyName;
    private String description;
    private int minSalary;
    private int maxSalary;
    private List<String> techStack;
    private String status;

    @DBRef
    private List<JobApplyEntity> applicants = new ArrayList<>();
}
