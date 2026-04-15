package at.htlkaindorf.backend.services;

import at.htlkaindorf.backend.config.SubjectMapping;
import at.htlkaindorf.backend.dto.RegisterStudentRequest;
import at.htlkaindorf.backend.dto.StudentResponseDTO;
import at.htlkaindorf.backend.entities.Student;
import at.htlkaindorf.backend.entities.StudentSubject;
import at.htlkaindorf.backend.entities.Subject;
import at.htlkaindorf.backend.repositories.StudentRepository;
import at.htlkaindorf.backend.repositories.StudentSubjectRepository;
import at.htlkaindorf.backend.repositories.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final StudentSubjectRepository studentSubjectRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentResponseDTO registerStudent(RegisterStudentRequest request) {
        if (studentRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new IllegalArgumentException("A student with this email already exists.");
        }

        Student student = Student.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .department(request.getDepartment())
                .build();

        Student savedStudent = studentRepository.save(student);

        assignSubjectsToStudent(savedStudent);

        return StudentResponseDTO.builder()
                .id(savedStudent.getId())
                .firstName(savedStudent.getFirstName())
                .lastName(savedStudent.getLastName())
                .email(savedStudent.getEmail())
                .department(savedStudent.getDepartment())
                .build();
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
}