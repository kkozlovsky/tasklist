package ru.kerporation.tasklist.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.kerporation.tasklist.domain.mail.MailType;
import ru.kerporation.tasklist.domain.task.Task;
import ru.kerporation.tasklist.domain.user.User;
import ru.kerporation.tasklist.service.MailService;
import ru.kerporation.tasklist.service.Reminder;
import ru.kerporation.tasklist.service.TaskService;
import ru.kerporation.tasklist.service.UserService;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class ReminderImpl implements Reminder {

    private final TaskService taskService;
    private final UserService userService;
    private final MailService mailService;
    private final Duration duration = Duration.ofHours(1);

    @Scheduled(cron = "0 0 * * * *")
    @Override
    public void remindForTask() {
        final List<Task> tasks = taskService.getAllSoonTasks(duration);
        tasks.forEach(task -> {
            final User user = userService.getTaskAuthor(task.getId());
            Properties properties = new Properties();
            properties.setProperty("task.title", task.getTitle());
            properties.setProperty("task.description", task.getDescription());
            mailService.sendEmail(user, MailType.REMINDER, properties);
        });
    }

}