package io.github.codewithwasif.techhire.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "applications")
public class JobApplyEntity {
    @Id
    private ObjectId objectId;

    private String fullName;
    private String email;
    private String resumeLink;
    private String coverLetter;
    private String githubProfileUrl;
    private String applicationStatus;

    @DBRef
    private UserEntity userDetails;
}
