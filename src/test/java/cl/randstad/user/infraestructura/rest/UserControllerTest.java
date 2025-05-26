package cl.randstad.user.infraestructura.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.randstad.common.security.JwtAuthorizationFilter;
import cl.randstad.common.security.JwtTokenService;
import cl.randstad.common.security.SecurityConfig;
import cl.randstad.user.application.service.UserServicePort;
import cl.randstad.user.infraestructura.rest.dto.PhoneRequest;
import cl.randstad.user.infraestructura.rest.dto.UserRequest;
import cl.randstad.user.infraestructura.rest.dto.UserResponse;
import jakarta.servlet.FilterChain;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private WebApplicationContext wac;

    @MockitoBean
    private UserServicePort userService;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @MockitoBean
    private JwtAuthorizationFilter jwtFilter;

    @BeforeEach
    void setup() {
    	this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .addFilters(jwtFilter)
                .build();
    }

    @Test
    void testInsertUser() throws Exception {
        UserRequest request = new UserRequest();
        request.setName("Juan");
        request.setEmail("juan@mail.com");
        request.setPassword("Holaclave12");
        request.setPhones(List.of(new PhoneRequest("21123213213", "122", "+54")));

        UserResponse response = new UserResponse(
                UUID.randomUUID(),
                "Juan",
                "juan@mail.com",
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                "fake-token",
                true,
                List.of()
        );

        when(userService.createUser(any(UserRequest.class))).thenReturn(response);
        
        doAnswer(invocation -> {
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(jwtFilter).doFilter(any(), any(), any());

        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf()))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name").value("Juan"))
                    .andExpect(jsonPath("$.email").value("juan@mail.com"))
                    .andDo(print());
        } catch (Exception e) {
            throw e;
        }
    }
    
    
    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    void testGetAllUsers() throws Exception {
        UserResponse user1 = new UserResponse(
                UUID.randomUUID(),
                "Juan",
                "juan@mail.com",
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                "fake-token",
                true,
                List.of()
        );

        UserResponse user2 = new UserResponse(
                UUID.randomUUID(),
                "Ana",
                "ana@mail.com",
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                "fake-token-2",
                true,
                List.of()
        );

        List<UserResponse> users = List.of(user1, user2);

        when(userService.findAll()).thenReturn(users);

        doAnswer(invocation -> {
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(jwtFilter).doFilter(any(), any(), any());

        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .header("Authorization", "Bearer fake-jwt-token")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Juan"))
                .andExpect(jsonPath("$[1].name").value("Ana"))
                .andDo(print());
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    void testGetUserById() throws Exception {
        UUID userId = UUID.randomUUID();

        UserResponse user = new UserResponse(
            userId,
            "Carlos",
            "carlos@mail.com",
            LocalDateTime.now(),
            LocalDateTime.now(),
            LocalDateTime.now(),
            "fake-token-carlos",
            true,
            List.of()
        );

        when(userService.findById(userId)).thenReturn(user);

        doAnswer(invocation -> {
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(jwtFilter).doFilter(any(), any(), any());

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", userId)
                        .header("Authorization", "Bearer fake-jwt-token")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.name").value("Carlos"))
                .andExpect(jsonPath("$.email").value("carlos@mail.com"))
                .andDo(print());
    }
    
    
    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    void testUpdateUser() throws Exception {
        UUID userId = UUID.randomUUID();

        UserRequest updateRequest = new UserRequest();
        updateRequest.setName("Carlos Updated");
        updateRequest.setEmail("carlosupdated@mail.com");
        updateRequest.setPassword("NewPass123");
        updateRequest.setPhones(List.of(new PhoneRequest("12345678", "1", "59")));

        UserResponse updatedUser = new UserResponse(
            userId,
            "Carlos Updated",
            "carlosupdated@mail.com",
            LocalDateTime.now(),
            LocalDateTime.now(),
            LocalDateTime.now(),
            "fake-token-updated",
            true,
            List.of()
        );

        when(userService.update(eq(userId), any(UserRequest.class))).thenReturn(updatedUser);

        doAnswer(invocation -> {
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(jwtFilter).doFilter(any(), any(), any());

        mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest))
                        .header("Authorization", "Bearer fake-jwt-token")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.name").value("Carlos Updated"))
                .andExpect(jsonPath("$.email").value("carlosupdated@mail.com"))
                .andDo(print());
    }
    
    
    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    void testDeleteUser() throws Exception {
        UUID userId = UUID.randomUUID();

        doNothing().when(userService).delete(userId);

        doAnswer(invocation -> {
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(jwtFilter).doFilter(any(), any(), any());

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", userId)
                        .header("Authorization", "Bearer fake-jwt-token")
                        .with(csrf()))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

}