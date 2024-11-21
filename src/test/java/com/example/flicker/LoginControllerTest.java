package com.example.flicker;

import com.FlickerDomain.flicker.FlickerApplication;
import com.FlickerDomain.flicker.controller.LoginController;
import com.FlickerDomain.flicker.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = {FlickerApplication.class, LoginControllerTest.TestConfig.class})
public class LoginControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private LoginController loginController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(loginController)
                .setViewResolvers(viewResolver())
                .build();
    }

    @Test
    public void testLoginUser_Successful() throws Exception {
        when(userService.authenticate("validUsername", "validPassword")).thenReturn(true);

        mockMvc.perform(post("/login")
                        .param("username", "validUsername")
                        .param("password", "validPassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    public void testLoginUser_Failure() throws Exception {
        when(userService.authenticate("invalidUsername", "invalidPassword")).thenReturn(false);

        mockMvc.perform(post("/login")
                        .param("username", "invalidUsername")
                        .param("password", "invalidPassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("error"));
    }

    @Configuration
    static class TestConfig {
        @Bean
        public ViewResolver viewResolver() {
            InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
            viewResolver.setPrefix("/WEB-INF/views/");
            viewResolver.setSuffix(".jsp");
            return viewResolver;
        }
    }

    private static ViewResolver viewResolver() {
        return new TestConfig().viewResolver();
    }
}