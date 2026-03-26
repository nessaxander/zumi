package at.htlkaindorf.backend.controller;

import at.htlkaindorf.backend.dto.RegisterStudentRequest;
import at.htlkaindorf.backend.dto.StudentResponseDTO;
import at.htlkaindorf.backend.pojos.Department;
import at.htlkaindorf.backend.entities.HTLClass;
import at.htlkaindorf.backend.entities.Student;
import at.htlkaindorf.backend.repositories.HTLClassRepository;
import at.htlkaindorf.backend.repositories.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentRepository studentRepository;
    private final HTLClassRepository htlClassRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentController(StudentRepository studentRepository,
                             HTLClassRepository htlClassRepository,
                             PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.htlClassRepository = htlClassRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public List<StudentResponseDTO> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(this::mapToStudentResponseDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable Long id) {
        return studentRepository.findById(id)
                .map(student -> ResponseEntity.ok(mapToStudentResponseDTO(student)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerStudent(@RequestBody RegisterStudentRequest request) {

        if (request.getFirstName() == null || request.getFirstName().isBlank() ||
                request.getLastName() == null || request.getLastName().isBlank() ||
                request.getEmail() == null || request.getEmail().isBlank() ||
                request.getPassword() == null || request.getPassword().isBlank() ||
                request.getClassAcronym() == null || request.getClassAcronym().isBlank()) {
            return ResponseEntity.badRequest().body("Alle Felder müssen ausgefüllt sein.");
        }

        String normalizedEmail = request.getEmail().trim().toLowerCase();
        String normalizedClassAcronym = request.getClassAcronym().trim().toUpperCase();

        if (studentRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Diese E-Mail wird bereits verwendet.");
        }

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

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        Student student = Student.builder()
                .firstName(request.getFirstName().trim())
                .lastName(request.getLastName().trim())
                .email(normalizedEmail)
                .password(hashedPassword)
                .department(department)
                .htlClass(htlClass)
                .build();

        Student savedStudent = studentRepository.save(student);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapToStudentResponseDTO(savedStudent));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        if (!studentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        studentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private StudentResponseDTO mapToStudentResponseDTO(Student student) {
        return StudentResponseDTO.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .classAcronym(student.getHtlClass().getClassAcronym())
                .email(student.getEmail())
                .department(student.getDepartment().name())
                .build();
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