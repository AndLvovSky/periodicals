package com.andlvovsky.periodicals.controller;

import com.andlvovsky.periodicals.exception.PublicationNotFoundException;
import com.andlvovsky.periodicals.meta.Endpoints;
import com.andlvovsky.periodicals.model.Publication;
import com.andlvovsky.periodicals.dto.PublicationDto;
import com.andlvovsky.periodicals.mapper.PublicationMapper;
import com.andlvovsky.periodicals.service.PublicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.Matchers.allOf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(PublicationController.class)
@AutoConfigureMockMvc
public class PublicationControllerTests extends ControllerTests {

    @MockBean
    private PublicationService publicationService;

    private ObjectMapper jsonMapper = new ObjectMapper();

    private static Publication[] publications;

    private static PublicationDto[] publicationDtos;

    @BeforeClass
    public static void initializePublicationsAndCorrespondingDtos() {
        publications = new Publication[6];
        publications[0] = new Publication(1L,"The Guardian", 7, new BigDecimal("10"), "-");
        publications[1] = new Publication(2L,"Daily Mail", 1, new BigDecimal("5.5"), "-");
        publications[2] = new Publication(3L,"The Washington Post", 14, new BigDecimal("17"), "-");
        publications[3] = new Publication(4L,"The Sun", 30, new BigDecimal("20"), "-");
        publications[4] = new Publication(5L,"The Wall Street Journal", 7, new BigDecimal("15"), "-");
        publications[5] = new Publication(6L,"The New Yorker", -5, new BigDecimal("10"), "-");
        publicationDtos = new PublicationDto[publications.length];
        PublicationMapper publicationDtoMapper = Mappers.getMapper(PublicationMapper.class);
        for (int i = 0; i < publications.length; i++) {
            publicationDtos[i] = publicationDtoMapper.toDto(publications[i]);
        }
    }

    @Before
    public void defineRepositoryBehavior() {
        when(publicationService.getOne(1L)).thenReturn(publicationDtos[0]);
        when(publicationService.getOne(2L)).thenReturn(publicationDtos[1]);
        when(publicationService.getOne(3L)).thenReturn(publicationDtos[2]);
        when(publicationService.getAll()).thenReturn(Arrays.asList(
                publicationDtos[0], publicationDtos[1], publicationDtos[2]));
        when(publicationService.getOne(99L)).thenThrow(new PublicationNotFoundException(99L));
        doThrow(new PublicationNotFoundException(88L)).when(publicationService).delete(88L);
        doThrow(new ConstraintViolationException(null, null, null)).when(publicationService).delete(4L);
    }


    @Test
    @WithMockUser(authorities = {"READ_PUBLICATIONS"})
    public void getsOne() throws Exception {
        mvc.perform(get(Endpoints.PUBLICATIONS + "/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("The Guardian")).andDo(print());
    }

    @Test
    @WithMockUser(authorities = {"READ_PUBLICATIONS"})
    public void getOneFails() throws Exception {
        mvc.perform(get(Endpoints.PUBLICATIONS + "/99")).andExpect(status().isNotFound())
                .andExpect(content().string(allOf(Matchers.containsString("cannot find"),
                        Matchers.containsString("99")))).andDo(print());
    }

    @Test
    @WithMockUser(authorities = {})
    public void getOneFailsUnauthorized() throws Exception {
        mvc.perform(get(Endpoints.PUBLICATIONS + "/1"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @WithAnonymousUser
    public void getOneFailsUnauthenticated() throws Exception {
        mvc.perform(get(Endpoints.PUBLICATIONS + "/1"))
                .andExpect(redirectedUrlPattern("**/login")).andDo(print());
    }

    @Test
    @WithMockUser(authorities = {"READ_PUBLICATIONS"})
    public void getsAll() throws Exception {
        mvc.perform(get(Endpoints.PUBLICATIONS))
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[2].period").value(14)).andDo(print());
    }

    @Test
    @WithMockUser(authorities = {"EDIT_PUBLICATIONS"})
    public void adds() throws Exception {
        String publicationJson = jsonMapper.writeValueAsString(publicationDtos[3]);
        mvc.perform(post(Endpoints.PUBLICATIONS).content(publicationJson)
                .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isCreated());
        verify(publicationService).add(publicationDtos[3]);
        verifyNoMoreInteractions(publicationService);
    }

    @Test
    @WithMockUser(authorities = {"EDIT_PUBLICATIONS"})
    public void additionFails() throws Exception {
        String publicationJson = jsonMapper.writeValueAsString(publicationDtos[5]);
        mvc.perform(post(Endpoints.PUBLICATIONS).content(publicationJson)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest()).andDo(print());
        verifyNoMoreInteractions(publicationService);
    }

    @Test
    @WithMockUser(authorities = {"READ_PUBLICATIONS"})
    public void additionFailsUnauthorized() throws Exception {
        String publicationJson = jsonMapper.writeValueAsString(publicationDtos[3]);
        mvc.perform(post(Endpoints.PUBLICATIONS).content(publicationJson)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = {"EDIT_PUBLICATIONS"})
    public void replaces() throws Exception {
        String publicationJson = jsonMapper.writeValueAsString(publicationDtos[4]);
        mvc.perform(put(Endpoints.PUBLICATIONS + "/2").content(publicationJson)
                .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk());
        verify(publicationService).replace(2L, publicationDtos[4]);
        verifyNoMoreInteractions(publicationService);
    }

    @Test
    @WithMockUser(authorities = {"EDIT_PUBLICATIONS"})
    public void replacementFails() throws Exception {
        String publicationJson = jsonMapper.writeValueAsString(publicationDtos[5]);
        mvc.perform(put(Endpoints.PUBLICATIONS + "/2").content(publicationJson)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest()).andDo(print());
        verifyNoMoreInteractions(publicationService);
    }

    @Test
    @WithMockUser(authorities = {"READ_PUBLICATIONS"})
    public void replacementFailsUnauthorized() throws Exception {
        String publicationJson = jsonMapper.writeValueAsString(publicationDtos[4]);
        mvc.perform(put(Endpoints.PUBLICATIONS + "/2").content(publicationJson)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = {"EDIT_PUBLICATIONS"})
    public void deletes() throws Exception {
        mvc.perform(delete(Endpoints.PUBLICATIONS + "/3")).andExpect(status().isNoContent());
        verify(publicationService).delete(3L);
    }

    @Test
    @WithMockUser(authorities = {"EDIT_PUBLICATIONS"})
    public void deletionFailsNotFound() throws Exception {
        mvc.perform(delete(Endpoints.PUBLICATIONS + "/88")).andExpect(status().isNotFound())
                .andExpect(content().string(allOf(Matchers.containsString("cannot find"),
                        Matchers.containsString("88")))).andDo(print());
    }

    @Test
    @WithMockUser(authorities = {"EDIT_PUBLICATIONS"})
    public void deletionFailsConstraintViolation() throws Exception {
        mvc.perform(delete(Endpoints.PUBLICATIONS + "/4")).andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = {"READ_PUBLICATIONS"})
    public void deletionFailsUnauthorized() throws Exception {
        mvc.perform(delete(Endpoints.PUBLICATIONS + "/3"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

}
