package ru.kerporation.tasklist.service.impl;

import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.kerporation.tasklist.config.TestConfig;
import ru.kerporation.tasklist.domain.mail.MailType;
import ru.kerporation.tasklist.domain.user.User;
import ru.kerporation.tasklist.repository.TaskRepository;
import ru.kerporation.tasklist.repository.UserRepository;

import java.io.IOException;
import java.util.Properties;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class MailServiceImplTest {

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private Configuration configuration;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private JavaMailSender javaMailSender;

    @Autowired
    private MailServiceImpl mailService;

    @Test
    void sendEmailForRegistration() {
        try {
            final String name = "Mike";
            final String username = "mike@gmail.com";
            final User user = new User();
            user.setName(name);
            user.setUsername(username);
            Mockito.when(javaMailSender.createMimeMessage()).thenReturn(Mockito.mock(MimeMessage.class));
            Mockito.when(configuration.getTemplate("register.ftlh")).thenReturn(Mockito.mock(Template.class));
            mailService.sendEmail(user, MailType.REGISTRATION, new Properties());
            Mockito.verify(configuration).getTemplate("register.ftlh");
            Mockito.verify(javaMailSender).send(Mockito.any(MimeMessage.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void sendEmailForRemind() {
        try {
            final String name = "Mike";
            final String username = "mike@gmail.com";
            final User user = new User();
            user.setName(name);
            user.setUsername(username);
            Mockito.when(javaMailSender.createMimeMessage()).thenReturn(Mockito.mock(MimeMessage.class));
            Mockito.when(configuration.getTemplate("reminder.ftlh")).thenReturn(Mockito.mock(Template.class));
            mailService.sendEmail(user, MailType.REMINDER, new Properties());
            Mockito.verify(configuration).getTemplate("reminder.ftlh");
            Mockito.verify(javaMailSender).send(Mockito.any(MimeMessage.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}