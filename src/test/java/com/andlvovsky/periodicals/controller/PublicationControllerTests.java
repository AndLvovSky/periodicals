package com.andlvovsky.periodicals.controller;

import com.andlvovsky.periodicals.exception.PublicationNotFoundException;
import com.andlvovsky.periodicals.model.publication.Publication;
import com.andlvovsky.periodicals.model.publication.PublicationDto;
import com.andlvovsky.periodicals.model.publication.PublicationMapper;
import com.andlvovsky.periodicals.service.PublicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
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
public class PublicationControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PublicationService service;

    private ObjectMapper jsonMapper = new ObjectMapper();

    private static Publication[] publications;

    private static PublicationDto[] publicationDtos;

    @BeforeClass
    public static void initializePublicationsAndCorrespondingDtos() {
        publications = new Publication[6];
        publications[0] = new Publication("The Guardian", 7, 10., "-");
        publications[1] = new Publication("Daily Mail", 1, 5.5, "-");
        publications[2] = new Publication("The Washington Post", 14, 17., "-");
        publications[3] = new Publication("The Sun", 30, 20., "-");
        publications[4] = new Publication("The Wall Street Journal", 7, 15., "-");
        publications[5] = new Publication("The New Yorker", -5, 10., "-");
        publicationDtos = new PublicationDto[publications.length];
        PublicationMapper publicationDtoMapper = Mappers.getMapper(PublicationMapper.class);
        for (int i = 0; i < publications.length; i++) {
            publicationDtos[i] = publicationDtoMapper.toDto(publications[i]);
        }
    }

    @Before
    public void defineRepositoryBehavior() {
        when(service.getOne(1L)).thenReturn(publications[0]);
        when(service.getOne(2L)).thenReturn(publications[1]);
        when(service.getOne(3L)).thenReturn(publications[2]);
        when(service.getAll()).thenReturn(Arrays.asList(publications[0], publications[1], publications[2]));
        when(service.getOne(99L)).thenThrow(new PublicationNotFoundException(99L));
        doThrow(new PublicationNotFoundException(88L)).when(service).delete(88L);
    }


    @Test
    @WithMockUser(authorities = {"READ_PUBLICATIONS"})
    public void getsOne() throws Exception {
        mvc.perform(get(url("1"))).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("The Guardian")).andDo(print());
    }

    @Test
    @WithMockUser(authorities = {"READ_PUBLICATIONS"})
    public void getOneFails() throws Exception {
        mvc.perform(get(url("99"))).andExpect(status().isNotFound())
                .andExpect(content().string(allOf(Matchers.containsString("cannot find"),
                        Matchers.containsString("99")))).andDo(print());
    }

    @Test
    @WithMockUser(authorities = {})
    public void getOneFailsUnauthorized() throws Exception {
        mvc.perform(get(url("1")))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @WithAnonymousUser
    public void getOneFailsUnauthenticated() throws Exception {
        mvc.perform(get(url("1")))
                .andExpect(redirectedUrlPattern("**/login")).andDo(print());
    }

    @Test
    @WithMockUser(authorities = {"READ_PUBLICATIONS"})
    public void getsAll() throws Exception {
        mvc.perform(get(url("")))
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[2].period").value(14)).andDo(print());
    }

    @Test
    @WithMockUser(authorities = {"EDIT_PUBLICATIONS"})
    public void adds() throws Exception {
        String publicationJson = jsonMapper.writeValueAsString(publicationDtos[3]);
        mvc.perform(post(url("")).content(publicationJson)
                .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isCreated());
        verify(service).add(publications[3]);
        verifyNoMoreInteractions(service);
    }

    @Test
    @WithMockUser(authorities = {"EDIT_PUBLICATIONS"})
    public void additionFails() throws Exception {
        String publicationJson = jsonMapper.writeValueAsString(publicationDtos[5]);
        mvc.perform(post(url("")).content(publicationJson)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest()).andDo(print());
        verifyNoMoreInteractions(service);
    }

    @Test
    @WithMockUser(authorities = {"READ_PUBLICATIONS"})
    public void additionFailsUnauthorized() throws Exception {
        String publicationJson = jsonMapper.writeValueAsString(publicationDtos[3]);
        mvc.perform(post(url("")).content(publicationJson)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = {"EDIT_PUBLICATIONS"})
    public void replaces() throws Exception {
        String publicationJson = jsonMapper.writeValueAsString(publicationDtos[4]);
        mvc.perform(put(url("2")).content(publicationJson)
                .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk());
        verify(service).replace(2L, publications[4]);
        verifyNoMoreInteractions(service);
    }

    @Test
    @WithMockUser(authorities = {"EDIT_PUBLICATIONS"})
    public void replacementFails() throws Exception {
        String publicationJson = jsonMapper.writeValueAsString(publicationDtos[5]);
        mvc.perform(put(url("2")).content(publicationJson)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest()).andDo(print());
        verifyNoMoreInteractions(service);
    }

    @Test
    @WithMockUser(authorities = {"READ_PUBLICATIONS"})
    public void replacementFailsUnauthorized() throws Exception {
        String publicationJson = jsonMapper.writeValueAsString(publicationDtos[4]);
        mvc.perform(put(url("2")).content(publicationJson)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = {"EDIT_PUBLICATIONS"})
    public void deletes() throws Exception {
        mvc.perform(delete(url("3"))).andExpect(status().isNoContent());
        verify(service).delete(3L);
    }

    @Test
    @WithMockUser(authorities = {"EDIT_PUBLICATIONS"})
    public void deletionFails() throws Exception {
        mvc.perform(delete(url("88"))).andExpect(status().isNotFound())
                .andExpect(content().string(allOf(Matchers.containsString("cannot find"),
                        Matchers.containsString("88")))).andDo(print());
    }

    @Test
    @WithMockUser(authorities = {"READ_PUBLICATIONS"})
    public void deletionFailsUnauthorized() throws Exception {
        mvc.perform(delete(url("3")))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    private String url(String suffix) {
        return "/publications/" + suffix;
    }

}
