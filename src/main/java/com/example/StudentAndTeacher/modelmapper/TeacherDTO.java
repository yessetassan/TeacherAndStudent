package com.example.StudentAndTeacher.modelmapper;

import com.example.StudentAndTeacher.models.Role;
import com.example.StudentAndTeacher.models.Student;
import com.example.StudentAndTeacher.models.Subject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TeacherDTO {
    private Long id;
    private String username;
    private String full_name;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;
    private String password;
    private String email;
    private String confirmPassword;
    private String fileName;
    private Set<Subject> subjects = new HashSet<>();
    private Set<Role> teacher_roles = new HashSet<>();
    private Set<Student> students = new HashSet<>();
}
