package com.example.flicker;

import com.FlickerDomain.flicker.controller.RegistrationController;
import com.FlickerDomain.flicker.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RegistrationControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private RegistrationController registrationController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(registrationController)
                .setViewResolvers(viewResolver())
                .build();
    }

    @Test
    void testShowRegistrationForm() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    void testRegisterUserSuccess() throws Exception {
        String username = "testuser";
        String email = "test@example.com";
        String password = "password123";

        doNothing().when(userService).registerNewUser(username, email, password);

        mockMvc.perform(post("/register")
                        .param("username", username)
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/success"));

        verify(userService, times(1)).registerNewUser(username, email, password);
    }

    @Test
    void testRegisterUserFailure() throws Exception {
        String username = "testuser";
        String email = "test@example.com";
        String password = "password123";

        doThrow(new RuntimeException("Registration failed")).when(userService).registerNewUser(username, email, password);

        mockMvc.perform(post("/register")
                        .param("username", username)
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Rejestracja nie powiodła się: Registration failed"));

        verify(userService, times(1)).registerNewUser(username, email, password);
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