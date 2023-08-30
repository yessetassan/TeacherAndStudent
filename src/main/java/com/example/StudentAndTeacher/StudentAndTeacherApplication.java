package com.example.StudentAndTeacher;

import com.example.StudentAndTeacher.models.Student;
import com.example.StudentAndTeacher.models.Teacher;
import com.example.StudentAndTeacher.services.StudentService;
import com.example.StudentAndTeacher.services.TeacherService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootApplication
public class StudentAndTeacherApplication {
	public static void main(String[] args) {
		SpringApplication.run(StudentAndTeacherApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
