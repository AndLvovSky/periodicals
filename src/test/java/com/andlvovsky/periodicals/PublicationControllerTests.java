package com.andlvovsky.periodicals;

import com.andlvovsky.periodicals.exception.PublicationNotFoundException;
import com.andlvovsky.periodicals.model.publication.Publication;
import com.andlvovsky.periodicals.model.publication.PublicationDto;
import com.andlvovsky.periodicals.model.publication.PublicationMapper;
import com.andlvovsky.periodicals.service.PublicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.allOf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PublicationControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PublicationService service;

    private PublicationMapper publicationDtoMapper = Mappers.getMapper(PublicationMapper.class);

    private Publication[] publications = {
            new Publication("The Guardian", 7, 10., "-"),
            new Publication("Daily Mail", 1, 5.5, "-"),
            new Publication("The Washington Post", 14, 17., "-"),
            new Publication("The Sun", 30, 20., "-"),
            new Publication("The Wall Street Journal", 7, 15., "-"),
            new Publication("The New Yorker", -5, 10., "-")
    };

    @Before
    public void defineRepositoryBehavior() {
        when(service.getOne(1L)).thenReturn(publications[0]);
        when(service.getOne(2L)).thenReturn(publications[1]);
        when(service.getOne(3L)).thenReturn(publications[2]);
        when(service.getAll()).thenReturn(Arrays.asList(publications[0], publications[1], publications[2]));
    }

    @Test
    public void getsOne() throws Exception {
        mvc.perform(get("/publications/1")).andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.name").value("The Guardian"));
    }

    @Test
    public void getsOneFail() throws Exception {
        when(service.getOne(99L)).thenThrow(new PublicationNotFoundException(99L));
        mvc.perform(get("/publications/99")).andExpect(status().isNotFound()).andDo(print())
                .andExpect(content().string(allOf(Matchers.containsString("cannot find"),
                        Matchers.containsString("99"))));
    }

    @Test
    public void getsAll() throws Exception {
        mvc.perform(get("/publications/")).andDo(print())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[2].period").value(14));
    }

    @Test
    public void adds() throws Exception {
        PublicationDto publicationDto = publicationDtoMapper.toDto(publications[3]);
        ObjectMapper jsonMapper = new ObjectMapper();
        String publicationJson = jsonMapper.writeValueAsString(publicationDto);
        mvc.perform(post("/publications/").content(publicationJson)
                .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isCreated());
        verify(service).add(publications[3]);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void additionFails() throws Exception {
        PublicationDto publicationDto = publicationDtoMapper.toDto(publications[5]);
        ObjectMapper jsonMapper = new ObjectMapper();
        String publicationJson = jsonMapper.writeValueAsString(publicationDto);
        mvc.perform(post("/publications/").content(publicationJson)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest()).andDo(print());
        verifyNoMoreInteractions(service);
    }

    @Test
    public void replaces() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        PublicationDto publicationDto = publicationDtoMapper.toDto(publications[4]);
        String publicationJson = mapper.writeValueAsString(publicationDto);
        mvc.perform(put("/publications/2").content(publicationJson)
                .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk());
        verify(service).replace(2L, publications[4]);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void replacementFails() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        PublicationDto publicationDto = publicationDtoMapper.toDto(publications[5]);
        String publicationJson = mapper.writeValueAsString(publicationDto);
        mvc.perform(put("/publications/2").content(publicationJson)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest()).andDo(print());
        verifyNoMoreInteractions(service);
    }

    @Test
    public void deletes() throws Exception {
        mvc.perform(delete("/publications/3")).andExpect(status().isNoContent());
        verify(service).delete(3L);
    }

    @Test
    public void deletionFails() throws Exception {
        doThrow(new PublicationNotFoundException(88L)).when(service).delete(88L);
        mvc.perform(delete("/publications/88")).andExpect(status().isNotFound()).andDo(print())
                .andExpect(content().string(allOf(Matchers.containsString("cannot find"),
                        Matchers.containsString("88"))));
    }

}
