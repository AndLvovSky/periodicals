package com.andlvovsky.periodicals.config;

import com.andlvovsky.periodicals.meta.ClientPages;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController(ClientPages.PUBLICATIONS_EDIT).setViewName("edit");
        registry.addViewController(ClientPages.LOGIN).setViewName("login");
        registry.addViewController(ClientPages.PUBLICATIONS_VIEW).setViewName("catalog");
        registry.addViewController(ClientPages.BASKET).setViewName("basket");
    }

}