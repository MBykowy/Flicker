package com.example.flicker;

import com.FlickerDomain.flicker.controller.LoginController;
import com.FlickerDomain.flicker.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.Model;

import static org.mockito.Mockito.*;

@SpringBootTest
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private LoginController loginController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
    }

    @Test
    public void testLoginUser_Successful() throws Exception {
        // Ustawiamy, że usługa UserService zwróci true (logowanie udane)
        when(userService.authenticate("validUsername", "validPassword")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .param("username", "validUsername")
                        .param("password", "validPassword"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/dashboard"));  // Sprawdzenie, że po zalogowaniu następuje przekierowanie na /dashboard
    }

    @Test
    public void testLoginUser_Failure() throws Exception {
        // Ustawiamy, że usługa UserService zwróci false (logowanie nieudane)
        when(userService.authenticate("invalidUsername", "invalidPassword")).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .param("username", "invalidUsername")
                        .param("password", "invalidPassword"))
                .andExpect(MockMvcResultMatchers.status().isOk())  // Status 200 OK (form)
                .andExpect(MockMvcResultMatchers.view().name("login"))  // Sprawdzenie, że widok "login" jest renderowany
                .andExpect(MockMvcResultMatchers.model().attributeExists("error"));  // Sprawdzenie, czy atrybut "error" istnieje w modelu
    }
}