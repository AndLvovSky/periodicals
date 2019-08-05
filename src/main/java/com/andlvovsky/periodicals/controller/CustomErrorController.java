package com.andlvovsky.periodicals.controller;

import com.andlvovsky.periodicals.meta.ClientPages;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Controller
public class CustomErrorController implements ErrorController {

    private static final List<Integer> CUSTOM_ERROR_PAGES =
            Arrays.asList(403, 404, 500);

    @RequestMapping(ClientPages.ERROR)
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status == null) {
            return "error/any-error";
        }
        Integer statusCode = Integer.valueOf(status.toString());
        if (CUSTOM_ERROR_PAGES.contains(statusCode)) {
            return "error/error-" + statusCode;
        }
        model.addAttribute("errorCode", statusCode);
        return "error/any-error";
    }

    public String getErrorPath() {
        return ClientPages.ERROR;
    }

}
