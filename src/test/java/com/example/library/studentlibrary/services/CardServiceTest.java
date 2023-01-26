package com.example.library.studentlibrary.services;

import com.example.library.studentlibrary.models.Author;
import com.example.library.studentlibrary.models.Card;
import com.example.library.studentlibrary.models.CardStatus;
import com.example.library.studentlibrary.models.Student;
import com.example.library.studentlibrary.repositories.AuthorRepository;
import com.example.library.studentlibrary.repositories.CardRepository;
import com.example.library.studentlibrary.repositories.StudentRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static com.example.library.studentlibrary.models.CardStatus.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CardServiceTest {
    @InjectMocks CardService cardService;

    @Mock
    CardRepository cardRepository;


    @InjectMocks AuthorService authorService;

    @Mock
    AuthorRepository authorRepository;

//    @InjectMocks StudentService studentService;
//
//    @Mock
//    CardService cardService;
//
//
//    @Mock
//    StudentRepository studentRepository;
//
//    List<Student> students = new ArrayList<>();


    Student student = new Student();
    Card card = new Card();
    @Before
    public void setUp(){
        student.setAge(23);
        student.setCountry("INDIA");
        student.setName("Accio");
        student.setEmailId("accio@gmail.com");
        doAnswer((test) -> {
            card.setCardStatus(DEACTIVATED);
            return null;
        }).when(cardRepository).deactivateCard(student.getId(),
                DEACTIVATED.toString());

    }

    @Test
    public void testCreateAndReturn(){
        Card card = cardService.createAndReturn(student);
        assert(card.getCardStatus() == ACTIVATED);
        assert(card.getStudent() == student);
    }

    @Test
    public void testDeactivateCard(){
        cardService.deactivateCard(student.getId());
        assert(card.getCardStatus() == DEACTIVATED);
    }

    @Test
    public void testCreate(){
        authorService.create(new Author("1", "1@gmail.com", 21, "India"));
    }

}
