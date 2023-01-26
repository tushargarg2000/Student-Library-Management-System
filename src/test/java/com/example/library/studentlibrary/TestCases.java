package com.example.library.studentlibrary;

import com.example.library.studentlibrary.models.*;
import com.example.library.studentlibrary.repositories.*;
import com.example.library.studentlibrary.services.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static com.example.library.studentlibrary.models.CardStatus.ACTIVATED;
import static com.example.library.studentlibrary.models.CardStatus.DEACTIVATED;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestCases {
    @InjectMocks
    AuthorService authorService;

    @InjectMocks
    BookService bookService;

    @InjectMocks
    CardService cardService;

    @InjectMocks
    StudentService studentService;

    @InjectMocks
    TransactionService transactionService;

    @Mock
    BookRepository bookRepository;

    @Mock
    CardRepository cardRepository;

    @Mock
    TransactionRepository transactionRepository;

    @Captor
    ArgumentCaptor<Integer> fineCaptor;

    @Mock
    CardService cardService1;
    @Mock
    StudentRepository studentRepository;


    @Mock
    AuthorRepository authorRepository;

    private List<Book> bookList = new ArrayList<>();

    Student student = new Student();
    Card card = new Card();

    List<Student> students = new ArrayList<>();
    Book book3;

    @Before
    public void setUp(){

        Author author1 = new Author("1", "1@gmail.com", 23, "INDIA");
        Author author2 = new Author("2", "2@gmail.com", 47, "USA");

        Book book1 = new Book("A", Genre.FICTIONAL, author1);
        Book book2 = new Book("B", Genre.PHYSICS, author1);
        book3 = new Book("C", Genre.PHYSICS, author1);
        Book book4 = new Book("D", Genre.PHYSICS, author2);
        Book book5 = new Book("E", Genre.BOTANY, author2);
        Book book6 = new Book("F", Genre.FICTIONAL, author2);

        book1.setAvailable(Boolean.FALSE);
        book6.setAvailable(Boolean.FALSE);

        bookList.add(book1);
        bookList.add(book2);
        bookList.add(book3);
        bookList.add(book4);
        bookList.add(book5);
        bookList.add(book6);

        List<Book> expectedNoInputNull = new ArrayList<>();
        for(Book book: bookList){
            if((book.isAvailable() == Boolean.TRUE) && (book.getGenre() == Genre.PHYSICS) && (book.getAuthor().getName() == "1")){
                expectedNoInputNull.add(book);
            }
        }

        List<Book> expectedAuthorNull = new ArrayList<>();
        for(Book book: bookList){
            if((book.isAvailable() == Boolean.TRUE) && (book.getGenre() == Genre.PHYSICS)){
                expectedAuthorNull.add(book);
            }
        }

        List<Book> expectedGenreNull = new ArrayList<>();
        for(Book book: bookList){
            if((book.isAvailable() == Boolean.TRUE) && (book.getAuthor().getName() == "2")){
                expectedGenreNull.add(book);
            }
        }

        List<Book> expectedNotAvailable = new ArrayList<>();
        for(Book book: bookList){
            if(book.isAvailable() == Boolean.FALSE){
                expectedNotAvailable.add(book);
            }
        }

        when(bookRepository.findBooksByGenreAuthor(anyString(), anyString(), anyBoolean())).thenReturn(expectedNoInputNull);
        when(bookRepository.findBooksByAuthor(anyString(), anyBoolean())).thenReturn(expectedGenreNull);
        when(bookRepository.findBooksByGenre(anyString(), anyBoolean())).thenReturn(expectedAuthorNull);
        when(bookRepository.findByAvailability(Boolean.FALSE)).thenReturn(expectedNotAvailable);

        student.setAge(23);
        student.setCountry("INDIA");
        student.setName("Accio");
        student.setEmailId("accio@gmail.com");
        doAnswer((test) -> {
            card.setCardStatus(DEACTIVATED);
            return null;
        }).when(cardRepository).deactivateCard(student.getId(),
                DEACTIVATED.toString());

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

        transactionService.max_allowed_books = 1;
        transactionService.getMax_allowed_days = 3;
        transactionService.fine_per_day = 5;

        book2.setAvailable(Boolean.FALSE);

        Card card1 = new Card();
        card1.setBooks(new ArrayList<>());
        Card card2 = new Card();
        card2.setCardStatus(CardStatus.DEACTIVATED);
        Card card3 = new Card();
        List<Book> booksOnCard3 = new ArrayList<>();
        booksOnCard3.add(book3);
        card3.setBooks(booksOnCard3);

        when(bookRepository.findById(1)).thenReturn(Optional.of(book1));
        when(bookRepository.findById(2)).thenReturn(Optional.of(book2));


        when(cardRepository.findById(1)).thenReturn(Optional.of(card1));
        when(cardRepository.findById(2)).thenReturn(Optional.of(card2));
        when(cardRepository.findById(3)).thenReturn(Optional.of(card3));
        //when(cardRepository.findById(4)).thenReturn(null);

        Transaction transaction = new Transaction();
        transaction.setBook(book1);
        transaction.setCard(card1);
        transaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        transaction.setIssueOperation(Boolean.TRUE);
        Integer daysAfterTransaction = 5;
        Integer millisAfterTransaction = daysAfterTransaction * 86400000;
        transaction.setTransactionDate(new Date(System.currentTimeMillis() - millisAfterTransaction));

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        when(transactionRepository.find(1, 1, TransactionStatus.SUCCESSFUL, true)).thenReturn(transactions);
    }

    @Test
    public void testCreateBook(){
        bookService.createBook(bookList.get(0));
    }

    @Test
    public void testGetBooksWhenNoInputNull(){
        HashSet<Book> res = new HashSet<>(bookService.getBooks("PHYSICS", Boolean.TRUE, "1"));
        assert(res.size() == 2);
        for(Book book: res){
            assert((book.isAvailable() == Boolean.TRUE) && (book.getGenre() == Genre.PHYSICS) && (book.getAuthor().getName() == "1"));
        }
    }

    @Test
    public void testGetBooksWhenGenreNull(){
        HashSet<Book> res = new HashSet<>(bookService.getBooks(null, Boolean.TRUE, "2"));
        assert(res.size() == 2);
        for(Book book: res){
            assert((book.isAvailable() == Boolean.TRUE) && (book.getAuthor().getName() == "2"));
        }
    }

    @Test
    public void testGetBooksWhenAuthorNull(){
        HashSet<Book> res = new HashSet<>(bookService.getBooks("PHYSICS", Boolean.TRUE, null));
        assertEquals(res.size(), 3);
        for(Book book: res){
            assert((book.isAvailable() == Boolean.TRUE) && (book.getGenre() == Genre.PHYSICS));
        }
    }

    @Test
    public void testGetBooksWhenNotAvailable(){
        HashSet<Book> res = new HashSet<>(bookService.getBooks(null, Boolean.FALSE, null));
        assert(res.size() == 2);
        for(Book book: res){
            assert(book.isAvailable() == Boolean.FALSE);
        }
    }

    @Test
    public void testCreate(){
        authorService.create(new Author("1", "1@gmail.com", 21, "India"));
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

    @Test
    public void testIssueBookWhenBookNotAvailable(){
        try{
            transactionService.issueBook(1, 2);
        }
        catch (Exception e){
            assert(e.getMessage().equals("Book is either unavailable or not present"));
        }
    }

    @Test
    public void testIssueBookWhenCardNotActivated(){
        try{
            when(bookRepository.findById(3)).thenReturn(Optional.of(book3));
            transactionService.issueBook(2, 3);
        }
        catch (Exception e){
            assert(e.getMessage().equals("Card is invalid"));
        }
    }

    @Test
    public void testIssueBookWhenCardLimitExceeds(){
        try{
            when(bookRepository.findById(3)).thenReturn(Optional.of(book3));
            transactionService.issueBook(3, 3);
        }
        catch (Exception e){
            assert(e.getMessage().equals("Book limit has reached for this card"));
        }
    }

    @Test
    public void testIssueBookWhenTransactionSuccessful(){
        String transactionId = null;
        try{
            when(bookRepository.findById(3)).thenReturn(Optional.of(book3));
            transactionId = transactionService.issueBook(1, 3);
        }
        catch (Exception e){
            assert (e == null);
        }
        assertNotNull(transactionId);
    }

    @Test
    public void testReturnBook(){
        Transaction transaction = null;
        try{
            transaction = transactionService.returnBook(1, 1);
        }
        catch (Exception e){
            assert (e == null);
        }
        assertNotNull(transaction);
        assert(transaction.getFineAmount()==10);
    }
}
