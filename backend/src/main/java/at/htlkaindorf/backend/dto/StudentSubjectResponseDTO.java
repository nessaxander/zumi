package at.htlkaindorf.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentSubjectResponseDTO {
    private Long id;
    private String longName;
    private String shortName;
    private Integer semester;
    private Integer grade;
}