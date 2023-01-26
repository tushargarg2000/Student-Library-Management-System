package com.example.library.studentlibrary.services;

import com.example.library.studentlibrary.models.Student;
import com.example.library.studentlibrary.repositories.StudentRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StudentServiceTest {
    @InjectMocks StudentService studentService;

    @Mock
    CardService cardService;
    @Mock
    StudentRepository studentRepository;

    List<Student> students = new ArrayList<>();

    @Before
    public void setUp(){
        Student student1 = new Student("1@gmail.com", "1", 1, "India");
        Student student2 = new Student("2@gmail.com", "2", 2, "USA");
        Student student3 = new Student("3@gmail.com", "3", 3, "UK");
        Student student4 = new Student("4@gmail.com", "4", 4, "China");

        students.add(student1);
        students.add(student2);
        students.add(student3);
        students.add(student4);

        when(studentRepository.findByEmailId(anyString())).thenReturn(student1);
        when(studentRepository.findById(anyInt())).thenReturn(Optional.of(student2));
    }

    @Test
    public void testGetDetails(){
        String email = "1@gmail.com";
        Student student = studentService.getDetailsByEmail(email);
        assert((student.getEmailId()=="1@gmail.com") && (student.getName()=="1") && (student.getAge()==1) && (student.getCountry()=="India"));
    }

    @Test
    public void testGetDetailsById(){
        Integer id = students.get(1).getId();
        Student student = studentService.getDetailsById(id);
        assert(student.getId()==id);
    }

    @Test
    public void testCreateStudent(){
        Student student = students.get(3);
        studentService.createStudent(student);
    }

    @Test
    public void testUpdateStudent(){
        Student student = students.get(2);
        studentService.updateStudent(student);
    }

    @Test
    public void testDeleteStudent(){
        Integer id = students.get(0).getId();
        studentService.deleteStudent(id);
    }
}
