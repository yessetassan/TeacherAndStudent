package com.example.StudentAndTeacher.services;

import com.example.StudentAndTeacher.models.Student;
import com.example.StudentAndTeacher.repo.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.cert.Extension;
import java.util.List;
import java.util.Optional;

@Service @Transactional
public class StudentService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    private final StudentRepo studentRepo;

    @Autowired
    public StudentService(StudentRepo studentRepo) {
        this.studentRepo = studentRepo;
    }

    public List<Student> all() {
        return studentRepo.findAll();
    }

    public void save(Student student) {
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        studentRepo.save(student);
    }
    public Optional<Student> getById(Long id) {
        return Optional.ofNullable(studentRepo.findById(id).stream().findAny().orElse(null));
    }
    public Student getByUsername(String username) {
        return studentRepo.findByUsername(username);
    }

    public void simple_save(Student student) {
        studentRepo.save(student);
    }

    public boolean check(String oldPassword, String password) {
        return passwordEncoder.matches(oldPassword,password);
    }
}
