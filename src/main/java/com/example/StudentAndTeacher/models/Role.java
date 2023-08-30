package com.example.StudentAndTeacher.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;


    @ManyToMany(mappedBy = "teacher_roles",fetch = FetchType.EAGER)
    @JsonBackReference
    private Set<Teacher> teachers = new HashSet<>();


    @ManyToMany(mappedBy = "student_roles",fetch = FetchType.EAGER)
    @JsonBackReference
    private Set<Student> students = new HashSet<>();

    public String toString() {
        return "students";
    }
}
