package com.example.StudentAndTeacher.modelmapper;


import com.example.StudentAndTeacher.models.Role;
import com.example.StudentAndTeacher.models.Subject;
import com.example.StudentAndTeacher.models.Teacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudentDTO {
    private Long id;
    private String username;
    private String full_name;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;
    private String password;
    private String email;
    private String confirmPassword;
    private String fileName;
    private Set<Teacher> teachers = new HashSet<>();
    private Set<Role> student_roles = new HashSet<>();
    private Set<Subject> student_subject = new HashSet<>();
}
