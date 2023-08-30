package com.example.StudentAndTeacher.controllers;


import com.example.StudentAndTeacher.modelmapper.TeacherDTO;
import com.example.StudentAndTeacher.models.*;
import com.example.StudentAndTeacher.repo.*;
import com.example.StudentAndTeacher.services.StudentService;
import com.example.StudentAndTeacher.services.TeacherService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final SubjectRepo subjectRepo;
    private final MessageRepo messageRepo;
    private final TaskSetRepo taskSetRepo;
    private final TaskCompleteRepo taskCompleteRepo;
    private final JdbcTemplate jdbcTemplate;

    private final FeedbackRepo feedbackRepo;

    public TeacherController(StudentService studentService, TeacherService teacherService, SubjectRepo subjectRepo, MessageRepo messageRepo, TaskSetRepo taskSetRepo, TaskCompleteRepo taskCompleteRepo, JdbcTemplate jdbcTemplate, FeedbackRepo feedbackRepo) {
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.subjectRepo = subjectRepo;
        this.messageRepo = messageRepo;
        this.taskSetRepo = taskSetRepo;
        this.taskCompleteRepo = taskCompleteRepo;
        this.jdbcTemplate = jdbcTemplate;
        this.feedbackRepo = feedbackRepo;
    }

    private Teacher teacher;

    @GetMapping
    public String fvnw(Principal principal,
                       Model model) {
        teacher = teacherService.getByUsername(principal.getName());
        model.addAttribute("teacher", teacher);
        model.addAttribute("subjects", teacher.getSubjects());
        model.addAttribute("hasMessage", messageRepo.findByTo_username(principal.getName())
                .stream().filter(x -> !x.isViewed()).collect(Collectors.toSet()));

        return "teacher/page";
    }

    @GetMapping("/set_profile")
    public String dfv(Model model,
                      Principal principal) {
        teacher = teacherService.getByUsername(principal.getName());
        model.addAttribute("dto", new ModelMapper().map(teacher, TeacherDTO.class));
        model.addAttribute("password_changer", new PasswordChanger());
        model.addAttribute("fn", new FN(teacher.getFileName()));
        return "teacher/set_profile";
    }

    @PostMapping("/set_profile")
    public String fdeve(@ModelAttribute("fn") FN fn,
                        @ModelAttribute("dto") TeacherDTO teacherDTO,
                        @Valid @ModelAttribute("password_changer") PasswordChanger passwordChanger,
                        BindingResult password_result,
                        Model model,
                        Principal principal) {

        teacher = teacherService.getByUsername(principal.getName());

        if (passwordChanger.getNew_password() != null) {

            if (!teacherService.check(passwordChanger.getOld_password(), teacher.getPassword())) {
                password_result.rejectValue("old_password", "", "Old password doesn't match !");
            }

            if (!passwordChanger.getNew_password().equals(passwordChanger.getRepeat_password())) {
                password_result.rejectValue("repeat_password", "", "Repeat password doesn't match !");
            }

            if (password_result.hasErrors()) {
                model.addAttribute("dto", new ModelMapper().map(teacher, TeacherDTO.class));
                model.addAttribute("password_changer", passwordChanger);
                return "teacher/set_profile";
            }
            teacher.setPassword(passwordChanger.getNew_password());
            teacherService.save(teacher);
        }
        else if (teacherDTO.getEmail() != null) {
            dto(teacherDTO,principal);
        }
        else {
            teacher.setFileName(fn.getFile_name());
            teacherService.simple_save(teacher);
        }


        return "redirect:/teacher";
    }

    private void dto(TeacherDTO teacherDTO, Principal principal) {
        teacher = teacherService.getByUsername(principal.getName());
        teacher = new ModelMapper().map(teacherDTO, Teacher.class);
        teacherService.simple_save(teacher);
    }

    @GetMapping("/registration")
    public String registration(Principal principal,
                               Model model) {

        teacher = teacherService.getByUsername(principal.getName());
        model.addAttribute("subjects" , subjectRepo.all());
        model.addAttribute("si", new SI());
        model.addAttribute("teacher", teacher);
        return "teacher/registration";
    }

    @GetMapping("registration/agreement")
    public String sfesfdc(@ModelAttribute("si") SI si,
                          Principal principal,
                          Model model) {

        teacher = teacherService.getByUsername(principal.getName());
        model.addAttribute("si", new SI(si.getId()));
        System.out.println(si);
        return "teacher/sub_reg_detail";
    }

    @PostMapping("registration/agreement")
    public String dfvfed(@ModelAttribute("si") SI si,Principal principal, Model model) {
        System.out.println(si);
        teacher = teacherService.getByUsername(principal.getName());
        Subject subject = subjectRepo.findById(si.getId()).get();
        teacher.getSubjects().add(subject);
        teacherService.simple_save(teacher);
        return "redirect:/teacher";
    }

    @GetMapping("/dashboard")
    public String sdkfdf(@RequestParam(name = "id", required = false) Long id,
                         Principal principal,
                         Model model) {

        if (id != null) {
            model.addAttribute("subject" , subjectRepo.findById(id).get());
            model.addAttribute("task_set" , new TaskSet());
            model.addAttribute("si" , new SI(id));
            model.addAttribute("tak_set", taskSetRepo.findAllByTeacherAndSubject(
                    teacher.getId(), id));
            return "teacher/dashboard_info";
        }

        teacher = teacherService.getByUsername(principal.getName());
        model.addAttribute("subjects" , teacher.getSubjects());
        model.addAttribute("si" , new SI());
        return "teacher/dashboard";
    }



    @PostMapping("/dashboard")
    public String esfv(@ModelAttribute("task_set") TaskSet taskSet,
                       Principal principal) {

        taskSet.setId(null);
        taskSet.setTeacher_id(teacherService.getByUsername(principal.getName()).getId());
        taskSet.setPosted_date(LocalDateTime.now());
        System.out.println(taskSet);
        taskSetRepo.save(taskSet);

        teacher = teacherService.getByUsername(principal.getName());

        Set<Student> students = teacher.getStudents();

        for (Student s : students) {
            if (s.getStudent_subject().stream().anyMatch(x ->
                    x.getId().equals(taskSet.getSubject_id()))) {
                taskCompleteRepo.save(new TaskComplete(
                        taskSet.getId(), s.getId()
                ));
            }
        }


        System.out.println(students);

        return "redirect:/teacher";
    }

    @GetMapping("/grades")
    public String sefce(Principal principal,
                        Model model) {
        teacher = teacherService.getByUsername(principal.getName());
        model.addAttribute("subjects" , teacher.getSubjects());
        return "teacher/grades";
    }

    @GetMapping("/grades/{s_id}")
    public String sfdcs(@PathVariable("s_id") Long s_id, Principal principal, Model model) {

        model.addAttribute("task_set" , taskSetRepo.findAllByTeacherAndSubject(
                teacher.getId(), s_id).stream().filter(x -> x.getDeadline() != null).
                collect(Collectors.toSet())
        );

        model.addAttribute("subject" , subjectRepo.findById(s_id).get());
        model.addAttribute("si", new SI());
        return "teacher/grades_more";
    }
    @PostMapping("/grades/{s_id}")
    public String sdfcdsf(@ModelAttribute("si") SI si,
                          @PathVariable("s_id") Long s_id,
                          Principal principal,
                          Model model) {

        List<TaskComplete> taskCompletes = taskCompleteRepo.findAllByTask_set_id(si.getId()).
                stream().toList();

        TaskCompletesWrapper taskCompletesWrapper = new TaskCompletesWrapper(taskCompletes);

        model.addAttribute("students" , teacher.getStudents());
        model.addAttribute("task_set", taskSetRepo.findById(si.getId()).get());
        model.addAttribute("taskCompletesWrapper",taskCompletesWrapper);

        Map<Long, String> map_student = new HashMap<>();
        studentService.all().stream().forEach(
                x -> map_student.put(x.getId(), x.getFull_name())
        );

        model.addAttribute("students", map_student);

        return "teacher/grades_more_more";
    }

    @PostMapping("/grades/{s_id}/{task_id}")
    public String sdfcdsf(@ModelAttribute("taskCompletesWrapper") TaskCompletesWrapper taskCompletesWrapper) {

        taskCompletesWrapper.getTaskCompletes().forEach(x -> taskCompleteRepo.save(x));

        return "redirect:/teacher";
    }



    @GetMapping("/feedback")
    public String sdfvds(Principal principal,
                         Model model) {

        model.addAttribute("feedback" , new Feedback());
        return "teacher/feedback";
    }

    @PostMapping("/feedback")
    public String sdfc(@ModelAttribute("feedback") Feedback feedback, Principal principal, Model model) {

        feedback.setUsername(principal.getName());
        feedbackRepo.save(feedback);
        model.addAttribute("feedback" , new Feedback("Thank for Feedback !"));

        return "teacher/feedback";
    }

    @GetMapping("/message")
    public String message(Principal principal,
                          Model model) {

        model.addAttribute("my_messages" , messageRepo.findByFrom_username(principal.getName()));
        model.addAttribute("from_messages" , messageRepo.findByTo_username(principal.getName()));
        model.addAttribute("send_message", new Message());
        model.addAttribute("send_message_error" , false);


        Set<Message> messages = messageRepo.findByTo_username(principal.getName());
        messages.stream().forEach(x -> x.setViewed(true));
        messages.stream().forEach(x -> messageRepo.save(x));

        return "teacher/message";
    }

    @PostMapping("/message")
    public String dfvdfe(@Valid @ModelAttribute("send_message") Message message,
                         BindingResult result,
                         Principal principal, Model model) {

        if (teacherService.getByUsername(message.getTo_username()) == null) {
            result.rejectValue("to_username", "", "User is not found !");
        }

        if (result.hasErrors()) {
            model.addAttribute("send_message", message);
            model.addAttribute("send_message_error" , true);
            return "/teacher/message";
        }

        message.setPosted_date(LocalDateTime.now());
        message.setFrom_username(principal.getName());
        messageRepo.save(message);


        return "redirect:/teacher/message";
    }


    @GetMapping("/notification")
    public String notification(Principal principal, Model model) {

        teacher = teacherService.getByUsername(principal.getName());

        return "teacher/notification";
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

@Data @AllArgsConstructor
class TaskCompletesWrapper {
    private List<TaskComplete> taskCompletes;

    public List<TaskComplete> getTaskCompletes() {
        return taskCompletes;
    }

    public void setTaskCompletes(List<TaskComplete> taskCompletes) {
        this.taskCompletes = taskCompletes;
    }
}


