package at.htlkaindorf.backend.dto;

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
    private String department;
}