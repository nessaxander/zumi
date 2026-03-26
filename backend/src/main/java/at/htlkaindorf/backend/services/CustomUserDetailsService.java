package at.htlkaindorf.backend.services;

import at.htlkaindorf.backend.entities.Student;
import at.htlkaindorf.backend.repositories.StudentRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final StudentRepository studentRepository;

    public CustomUserDetailsService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Student student = studentRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Kein Benutzer mit dieser E-Mail gefunden."));

        return User.builder()
                .username(student.getEmail())
                .password(student.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_STUDENT")))
                .build();
    }
}