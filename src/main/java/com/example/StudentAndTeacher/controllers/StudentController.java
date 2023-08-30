package com.example.StudentAndTeacher.controllers;

import com.example.StudentAndTeacher.modelmapper.StudentDTO;
import com.example.StudentAndTeacher.modelmapper.TeacherDTO;
import com.example.StudentAndTeacher.models.*;
import com.example.StudentAndTeacher.repo.*;
import com.example.StudentAndTeacher.services.StudentService;
import com.example.StudentAndTeacher.services.TeacherService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final SubjectRepo subjectRepo;
    private final MessageRepo messageRepo;
    private final TaskSetRepo taskSetRepo;
    private final TaskCompleteRepo taskCompleteRepo;

    private final FeedbackRepo feedbackRepo;

    public StudentController(StudentService studentService, TeacherService teacherService, SubjectRepo subjectRepo, MessageRepo messageRepo, TaskSetRepo taskSetRepo, TaskCompleteRepo taskCompleteRepo, FeedbackRepo feedbackRepo) {
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.subjectRepo = subjectRepo;
        this.messageRepo = messageRepo;
        this.taskSetRepo = taskSetRepo;
        this.taskCompleteRepo = taskCompleteRepo;
        this.feedbackRepo = feedbackRepo;
    }

    private Student student;

    @GetMapping
    public String fvnw(Principal principal,
                       Model model) {


        student = studentService.getByUsername(principal.getName());
        model.addAttribute("student", student);
        model.addAttribute("subjects", student.getStudent_subject());

        for (Teacher t : student.getTeachers()) {
            System.out.println(t);
        }
        return "student/page";
    }

    @GetMapping("/set_profile")
    public String dfv(Model model,
                      Principal principal) {
        student = studentService.getByUsername(principal.getName());
        model.addAttribute("dto", new ModelMapper().map(student, StudentDTO.class));
        model.addAttribute("password_changer", new PasswordChanger());
        model.addAttribute("fn", new FN(student.getFileName()));
        return "student/set_profile";
    }

    @PostMapping("/set_profile")
    public String fdeve(@ModelAttribute("fn") FN fn,
                        @ModelAttribute("dto") StudentDTO studentDTO,
                        @Valid @ModelAttribute("password_changer") PasswordChanger passwordChanger,
                        BindingResult password_result,
                        Model model,
                        Principal principal) {

        student = studentService.getByUsername(principal.getName());

        if (passwordChanger.getNew_password() != null) {

            if (!studentService.check(passwordChanger.getOld_password(), student.getPassword())) {
                password_result.rejectValue("old_password", "", "Old password doesn't match !");
            }

            if (!passwordChanger.getNew_password().equals(passwordChanger.getRepeat_password())) {
                password_result.rejectValue("repeat_password", "", "Repeat password doesn't match !");
            }

            if (password_result.hasErrors()) {
                model.addAttribute("dto", new ModelMapper().map(student, StudentDTO.class));
                model.addAttribute("password_changer", passwordChanger);
                return "student/set_profile";
            }
            student.setPassword(passwordChanger.getNew_password());
            studentService.simple_save(student);
        }
        else if (studentDTO.getEmail() != null) {
            dto(studentDTO,principal);
        }
        else {
            student.setFileName(fn.getFile_name());
            studentService.simple_save(student);
        }


        return "redirect:/student";
    }

    private void dto(StudentDTO studentDTO, Principal principal) {
        student = studentService.getByUsername(principal.getName());
        student = new ModelMapper().map(studentDTO, Student.class);
        studentService.simple_save(student);
    }

    @GetMapping("/registration")
    public String registration(Principal principal,
                               Model model) {

        student = studentService.getByUsername(principal.getName());
        model.addAttribute("student", student);
        model.addAttribute("subjects" , subjectRepo.all());
        return "student/registration";
    }

    @GetMapping("/registration/{s_id}")
    public String ergfv(@PathVariable("s_id") Long s_id,
                        Principal principal,
                        Model model) {
        student = studentService.getByUsername(principal.getName());
        if (student.getStudent_subject().contains(subjectRepo.getById(s_id))) {
            return "redirect:/student/registration";
        }

        Subject subject = subjectRepo.findById(s_id).get();
        model.addAttribute("teachers", subject.getTeachers());
        model.addAttribute("subject", subject);
        model.addAttribute("si", new SI());
        return "student/registration_more";
    }

    @PostMapping("/registration/{s_id}")
    public String wfcewd(@PathVariable("s_id") Long s_id,
                        Principal principal,
                        Model model,
                        @ModelAttribute("si") SI si)  {

        Subject subject = subjectRepo.findById(s_id).get();
        Teacher teacher = teacherService.findById(si.getId()).get();

        student = studentService.getByUsername(principal.getName());
        student.getStudent_subject().add(subject);
        teacher.getStudents().add(student);
        studentService.simple_save(student);
        teacherService.simple_save(teacher);


        return "redirect:/student/registration";
    }


    @GetMapping("/dashboard")
    public String sdkfdf(Principal principal,
                         Model model) {
        student = studentService.getByUsername(principal.getName());
        model.addAttribute("subjects", student.getStudent_subject());
        model.addAttribute("student", student);
        
        return "student/dashboard";
    }

    @GetMapping("/dashboard/{s_id}")
    public String esfv(@PathVariable("s_id") Long s_id,
                       Principal principal,
                       Model model) {
        student = studentService.getByUsername(principal.getName());
        Teacher teacher = teacherService.all().stream().filter(
                x -> x.getStudents().contains(student)).findAny().get();
        model.addAttribute("task_set", taskSetRepo.findAllByTeacherAndSubject(
                teacher.getId(), s_id
        ));

        return "/student/dashboard_info";
    }
    @GetMapping("/dashboard/{s_id}/{task_id}/do_task")
    public String sdvcs(@PathVariable("s_id") Long s_id,
                        @PathVariable("task_id") Long task_id,
                        Principal principal,
                        Model model) {
        student = studentService.getByUsername(principal.getName());
        TaskComplete taskComplete = taskCompleteRepo.findByTask_set_idAndStudent(
                task_id, student.getId());
        if (taskComplete == null) {
            taskComplete = new TaskComplete(
                    task_id ,
                    student.getId()
            );
        }

        System.out.println(taskComplete);


        model.addAttribute("task_set" , taskSetRepo.findById(task_id).get());

        model.addAttribute("taskComplete",taskComplete);


        return "/student/dashboard_more_more";
    }

    @PostMapping("/dashboard/{s_id}/{task_id}/do_task")
    public String werfe(@PathVariable("s_id") Long s_id,
                        @PathVariable("task_id") Long task_id,
                        @ModelAttribute("taskComplete") TaskComplete taskComplete,
                        Principal principal,
                        Model model) {

        student = studentService.getByUsername(principal.getName());

        taskComplete.setPassed_date(LocalDateTime.now());
        System.out.println(taskComplete);
        taskCompleteRepo.save(taskComplete);

        return "redirect:/student";
    }

    @GetMapping("/grades")
    public String sefce(Principal principal,
                        Model model) {

        student = studentService.getByUsername(principal.getName());
        model.addAttribute("subjects", student.getStudent_subject());
        
        return "student/grades";
    }

    @GetMapping("/grades/{s_id}")
    public String sfdcs(@PathVariable("s_id") Long s_id, 
                        Principal principal, 
                        Model model) {

        return "student/grades_more";
    }

    @PostMapping("/grades/{s_id}")
    public String sdfcdsf(@ModelAttribute("si") SI si,
                          @PathVariable("s_id") Long s_id,
                          Principal principal,
                          Model model) {
        
        return "student/grades_more_more";
    }



    @GetMapping("/feedback")
    public String sdfvds(Principal principal,
                         Model model) {

        model.addAttribute("feedback" , new Feedback());
        return "student/feedback";
    }

    @PostMapping("/feedback")
    public String sdfc(@ModelAttribute("feedback") Feedback feedback, Principal principal, Model model) {

        feedback.setUsername(principal.getName());
        feedbackRepo.save(feedback);
        model.addAttribute("feedback" , new Feedback("Thank for Feedback !"));

        return "student/feedback";
    }

    @GetMapping("/message")
    public String message(Principal principal,
                          Model model) {

        return "student/message";
    }


    @GetMapping("/notification")
    public String notification(Principal principal, Model model) {

        student = studentService.getByUsername(principal.getName());

        return "student/notification";
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class FN {
        private String file_name;
    }

    @Data @AllArgsConstructor @NoArgsConstructor
    private static class SI {
        private Long id;
    }

    @Data @AllArgsConstructor @NoArgsConstructor
    private static class PasswordChanger {
        @NotEmpty(message = "")
        private String old_password;
        private String new_password;
        @NotEmpty(message = "")
        private String repeat_password;
    }

}

