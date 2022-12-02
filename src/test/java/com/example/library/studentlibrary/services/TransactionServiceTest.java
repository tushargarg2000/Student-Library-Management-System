package com.example.library.studentlibrary.services;

import com.example.library.studentlibrary.models.*;
import com.example.library.studentlibrary.repositories.BookRepository;
import com.example.library.studentlibrary.repositories.CardRepository;
import com.example.library.studentlibrary.repositories.TransactionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {
    @InjectMocks TransactionService transactionService;

    @Mock
    BookRepository bookRepository;

    @Mock
    CardRepository cardRepository;

    @Mock
    TransactionRepository transactionRepository;

    @Captor
    ArgumentCaptor<Integer> fineCaptor;

    @Before
    public void setUp(){

        transactionService.max_allowed_books = 1;
        transactionService.getMax_allowed_days = 3;
        transactionService.fine_per_day = 5;

        Author author1 = new Author("1", "1@gmail.com", 23, "INDIA");
        Author author2 = new Author("2", "2@gmail.com", 47, "USA");

        Book book1 = new Book("A", Genre.FICTIONAL, author1);
        Book book2 = new Book("B", Genre.PHYSICS, author1);
        Book book3 = new Book("C", Genre.PHYSICS, author2);

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
        //when(bookRepository.findById(3)).thenReturn(null);

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
            transactionService.issueBook(2, 1);
        }
        catch (Exception e){
            assert(e.getMessage().equals("Card is invalid"));
        }
    }

    @Test
    public void testIssueBookWhenCardLimitExceeds(){
        try{
            transactionService.issueBook(3, 1);
        }
        catch (Exception e){
            assert(e.getMessage().equals("Book limit has reached for this card"));
        }
    }

    @Test
    public void testIssueBookWhenTransactionSuccessful(){
        String transactionId = null;
        try{
            transactionId = transactionService.issueBook(1, 1);
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
