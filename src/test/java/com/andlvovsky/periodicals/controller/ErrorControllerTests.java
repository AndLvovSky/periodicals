package com.andlvovsky.periodicals.controller;

import com.andlvovsky.periodicals.meta.ClientPages;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.RequestDispatcher;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@WebMvcTest
@AutoConfigureMockMvc
public class ErrorControllerTests extends ControllerTests {

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
        mvc.perform(get(ClientPages.ERROR).requestAttr(RequestDispatcher.ERROR_STATUS_CODE, code))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(containsString(stringToContain)))
                .andDo(print());
    }


}
