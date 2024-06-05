package greencity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import greencity.ModelUtils;
import greencity.config.SecurityConfig;
import greencity.message.EventEmailMessage;
import greencity.message.SendHabitNotification;
import greencity.security.jwt.JwtTool;
import greencity.service.EmailService;
import greencity.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SecurityConfig.class, EmailController.class})
@WebAppConfiguration
@EnableWebMvc
@Import({JwtTool.class})
@TestPropertySource(properties = {
        "accessTokenValidTimeInMinutes=60",
        "refreshTokenValidTimeInMinutes=1440",
        "tokenKey=secretTokenKey"
})
class EmailControllerWithSecurityConfigTest {
    private static final String LINK = "/email";

    private MockMvc mockMvc;

    @MockBean
    private EmailService emailService;

    @MockBean
    private UserService userService;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        when(userService.findByEmail(anyString())).thenReturn(ModelUtils.getUserVO());
    }

    @Test
    @WithMockUser(username = "TestAdmin", roles = "ADMIN")
    void sendHabitNotification_ReturnsIsOk() throws Exception {
        String content = "{" +
                "\"email\":\"test.email@gmail.com\"," +
                "\"name\":\"String\"" +
                "}";

        sentPostRequest(content, "/sendHabitNotification")
                .andExpect(status().isOk());

        SendHabitNotification notification =
                new ObjectMapper().readValue(content, SendHabitNotification.class);

        verify(emailService).sendHabitNotification(notification.getName(), notification.getEmail());
    }

    @Test
    @WithAnonymousUser
    void sendHabitNotification_ReturnsIsUnauthorized() throws Exception {
        String content = "{" +
                "\"email\":\"test.email@gmail.com\"," +
                "\"name\":\"String\"" +
                "}";

        sentPostRequest(content, "/sendHabitNotification")
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(emailService);
    }

    @Test
    @WithMockUser(username = "TestUser", roles = "USER")
    void sendHabitNotification_ReturnsIsForbidden() throws Exception {
        String content = "{" +
                "\"email\":\"test.email@gmail.com\"," +
                "\"name\":\"String\"" +
                "}";

        sentPostRequest(content, "/sendHabitNotification")
                .andExpect(status().isForbidden());

        verifyNoInteractions(emailService);
    }

    private ResultActions sentPostRequest(String content, String subLink) throws Exception {
        return mockMvc.perform(post(LINK + subLink)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));
    }

    @Test
    @WithAnonymousUser
    void changePlaceStatus_ReturnsIsUnauthorized() throws Exception {
        String content = "{" +
                "\"email\":\"test.email@gmail.com\"," +
                "\"name\":\"String\"" +
                "}";

        sentPostRequest(content, "/changePlaceStatus")
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(emailService);
    }
  
    @Test
    @WithAnonymousUser
    void addEcoNews_ReturnsIsUnauthorized() throws Exception {
        sentPostRequest("{}", "/addEcoNews")
                .andExpect(status().isUnauthorized());
        verifyNoInteractions(emailService);
    }

    @Test
    @WithAnonymousUser
    void sendEventNotification_ReturnsIsUnauthorized() throws Exception {
        String content = "{}";

        sentPostRequest(content, "/sendEventNotification")
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(emailService);
    }

    @ParameterizedTest
    @CsvSource({
            "User, USER",
            "Moderator, MODERATOR",
            "Ubs_Employee, UBS_EMPLOYEE",
            "Employee, EMPLOYEE"
    })
    @WithMockUser
    void sendEventNotification_ReturnsIsOk() throws Exception {
        String content = "{\n" +
                "  \"email\": \"email@email.com\",\n" +
                "  \"subject\": \"Subject\",\n" +
                "  \"author\": \"Author\",\n" +
                "  \"eventTitle\": \"Event Title\",\n" +
                "  \"description\": \"Event Description\",\n" +
                "  \"isOpen\": \"true\",\n" +
                "  \"status\": \"ONLINE\",\n" +
                "  \"link\": \"http://event\",\n" +
                "  \"startDateTime\": \"2024-06-05T10:00:00Z\",\n" +
                "  \"endDateTime\": \"2024-06-05T12:00:00Z\",\n" +
                "  \"address\": {\n" +
                "    \"latitude\": \"0\",\n" +
                "    \"longitude\": \"0\",\n" +
                "    \"addressEn\": \"En\",\n" +
                "    \"addressUa\": \"Укр\"\n" +
                "  },\n" +
                "  \"linkToEvent\": \"http://link-to-event\"\n" +
                "}";

        sentPostRequest(content, "/sendEventNotification")
                .andExpect(status().isOk());

        ArgumentCaptor<EventEmailMessage> messageCaptor = ArgumentCaptor.forClass(EventEmailMessage.class);
        verify(emailService, times(1)).sendNotificationMessageByEmail(messageCaptor.capture());
    }
}
