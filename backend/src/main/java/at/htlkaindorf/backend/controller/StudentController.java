package at.htlkaindorf.backend.controller;

import at.htlkaindorf.backend.dto.RegisterStudentRequest;
import at.htlkaindorf.backend.pojos.Department;
import at.htlkaindorf.backend.pojos.HTLClass;
import at.htlkaindorf.backend.pojos.Student;
import at.htlkaindorf.backend.repositories.HTLClassRepository;
import at.htlkaindorf.backend.repositories.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "http://localhost:5173")
public class StudentController {

    private final StudentRepository studentRepository;
    private final HTLClassRepository htlClassRepository;

    public StudentController(StudentRepository studentRepository, HTLClassRepository htlClassRepository) {
        this.studentRepository = studentRepository;
        this.htlClassRepository = htlClassRepository;
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return studentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> registerStudent(@RequestBody RegisterStudentRequest request) {

        if (request.getFirstName() == null || request.getFirstName().isBlank() ||
                request.getLastName() == null || request.getLastName().isBlank() ||
                request.getEmail() == null || request.getEmail().isBlank() ||
                request.getPassword() == null || request.getPassword().isBlank() ||
                request.getClassAcronym() == null || request.getClassAcronym().isBlank()) {
            return ResponseEntity.badRequest().body("Alle Felder müssen ausgefüllt sein.");
        }

        if (studentRepository.existsByEmailIgnoreCase(request.getEmail().trim())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Diese E-Mail wird bereits verwendet.");
        }

        String normalizedClassAcronym = request.getClassAcronym().trim().toUpperCase();

        if (!isValidClassAcronym(normalizedClassAcronym)) {
            return ResponseEntity.badRequest().body("Ungültiges Klassenkürzel.");
        }

        Department department = determineDepartment(normalizedClassAcronym);
        if (department == null) {
            return ResponseEntity.badRequest().body("Klassenkürzel konnte keiner Abteilung zugeordnet werden.");
        }

        HTLClass htlClass = htlClassRepository.findByClassAcronymIgnoreCase(normalizedClassAcronym)
                .orElseGet(() -> {
                    HTLClass newClass = HTLClass.builder()
                            .classAcronym(normalizedClassAcronym)
                            .build();
                    return htlClassRepository.save(newClass);
                });

        Student student = Student.builder()
                .firstName(request.getFirstName().trim())
                .lastName(request.getLastName().trim())
                .email(request.getEmail().trim())
                .password(request.getPassword())
                .department(department)
                .htlClass(htlClass)
                .build();

        Student savedStudent = studentRepository.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        if (!studentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        studentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private boolean isValidClassAcronym(String classAcronym) {
        return classAcronym.matches("^[A-Z]{4,6}\\d{2}$");
    }

    private Department determineDepartment(String classAcronym) {
        if (classAcronym.startsWith("BHME")) {
            return Department.ROBOTIK;
        }

        if (classAcronym.contains("HMBA")) {
            return Department.AUTOMATISIERUNG;
        }

        if (classAcronym.contains("HME")) {
            return Department.MECHATRONIK;
        }

        if (classAcronym.contains("HIF")) {
            return Department.INFORMATIK;
        }

        return null;
    }
}