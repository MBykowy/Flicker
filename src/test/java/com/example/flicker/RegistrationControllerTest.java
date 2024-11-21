package com.example.flicker;

import com.FlickerDomain.flicker.controller.RegistrationController;
import com.FlickerDomain.flicker.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

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
        MockitoAnnotations.openMocks(this);  // Initialize mocks
        mockMvc = MockMvcBuilders.standaloneSetup(registrationController).build();  // Setup MockMvc
    }

    @Test
    void testShowRegistrationForm() throws Exception {
        // Simulate a GET request to /register
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())  // Ensure the response status is 200 OK
                .andExpect(view().name("register"));  // Ensure the correct view is returned
    }

    @Test
    void testRegisterUserSuccess() throws Exception {
        // Simulate a POST request with registration data
        String username = "testuser";
        String email = "test@example.com";
        String password = "password123";

        // We assume that userService.registerNewUser will not throw any exception
        doNothing().when(userService).registerNewUser(username, email, password);

        mockMvc.perform(post("/register")
                        .param("username", username)
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().is3xxRedirection())  // Ensure a redirect is issued
                .andExpect(redirectedUrl("/success"));  // Ensure redirection to the success page

        // Verify that the registerNewUser method was called once
        verify(userService, times(1)).registerNewUser(username, email, password);
    }

    @Test
    void testRegisterUserFailure() throws Exception {
        // Simulate a POST request with registration data
        String username = "testuser";
        String email = "test@example.com";
        String password = "password123";

        // Simulate a failure in the user service by throwing an exception
        doThrow(new RuntimeException("Registration failed")).when(userService).registerNewUser(username, email, password);

        mockMvc.perform(post("/register")
                        .param("username", username)
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().isOk())  // Ensure the status is 200 OK
                .andExpect(view().name("register"))  // Ensure the registration form is displayed again
                .andExpect(model().attributeExists("error"))  // Check if the error attribute is added to the model
                .andExpect(model().attribute("error", "Rejestracja nie powiodła się: Registration failed"));

        // Verify that the registerNewUser method was called once
        verify(userService, times(1)).registerNewUser(username, email, password);
    }
}