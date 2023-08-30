package com.example.StudentAndTeacher.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "task_set")
public class TaskSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id ")
    private Long id ;
    @Column(name = "teacher_id ")
    private Long teacher_id ;
    @Column(name = "subject_id ")
    private Long subject_id ;
    @Column(name = "teacher_fileName")
    private String teacher_fileName;
    @Column(name = "deadline")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime deadline;
    @Column(name = "task_name")
    private String taskName;
    @Column(name = "posted_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime posted_date;
}
