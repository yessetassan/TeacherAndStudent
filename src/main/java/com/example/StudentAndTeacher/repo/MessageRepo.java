package com.example.StudentAndTeacher.repo;

import com.example.StudentAndTeacher.models.Message;
import lombok.extern.java.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {

    @Query("select m from Message m where m.to_username like ?1")
    Set<Message> findByTo_username(String to_username);

    @Query("select m from Message m where m.from_username like ?1")
    Set<Message> findByFrom_username(String from_username);

}
