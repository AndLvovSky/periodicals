package com.andlvovsky.periodicals.controller;

import com.andlvovsky.periodicals.service.OrderService;
import com.andlvovsky.periodicals.service.PublicationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.RequestDispatcher;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@WebMvcTest
@AutoConfigureMockMvc
@WithMockUser
public class ErrorControllerTests extends ControllerTests {

    private static final String URL = "/error";

    @Test
    public void showsForbiddenPage() throws Exception {
        performError(403, "Access denied");
    }

    @Test
    public void showsInternalErrorPage() throws Exception {
        performError(500, "Internal error");
    }

    @Test
    public void showsNotFoundPage() throws Exception {
        performError(404, "Page not found");
    }

    @Test
    public void showsAnotherErrorPage() throws Exception {
        performError(450, "450");
    }

    private void performError(Integer code, String stringToContain) throws Exception {
        mvc.perform(get(URL).requestAttr(RequestDispatcher.ERROR_STATUS_CODE, code))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(containsString(stringToContain)))
                .andDo(print());
    }


}
