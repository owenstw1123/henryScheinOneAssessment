package com.assessment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(BorrowingController.class)
public class BorrowingControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BorrowingController borrowingController;

    @Test
    public void all() throws Exception{

        Book testBook1 = new Book("Test Book 1","Test Author");
        Book testBook2 = new Book("Test Book 2","Test Author Jr");
        testBook1.setId((long) 1);
        testBook2.setId((long) 2);

        Borrower testBorrower1 = new Borrower("Tester McTest");
        Borrower testBorrower2 = new Borrower("Retset McTest");
        testBorrower1.setId((long) 1);
        testBorrower2.setId((long) 2);

        Borrowing testBorrowing1 = new Borrowing(testBook1.getId(), testBorrower1.getId());
        Borrowing testBorrowing2 = new Borrowing(testBook2.getId(), testBorrower2.getId());

        List<Borrowing> allBorrowings = new ArrayList<Borrowing>();
        allBorrowings.add(testBorrowing1);
        allBorrowings.add(testBorrowing2);

        given(borrowingController.all()).willReturn(allBorrowings);

        mvc.perform(get("/borrowings")
                        .contentType(APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(2)))
                        .andExpect(jsonPath("$[0].bookId", is(testBorrowing1.getBookId().intValue())))
                        .andExpect(jsonPath("$[0].borrowerId", is(testBorrowing1.getBorrowerId().intValue())))
                        .andExpect(jsonPath("$[1].bookId", is(testBorrowing2.getBookId().intValue())))
                        .andExpect(jsonPath("$[1].borrowerId", is(testBorrowing2.getBorrowerId().intValue())));
    }

    @Test
    public void allBooksByBorrower() throws Exception {
        Book testBook1 = new Book("Test Book 1","Test Author");
        Book testBook3 = new Book("Test Book 3","Test Author III Esq");
        testBook1.setId((long) 1);
        testBook3.setId((long) 3);

        Borrower testBorrower1 = new Borrower("Tester McTest");
        testBorrower1.setId((long) 1);

        Borrowing testBorrowing1 = new Borrowing(testBook1.getId(), testBorrower1.getId());
        Borrowing testBorrowing3 = new Borrowing(testBook3.getId(), testBorrower1.getId());

        List<Borrowing> allBorrowings = new ArrayList<Borrowing>();
        allBorrowings.add(testBorrowing1);
        allBorrowings.add(testBorrowing3);

        given(borrowingController.allBooksByBorrower(testBorrower1.getId())).willReturn(allBorrowings);

        mvc.perform(get("/borrowings/borrower_id/" + testBorrower1.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].bookId", is(testBorrowing1.getBookId().intValue())))
                .andExpect(jsonPath("$[0].borrowerId", is(testBorrowing1.getBorrowerId().intValue())))
                .andExpect(jsonPath("$[1].bookId", is(testBorrowing3.getBookId().intValue())))
                .andExpect(jsonPath("$[1].borrowerId", is(testBorrowing3.getBorrowerId().intValue())));


    }

    @Test
    public void allBooksByBorrower_BorrowerDoesNotExistThrowsException() throws Exception {

        Borrower testBorrower1 = new Borrower("Tester McTest");
        testBorrower1.setId((long) 1);
        Long wrongId = testBorrower1.getId() + 1;

        given(borrowingController.allBooksByBorrower(wrongId)).willThrow(new BorrowerNotFoundException(wrongId));

        mvc.perform(get("/borrowings/borrower_id/" + wrongId)
                        .contentType(APPLICATION_JSON))
                        .andExpect(status().is4xxClientError())
                        .andExpect(content().string("Could not find borrower " + wrongId));
    }

    @Test
    public void borrowBook() throws Exception{

        Book testBook1 = new Book("Test Book 1","Test Author");
        testBook1.setId((long) 1);

        Borrower testBorrower1 = new Borrower("Tester McTest");
        testBorrower1.setId((long) 1);

        Borrowing testBorrowing1 = new Borrowing(testBook1.getId(), testBorrower1.getId());
        ObjectMapper mapper = new ObjectMapper();
        String jsonBorrowing1 = mapper.writeValueAsString(testBorrowing1);

        given(borrowingController.borrowBook(testBook1.getId(), testBorrower1.getId())).willReturn(testBorrowing1);

        mvc.perform(get("/borrowings/borrow/book_id/" + testBook1.getId() + "/borrower_id/" + testBorrower1.getId())
                        .content(jsonBorrowing1)
                        .contentType(APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.bookId", is(testBorrowing1.getBookId().intValue())))
                        .andExpect(jsonPath("$.borrowerId", is(testBorrowing1.getBorrowerId().intValue())));
    }

    @Test
    public void borrowBook_BookDoesNotExistThrowsException() throws Exception{
        Book testBook1 = new Book("Test Book 1","Test Author");
        testBook1.setId((long) 1);
        Long wrongId = testBook1.getId() + 1;
        Borrower testBorrower1 = new Borrower("Tester McTest");
        testBorrower1.setId((long) 1);

        given(borrowingController.borrowBook(wrongId, testBorrower1.getId())).willThrow(new BookNotFoundException(wrongId));

        mvc.perform(get("/borrowings/borrow/book_id/" + wrongId + "/borrower_id/" + testBorrower1.getId())
                        .contentType(APPLICATION_JSON))
                        .andExpect(status().is4xxClientError())
                        .andExpect(content().string("Could not find book " + wrongId));
    }

    @Test
    public void borrowBook_BorrowerDoesNotExistThrowsException() throws Exception{
        Book testBook1 = new Book("Test Book 1","Test Author");
        testBook1.setId((long) 1);
        Borrower testBorrower1 = new Borrower("Tester McTest");
        testBorrower1.setId((long) 1);
        Long wrongId = testBorrower1.getId() + 1;

        given(borrowingController.borrowBook(testBook1.getId(), wrongId)).willThrow(new BorrowerNotFoundException(wrongId));

        mvc.perform(get("/borrowings/borrow/book_id/" + testBook1.getId() + "/borrower_id/" + wrongId)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Could not find borrower " + wrongId));
    }

    @Test
    public void borrowBook_BorrowingNotAvailableThrowsException() throws Exception{
        Book testBook1 = new Book("Test Book 1","Test Author");
        testBook1.setId((long) 1);
        Borrower testBorrower1 = new Borrower("Tester McTest");
        testBorrower1.setId((long) 1);

        given(borrowingController.borrowBook(testBook1.getId(), testBorrower1.getId())).willThrow(new BorrowingNotAvailableException(testBook1.getId()));

        mvc.perform(get("/borrowings/borrow/book_id/" + testBook1.getId() + "/borrower_id/" + testBorrower1.getId())
                        .contentType(APPLICATION_JSON))
                        .andExpect(status().is4xxClientError())
                        .andExpect(content().string("Book " + testBook1.getId() + "is not currently available to check out."));
    }

    @Test
    public void returnBook() throws Exception{

        Book testBook1 = new Book("Test Book 1","Test Author");
        testBook1.setId((long) 1);

        Borrower testBorrower1 = new Borrower("Tester McTest");
        testBorrower1.setId((long) 1);

        Borrowing testBorrowing1 = new Borrowing(testBook1.getId(), testBorrower1.getId());
        ObjectMapper mapper = new ObjectMapper();
        String jsonBorrowing1 = mapper.writeValueAsString(testBorrowing1);

        given(borrowingController.returnBook(testBook1.getId(), testBorrower1.getId())).willReturn(testBorrowing1);

        mvc.perform(get("/borrowings/return/book_id/" + testBook1.getId() + "/borrower_id/" + testBorrower1.getId())
                        .content(jsonBorrowing1)
                        .contentType(APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.bookId", is(testBorrowing1.getBookId().intValue())))
                        .andExpect(jsonPath("$.borrowerId", is(testBorrowing1.getBorrowerId().intValue())));
    }

    @Test
    public void returnBook_BorowingDoesNotExistThrowsException() throws Exception{

        Book testBook1 = new Book("Test Book 1","Test Author");
        testBook1.setId((long) 1);

        Borrower testBorrower1 = new Borrower("Tester McTest");
        testBorrower1.setId((long) 1);
        Long wrongId = testBorrower1.getId() + 1;

        Borrowing testBorrowing1 = new Borrowing(testBook1.getId(), testBorrower1.getId());
        ObjectMapper mapper = new ObjectMapper();
        String jsonBorrowing1 = mapper.writeValueAsString(testBorrowing1);

        given(borrowingController.returnBook(testBook1.getId(), wrongId)).willThrow(new BorrowingNotFoundException(testBook1.getId(), wrongId));

        mvc.perform(get("/borrowings/return/book_id/" + testBook1.getId() + "/borrower_id/" + wrongId)
                        .content(jsonBorrowing1)
                        .contentType(APPLICATION_JSON))
                        .andExpect(status().is4xxClientError())
                        .andExpect(content().string("Could not find borrowing with book_id " + testBook1.getId() + "and borrower_id " + wrongId));
    }
}