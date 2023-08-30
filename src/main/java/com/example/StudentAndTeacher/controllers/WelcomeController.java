package com.example.StudentAndTeacher.controllers;

import com.example.StudentAndTeacher.models.Student;
import com.example.StudentAndTeacher.models.Teacher;
import com.example.StudentAndTeacher.services.StudentService;
import com.example.StudentAndTeacher.services.TeacherService;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping
public class WelcomeController {

    private final StudentService studentService;
    private final TeacherService teacherService;

    public WelcomeController(StudentService studentService, TeacherService teacherService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
    }
    @Data
    class Login {
        private String username;
        private String password;
    }


    @GetMapping("/login")
    public String login(@ModelAttribute("login") Login login) {
        return "login";
    }

    @PostMapping("/login")
    public String sdfc(@ModelAttribute("login") Login login) {
        return "login";
    }

    @GetMapping("/registration/teacher")
    public String edfvfe(@Valid @ModelAttribute("teacher") Teacher teacher , BindingResult result, Model model) {
        System.out.println("Teacher");
        return "registration_teacher";
    }

    @PostMapping("/registration/teacher")
    public String ergf(@Valid @ModelAttribute("teacher") Teacher teacher , BindingResult result, Model model) {
        if (teacherService.getByUsername(teacher.getUsername()) != null){
            result.rejectValue("username" , "","Username must be unique !");
        }

        if (result.hasErrors()) {
            return "registration_teacher";
        }

        teacherService.save(teacher);

        return "login";
    }

    @GetMapping("/registration/student")
    public String eerfdfvfe(@Valid @ModelAttribute("student") Student student , BindingResult result, Model model) {
        return "registration_student";
    }

    @PostMapping("/registration/student")
    public String ergf(@Valid @ModelAttribute("student") Student student, BindingResult result, Model model) {
        if (studentService.getByUsername(student.getUsername()) != null){
            result.rejectValue("username" , "","Username must be unique !");
        }

        if (result.hasErrors()) {
            return "registration_student";
        }

        studentService.save(student);
        return "login";
    }



}
