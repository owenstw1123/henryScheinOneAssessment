package com.assessment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookController bookController;

    @Test
    public void all() throws Exception{
        Book testBook1 = new Book("Test Book 1","Test Author");
        Book testBook2 = new Book("Test Book 2","Test Author Jr");

        List<Book> allBooks = new ArrayList<Book>();
        allBooks.add(testBook1);
        allBooks.add(testBook2);

        given(bookController.all()).willReturn(allBooks);

        mvc.perform(get("/books")
                        .contentType(APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(2)))
                        .andExpect(jsonPath("$[0].title", is(testBook1.getTitle())))
                        .andExpect(jsonPath("$[0].author", is(testBook1.getAuthor())))
                        .andExpect(jsonPath("$[1].title", is(testBook2.getTitle())))
                        .andExpect(jsonPath("$[1].author", is(testBook2.getAuthor())));
    }

    @Test
    public void newBook() throws Exception{
        Book testBook1 = new Book("Test Book 1","Test Author");
        ObjectMapper mapper = new ObjectMapper();
        String jsonBook1 = mapper.writeValueAsString(testBook1);

        given(bookController.newBook(testBook1)).willReturn(testBook1);

        mvc.perform(post("/books")
                        .content(jsonBook1)
                        .contentType(APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.title", is(testBook1.getTitle())))
                        .andExpect(jsonPath("$.author", is(testBook1.getAuthor())));
    }

    @Test
    public void one() throws Exception{
        Book testBook1 = new Book("Test Book 1","Test Author");
        testBook1.setId((long) 1);

        given(bookController.one(testBook1.getId())).willReturn(testBook1);

        mvc.perform(get("/books/" + testBook1.getId() )
                        .contentType(APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.title", is(testBook1.getTitle())))
                        .andExpect(jsonPath("$.author", is(testBook1.getAuthor())));
    }

    @Test
    public void one_BookDoesNotExistThrowsException() throws Exception{
        Book testBook1 = new Book("Test Book 1","Test Author");
        testBook1.setId((long) 1);
        Long wrongId = testBook1.getId() + 1;

        given(bookController.one(wrongId)).willThrow( new BookNotFoundException(wrongId));

        mvc.perform(get("/books/" + wrongId )
                        .contentType(APPLICATION_JSON))
                        .andExpect(status().is4xxClientError())
                        .andExpect(content().string("Could not find book " + wrongId));
    }
}