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
@Table(name = "task_complete")
public class TaskComplete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long task_set_id;
    private String student_file_name;
    private Long student_id;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime passed_date;
    private Double score;

    public TaskComplete(Long task_set_id, Long student_id) {
        this.task_set_id = task_set_id;
        this.student_id = student_id;
    }
}
