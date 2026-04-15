package io.github.codewithwasif.techhire.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class UserEntity {
    @Id
    private String id;
    private String userName;
    private String email;
    private String password;
    private List<String> roles = new ArrayList<>();
}
