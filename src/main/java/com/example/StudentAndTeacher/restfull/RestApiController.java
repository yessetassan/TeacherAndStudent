package com.example.StudentAndTeacher.restfull;

import com.example.StudentAndTeacher.models.Student;
import com.example.StudentAndTeacher.models.Subject;
import com.example.StudentAndTeacher.models.Teacher;
import com.example.StudentAndTeacher.repo.StudentRepo;
import com.example.StudentAndTeacher.repo.SubjectRepo;
import com.example.StudentAndTeacher.repo.TeacherRepo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RestApiController {

    private final TeacherRepo teacherRepo;
    private final StudentRepo studentRepo;
    private final SubjectRepo subjectRepo;


    @Autowired
    public RestApiController(TeacherRepo teacherRepo, StudentRepo studentRepo, SubjectRepo subjectRepo) {
        this.teacherRepo = teacherRepo;
        this.studentRepo = studentRepo;
        this.subjectRepo = subjectRepo;
    }

    @GetMapping("/all/students")
    public ResponseEntity<List<Student>> all() {
        return ResponseEntity.ok().body(studentRepo.findAll());
    }

    @GetMapping("/all/teachers")
    public ResponseEntity<List<Teacher>> allt() {
        return ResponseEntity.ok().body(teacherRepo.findAll());
    }

    @PostMapping(value = "/subject_create",consumes={"application/json"})
    public ResponseEntity<Subject> create(@RequestBody Subject subject) {

        return new ResponseEntity(subjectRepo.save(subject), HttpStatus.OK);
    }

    @PostMapping(value = "/teacher_subject")
    public ResponseEntity<Subject> create(@RequestBody Map<String, String> map) {

        Long teacher_long = Long.parseLong("" +  map.get("teacher_id")),
                subject_long = Long.parseLong("" +  map.get("subject_id"));
        Optional<Teacher> teacher = teacherRepo.findById(teacher_long);
        Optional<Subject> subject = subjectRepo.findById(subject_long);
        teacher.get().getSubjects().add(subject.get());
        teacherRepo.save(teacher.get());
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("teacher_student")
    public ResponseEntity<Map<String, String>> dfw(@RequestBody Map<String, String> map) {
        Long teacher_long = Long.parseLong("" +  map.get("teacher_id")),
                student_id = Long.parseLong("" +  map.get("student_id"));
        Optional<Teacher> teacher = teacherRepo.findById(teacher_long);
        Optional<Student> student = studentRepo.findById(student_id);
        teacher.get().getStudents().add(student.get());
        teacherRepo.save(teacher.get());
        return new ResponseEntity(HttpStatus.OK);
    }
}

