package at.htlkaindorf.backend.entities;

import jakarta.persistence.*;
import lombok.*;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, name="subject_name_long")
    private String longName;

    @Column(nullable = false, unique = true, name="subject_name_short")
    private String shortName;


}
