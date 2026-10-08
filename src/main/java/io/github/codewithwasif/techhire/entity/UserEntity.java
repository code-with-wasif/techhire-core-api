package io.github.codewithwasif.techhire.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @OneToMany(mappedBy = "candidateDetails", cascade = CascadeType.REMOVE)
    private List<ResumeEntity> resumes = new ArrayList<>();

    @OneToMany(mappedBy = "employerDetails", cascade = CascadeType.REMOVE)
    private List<JobPostEntity> posts = new ArrayList<>();

    @OneToMany(mappedBy = "applicantDetails", cascade = CascadeType.REMOVE)
    private List<JobApplyEntity> applications = new ArrayList<>();

}
