package at.htlkaindorf.backend.dto;

import at.htlkaindorf.backend.pojos.Department;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoggedInStudentResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String classAcronym;
    private String email;
    private Department department;
}