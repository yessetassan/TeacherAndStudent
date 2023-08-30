package com.example.StudentAndTeacher.repo;

import com.example.StudentAndTeacher.models.Student;
import com.example.StudentAndTeacher.models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface StudentRepo extends JpaRepository<Student, Long> {

    Student findByUsername(String username);

}
