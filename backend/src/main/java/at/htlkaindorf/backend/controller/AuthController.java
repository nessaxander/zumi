package at.htlkaindorf.backend.controller;

import at.htlkaindorf.backend.dto.LoggedInStudentResponseDTO;
import at.htlkaindorf.backend.dto.LoginRequestDTO;
import at.htlkaindorf.backend.entities.Student;
import at.htlkaindorf.backend.repositories.StudentRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private final StudentRepository studentRepository;

    public AuthController(AuthenticationManager authenticationManager,
                          SecurityContextRepository securityContextRepository,
                          StudentRepository studentRepository) {
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
        this.studentRepository = studentRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request,
                                   HttpServletRequest httpRequest,
                                   HttpServletResponse httpResponse) {

        if (request.getEmail() == null || request.getEmail().isBlank() ||
                request.getPassword() == null || request.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body("E-Mail und Passwort müssen ausgefüllt sein.");
        }

        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(
                        request.getEmail().trim().toLowerCase(),
                        request.getPassword()
                )
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, httpRequest, httpResponse);

        Student student = studentRepository.findByEmailIgnoreCase(request.getEmail().trim().toLowerCase())
                .orElseThrow();

        return ResponseEntity.ok(mapToLoggedInStudentResponseDTO(student));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nicht eingeloggt.");
        }

        String email = authentication.getName();

        Student student = studentRepository.findByEmailIgnoreCase(email)
                .orElse(null);

        if (student == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Benutzer nicht gefunden.");
        }

        return ResponseEntity.ok(mapToLoggedInStudentResponseDTO(student));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request,
                                    HttpServletResponse response) {
        HttpSession session = request.getSession(false);

        SecurityContextHolder.clearContext();

        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.ok("Logout erfolgreich.");
    }

    private LoggedInStudentResponseDTO mapToLoggedInStudentResponseDTO(Student student) {
        return LoggedInStudentResponseDTO.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .classAcronym(student.getHtlClass().getClassAcronym())
                .email(student.getEmail())
                .department(student.getDepartment())
                .build();
    }
}