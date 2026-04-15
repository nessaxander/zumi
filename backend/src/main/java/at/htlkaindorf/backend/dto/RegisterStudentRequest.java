package at.htlkaindorf.backend.dto;

import at.htlkaindorf.backend.pojos.Department;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterStudentRequest {

    private String firstName;
    private String lastName;
    private String classAcronym;
    private String email;
    private String password;
    private Department department;
}