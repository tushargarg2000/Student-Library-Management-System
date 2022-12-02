package com.example.library.studentlibrary.controller;

import com.example.library.studentlibrary.models.Student;
import com.example.library.studentlibrary.security.AuthorityConstants;
import com.example.library.studentlibrary.security.User;
import com.example.library.studentlibrary.security.UserRepository;
import com.example.library.studentlibrary.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    StudentService studentService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/studentByEmail")
    public ResponseEntity getStudentByEmail(@RequestParam("email") String email){
        Student obj = studentService.getDetailsByEmail(email);
        return new ResponseEntity<>("Student details - " + obj, HttpStatus.OK);
    }

    @GetMapping("/studentById")
    public ResponseEntity getStudentById(@RequestParam("id") int id){
        Student obj = studentService.getDetailsById(id);
        return new ResponseEntity<>("Student details - " + obj, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity createStudent(@RequestBody Student student){

        studentService.createStudent(student);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user =  User.builder()
                .username(student.getEmailId())
                .password(encoder.encode("pass1234"))
                .authority(AuthorityConstants.STUDENT_AUTHORITY)
                .build();
        userRepository.save(user);

        return new ResponseEntity<>("the student is successfully added to the system", HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity updateStudent(@RequestBody Student student){

        studentService.updateStudent(student);
        return new ResponseEntity<>("student is updated", HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/")
    public ResponseEntity deleteStudent(@RequestParam("id") int id){
        studentService.deleteStudent(id);
        return new ResponseEntity<>("student is deleted", HttpStatus.ACCEPTED);
    }

}
