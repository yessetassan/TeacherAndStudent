package com.example.StudentAndTeacher.repo;


import com.example.StudentAndTeacher.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepo extends JpaRepository<Teacher, Long> {

    Teacher findByUsername(String username);

}
