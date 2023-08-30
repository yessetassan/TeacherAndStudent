    package com.example.StudentAndTeacher.models;


    import com.fasterxml.jackson.annotation.JsonBackReference;
    import com.fasterxml.jackson.annotation.JsonIgnore;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import javax.persistence.*;
    import java.io.Serializable;
    import java.util.HashSet;
    import java.util.Set;

    @Entity
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public class Subject implements Serializable {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String full_name;
        @Column(name = "picture")
        private String picture;

        @ManyToMany(mappedBy = "subjects", fetch = FetchType.EAGER)
        @JsonBackReference
        private Set<Teacher> teachers = new HashSet<>();

        @ManyToMany(mappedBy = "student_subject", fetch = FetchType.EAGER)
        @JsonBackReference
        private Set<Student> students = new HashSet<>();



        public int hashCode() {
            return Integer.parseInt(id+"");
        }

        @Override
        public String toString() {
            return "subject{}";
        }

    }
