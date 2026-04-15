package at.htlkaindorf.backend.controller;

import at.htlkaindorf.backend.config.SubjectMapping;
import at.htlkaindorf.backend.dto.RegisterStudentRequest;
import at.htlkaindorf.backend.dto.StudentResponseDTO;
import at.htlkaindorf.backend.dto.StudentSubjectResponseDTO;
import at.htlkaindorf.backend.pojos.Department;
import at.htlkaindorf.backend.entities.HTLClass;
import at.htlkaindorf.backend.entities.Student;
import at.htlkaindorf.backend.entities.StudentSubject;
import at.htlkaindorf.backend.entities.Subject;
import at.htlkaindorf.backend.repositories.HTLClassRepository;
import at.htlkaindorf.backend.repositories.StudentRepository;
import at.htlkaindorf.backend.repositories.StudentSubjectRepository;
import at.htlkaindorf.backend.repositories.SubjectRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentRepository studentRepository;
    private final HTLClassRepository htlClassRepository;
    private final SubjectRepository subjectRepository;
    private final StudentSubjectRepository studentSubjectRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentController(StudentRepository studentRepository,
                             HTLClassRepository htlClassRepository,
                             SubjectRepository subjectRepository,
                             StudentSubjectRepository studentSubjectRepository,
                             PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.htlClassRepository = htlClassRepository;
        this.subjectRepository = subjectRepository;
        this.studentSubjectRepository = studentSubjectRepository;
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

        assignSubjectsToStudent(savedStudent);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapToStudentResponseDTO(savedStudent));
    }

    @GetMapping("/me/subjects")
    public ResponseEntity<List<StudentSubjectResponseDTO>> getMySubjects(Authentication authentication) {
        String email = authentication.getName();

        List<StudentSubject> studentSubjects = studentSubjectRepository.findByStudent_Email(email);

        List<StudentSubjectResponseDTO> response = studentSubjects.stream()
                .map(studentSubject -> StudentSubjectResponseDTO.builder()
                        .id(studentSubject.getId())
                        .longName(studentSubject.getSubject().getLongName())
                        .shortName(studentSubject.getSubject().getShortName())
                        .semester(studentSubject.getSemester())
                        .grade(studentSubject.getGrade())
                        .build())
                .toList();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        if (!studentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        studentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void assignSubjectsToStudent(Student student) {
        List<String> subjectShortNames = SubjectMapping.getSubjectsForDepartment(student.getDepartment());

        List<Subject> subjects = subjectRepository.findByShortNameIn(subjectShortNames);

        List<StudentSubject> studentSubjects = subjects.stream()
                .map(subject -> StudentSubject.builder()
                        .student(student)
                        .subject(subject)
                        .grade(null)
                        .semester(1)
                        .build())
                .toList();

        studentSubjectRepository.saveAll(studentSubjects);
    }

    private StudentResponseDTO mapToStudentResponseDTO(Student student) {
        return StudentResponseDTO.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .classAcronym(student.getHtlClass().getClassAcronym())
                .email(student.getEmail())
                .department(student.getDepartment())
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