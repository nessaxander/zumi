package at.htlkaindorf.backend.repositories;

import at.htlkaindorf.backend.pojos.HTLClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HTLClassRepository extends JpaRepository<HTLClass, Long> {
    Optional<HTLClass> findByClassAcronymIgnoreCase(String classAcronym);
}