package at.htlkaindorf.backend.repositories;

import at.htlkaindorf.backend.entities.StudentSubject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentSubjectRepository extends JpaRepository<StudentSubject, Long> {
    List<StudentSubject> findByStudentId(Long studentId);
    List<StudentSubject> findByStudentEmail(String email);
}