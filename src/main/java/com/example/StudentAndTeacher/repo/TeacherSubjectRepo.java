package com.example.StudentAndTeacher.repo;

import com.example.StudentAndTeacher.models.TeacherSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherSubjectRepo extends JpaRepository<TeacherSubject, Long> {

    @Query("select s.subject_id from TeacherSubject s where s.teacher_id = ?1")
    List<Long> findAllByTeacher_id(Long id);

}
