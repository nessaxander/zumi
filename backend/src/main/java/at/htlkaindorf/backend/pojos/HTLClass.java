package at.htlkaindorf.backend.pojos;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "htlclasses")
public class HTLClass {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String classAcronym;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "htlClass")
    private Set<Student> students;


}
