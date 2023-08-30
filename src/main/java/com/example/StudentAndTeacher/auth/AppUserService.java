package com.example.StudentAndTeacher.auth;

import com.example.StudentAndTeacher.models.Student;
import com.example.StudentAndTeacher.models.Teacher;
import com.example.StudentAndTeacher.repo.StudentRepo;
import com.example.StudentAndTeacher.repo.TeacherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.cert.Extension;

@Service @Transactional
public class AppUserService implements UserDetailsService {

    private final StudentRepo studentRepo;
    private final TeacherRepo teacherRepo;

    @Autowired
    public AppUserService(StudentRepo studentRepo, TeacherRepo teacherRepo) {
        this.studentRepo = studentRepo;
        this.teacherRepo = teacherRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println(username);
        Student student = studentRepo.findByUsername(username);

        System.out.println(student);
        if (student != null) {
            return new StudentDetails(student);
        }

        Teacher teacher = teacherRepo.findByUsername(username);
        if (teacher != null) {
            return new TeacherUserDetails(teacher);
        }

        throw new UsernameNotFoundException("We can't find this username !");
    }



}
