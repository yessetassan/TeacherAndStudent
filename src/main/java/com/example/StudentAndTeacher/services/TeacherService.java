package com.example.StudentAndTeacher.services;

import com.example.StudentAndTeacher.models.Student;
import com.example.StudentAndTeacher.models.Teacher;
import com.example.StudentAndTeacher.repo.TeacherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service @Transactional
public class TeacherService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    private final TeacherRepo teacherRepo;

    @Autowired
    public TeacherService(TeacherRepo teacherRepo) {
        this.teacherRepo = teacherRepo;
    }

    public List<Teacher> all() {
        return teacherRepo.findAll();
    }

    public Optional<Teacher> getByiD(Long id) {
        return teacherRepo.findById(id);
    }

    public Teacher getByUsername(String username) {
        return teacherRepo.findByUsername(username);
    }

    public void save(Teacher teacher) {
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        teacherRepo.save(teacher);
    }

    public void simple_save(Teacher teacher) {
        teacherRepo.save(teacher);
    }

    public void save_fileName(Teacher teacher) {
        teacherRepo.save(teacher);
    }

    public Optional<Teacher> findById(Long id) {
        return Optional.ofNullable(teacherRepo.findById(id).stream().findAny().orElse(null));
    }

    public boolean check(String oldPassword, String password) {

        return passwordEncoder.matches(oldPassword,password);
    }
}
