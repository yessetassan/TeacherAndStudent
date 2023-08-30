package com.example.StudentAndTeacher.repo;

import com.example.StudentAndTeacher.models.TaskComplete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface TaskCompleteRepo extends JpaRepository<TaskComplete, Long> {

    @Query("select t from TaskComplete t where t.task_set_id = ?1")
    Set<TaskComplete> findAllByTask_set_id(Long id);

    @Query("select t from TaskComplete t where t.task_set_id = ?1 and t.student_id = ?2")
    TaskComplete findByTask_set_idAndStudent(Long task_set_id , Long student_id);
}
