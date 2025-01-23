package com.assessment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(BorrowerController.class)
public class BorrowerControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BorrowerController borrowerController;

    @Test
    public void all() throws Exception{
        Borrower testBorrower1 = new Borrower("Tester McTest");
        Borrower testBorrower2 = new Borrower("Retset McTest");

        List<Borrower> allBorrowers = new ArrayList<Borrower>();
        allBorrowers.add(testBorrower1);
        allBorrowers.add(testBorrower2);

        given(borrowerController.all()).willReturn(allBorrowers);

        mvc.perform(get("/borrowers")
                        .contentType(APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(2)))
                        .andExpect(jsonPath("$[0].name", is(testBorrower1.getName())))
                        .andExpect(jsonPath("$[1].name", is(testBorrower2.getName())));
    }

    @Test
    public void newBorrower() throws Exception{
        Borrower testBorrower1 = new Borrower("Test Person");
        ObjectMapper mapper = new ObjectMapper();
        String jsonBorrower1 = mapper.writeValueAsString(testBorrower1);

        given(borrowerController.newBorrower(testBorrower1)).willReturn(testBorrower1);

        mvc.perform(post("/borrowers")
                        .content(jsonBorrower1)
                        .contentType(APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.name", is(testBorrower1.getName())));
    }

    @Test
    public void one() throws Exception{
        Borrower testBorrower1 = new Borrower("Test Person");
        testBorrower1.setId((long) 1);

        given(borrowerController.one(testBorrower1.getId())).willReturn(testBorrower1);

        mvc.perform(get("/borrowers/" + testBorrower1.getId() )
                        .contentType(APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.name", is(testBorrower1.getName())));
    }

    @Test
    public void one_BorrowerDoesNotExistThrowsException() throws Exception{
        Borrower testBorrower1 = new Borrower("Test Person");
        testBorrower1.setId((long) 1);
        Long wrongId = testBorrower1.getId() + 1;

        given(borrowerController.one(wrongId)).willThrow( new BorrowerNotFoundException(wrongId));

        mvc.perform(get("/borrowers/" + wrongId )
                        .contentType(APPLICATION_JSON))
                        .andExpect(status().is4xxClientError())
                        .andExpect(content().string("Could not find borrower " + wrongId));
    }
}