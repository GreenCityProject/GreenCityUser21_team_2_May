package greencity.controller;

import greencity.UserApplication;
import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = UserApplication.class)
@WebAppConfiguration
@ActiveProfiles("dev")
public class UserControllerIntegrationTest {
    private static final String userLink = "/user";

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesGreetController() {
        ServletContext servletContext = webApplicationContext.getServletContext();

        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(webApplicationContext.getBean("userController"));
    }

    @Test
    void scheduleDeleteDeactivateUserTest() throws Exception {
        Principal principal = () -> "service@greencity.ua";  //admin

        mockMvc.perform(post(userLink + "/deleteDeactivatedUsers")
                        .principal(principal))
                .andExpect(status().isOk());
    }

    @Test
    void findUserForManagementByPage_isOk() throws Exception {
        Principal principal = () -> "service@greencity.ua"; //admin

        mockMvc.perform(get(userLink + "/findUserForManagement")
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").isArray())
                .andExpect(jsonPath("$.page[0]").isNotEmpty())
                .andExpect(jsonPath("$.page[1]").isNotEmpty())
                .andExpect(jsonPath("$.totalElements").isNumber())
                .andExpect(jsonPath(("$.currentPage")).value(0))
                .andExpect(jsonPath("$.totalPages").isNumber())
                .andExpect(jsonPath(("$.number")).isNumber())
                .andExpect(jsonPath("$.hasPrevious").value(false))
                .andExpect(jsonPath("$.hasNext").isBoolean())
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").isBoolean());
    }
}
