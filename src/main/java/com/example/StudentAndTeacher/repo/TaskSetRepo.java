package com.example.StudentAndTeacher.repo;

import com.example.StudentAndTeacher.models.TaskSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TaskSetRepo extends JpaRepository<TaskSet, Long> {

    @Query("select t from TaskSet t where t.teacher_id = ?1 and t.subject_id = ?2")
    Set<TaskSet> findAllByTeacherAndSubject(Long t_id, Long s_id);

    @Query("select t from TaskSet t")
    Set<TaskSet> all();

    Optional<TaskSet> findById(Long id);
}
