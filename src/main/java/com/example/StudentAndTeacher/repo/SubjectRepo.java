package com.example.StudentAndTeacher.repo;

import com.example.StudentAndTeacher.models.Subject;
import com.example.StudentAndTeacher.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SubjectRepo  extends JpaRepository<Subject, Long> {



    Optional<Subject> findById(Long id);
    @Query("select s from Subject s ")
    Set<Subject> all();


}
