package at.htlkaindorf.backend.repositories;

import at.htlkaindorf.backend.entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    List<Subject> findByShortNameIn(List<String> shortNames);
}
