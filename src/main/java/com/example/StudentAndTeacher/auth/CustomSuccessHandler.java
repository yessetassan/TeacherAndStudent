package com.example.StudentAndTeacher.auth;


import com.example.StudentAndTeacher.models.Student;
import com.example.StudentAndTeacher.models.Teacher;
import com.example.StudentAndTeacher.services.StudentService;
import com.example.StudentAndTeacher.services.TeacherService;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@NoArgsConstructor
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    private  StudentService studentService;
    private TeacherService teacherService;



    public CustomSuccessHandler(StudentService studentService, TeacherService teacherService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        System.out.println("Here u go !");
        String username = authentication.getName();

        Teacher teacher = teacherService.getByUsername(username);
        if (teacher != null) {
            response.sendRedirect("/teacher");
        }
        else{
            Student student = studentService.getByUsername(username);
            response.sendRedirect("/student");
        }
    }
}