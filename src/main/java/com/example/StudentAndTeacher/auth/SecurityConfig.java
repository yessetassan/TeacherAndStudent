package com.example.StudentAndTeacher.auth;

import com.example.StudentAndTeacher.services.StudentService;
import com.example.StudentAndTeacher.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;
    private final AppUserService appUserService;
    private final StudentService studentService;
    private final TeacherService teacherService;

    @Autowired
    public SecurityConfig(AppUserService appUserService, StudentService studentService, TeacherService teacherService) {
        this.appUserService = appUserService;
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/Pictures/**").permitAll()
                .antMatchers( "/registration/student","/registration/teacher").permitAll()
                .anyRequest().authenticated()
                .and().formLogin()
                .loginPage("/login").successHandler(new CustomSuccessHandler(studentService, teacherService)).permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login")  // Redirect to home page after logout
                .invalidateHttpSession(true)  // Invalidate session
                .clearAuthentication(true)  // Clear the authentication
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(appUserService).passwordEncoder(passwordEncoder);
    }

}
