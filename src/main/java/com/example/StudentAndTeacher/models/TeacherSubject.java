package com.example.StudentAndTeacher.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "teacher_subject")
public class TeacherSubject {

    @Id
    @Column(name = "teacher_id")
    private Long teacher_id;
    @Column(name = "subject_id")
    private Long subject_id;


}
