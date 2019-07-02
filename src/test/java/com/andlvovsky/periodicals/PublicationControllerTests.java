package com.andlvovsky.periodicals;

import com.andlvovsky.periodicals.model.Publication;
import com.andlvovsky.periodicals.repository.PublicationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.allOf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PublicationControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PublicationRepository repository;

    Publication[] publications = {
            new Publication("The Guardian", 7, 10., "-"),
            new Publication("Daily Mail", 1, 5.5, "-"),
            new Publication("The Washington Post", 14, 17., "-"),
            new Publication("The Sun", 30, 20., "-"),
            new Publication("The Wall Street Journal", 7, 15., "-")
    };

    @Before
    public void defineRepositoryBehavior() {
        when(repository.findById(any(Long.class))).thenReturn(Optional.empty());
        when(repository.findById(1L)).thenReturn(Optional.of(publications[0]));
        when(repository.findById(2L)).thenReturn(Optional.of(publications[1]));
        when(repository.findById(3L)).thenReturn(Optional.of(publications[2]));
        when(repository.findAll()).thenReturn(Arrays.asList(publications[0], publications[1], publications[2]));
    }

    @Test
    public void testGetPublication() throws Exception {
        mvc.perform(get("/publications/1")).andExpect(status().isOk()).andDo(print())
            .andExpect(jsonPath("$.name").value("The Guardian"));
    }

    @Test
    public void testGetPublicationFail() throws Exception {
        mvc.perform(get("/publications/99")).andDo(print())
                .andExpect(content().string(allOf(Matchers.containsString("cannot find"),
                        Matchers.containsString("99"))));
    }

    @Test
    public void testGetAllPublications() throws Exception {
        mvc.perform(get("/publications/")).andDo(print())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[2].frequency").value(14));
    }

    @Test
    public void testAddPublication() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String publicationJson = mapper.writeValueAsString(publications[3]);
        mvc.perform(post("/publications/").content(publicationJson)
                .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk());
        verify(repository).save(publications[3]);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void testReplacePublication() throws Exception {
        when(repository.save(publications[4])).thenReturn(publications[4]);
        ObjectMapper mapper = new ObjectMapper();
        String publicationJson = mapper.writeValueAsString(publications[4]);
        mvc.perform(put("/publications/2").content(publicationJson)
                .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk());
        verify(repository).findById(2L);
        verify(repository).save(publications[4]);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void testDeletePublication() throws Exception {
        mvc.perform(delete("/publications/3")).andExpect(status().isOk());
        verify(repository).deleteById(3L);
    }

}
