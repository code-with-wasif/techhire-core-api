package io.github.codewithwasif.techhire.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;
    private String email;
    private String password;

    @ElementCollection
    private List<String> roles = new ArrayList<>();

    @OneToOne(mappedBy = "candidateDetails", cascade = CascadeType.REMOVE)
    private ResumeEntity resume;

    @OneToMany(mappedBy = "employerDetails", cascade = CascadeType.REMOVE)
    private List<JobPostEntity> posts = new ArrayList<>();

    @OneToMany(mappedBy = "applicantDetails", cascade = CascadeType.REMOVE)
    private List<JobApplyEntity> applications = new ArrayList<>();

}
