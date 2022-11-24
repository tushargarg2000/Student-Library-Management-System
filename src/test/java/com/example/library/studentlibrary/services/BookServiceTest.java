package com.example.library.studentlibrary.services;

import com.example.library.studentlibrary.models.Author;
import com.example.library.studentlibrary.models.Book;
import com.example.library.studentlibrary.models.Genre;
import com.example.library.studentlibrary.repositories.BookRepository;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceTest {
    @InjectMocks BookService bookService;

    @Mock
    BookRepository bookRepository;

    private List<Book> bookList = new ArrayList<>();

    @Before
    public void setUp(){
        Author author1 = new Author("1", "1@gmail.com", 23, "INDIA");
        Author author2 = new Author("2", "2@gmail.com", 47, "USA");

        Book book1 = new Book("A", Genre.FICTIONAL, author1);
        Book book2 = new Book("B", Genre.PHYSICS, author1);
        Book book3 = new Book("C", Genre.PHYSICS, author1);
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
        assert(res.size() == 3);
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
}
