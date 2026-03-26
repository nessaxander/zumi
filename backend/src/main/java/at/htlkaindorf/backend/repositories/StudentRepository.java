package at.htlkaindorf.backend.repositories;

import at.htlkaindorf.backend.pojos.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByEmailIgnoreCase(String email);
}