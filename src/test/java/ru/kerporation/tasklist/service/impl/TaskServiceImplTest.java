package ru.kerporation.tasklist.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.kerporation.tasklist.config.TestConfig;
import ru.kerporation.tasklist.domain.exception.ResourceNotFoundException;
import ru.kerporation.tasklist.domain.task.Status;
import ru.kerporation.tasklist.domain.task.Task;
import ru.kerporation.tasklist.domain.task.TaskImage;
import ru.kerporation.tasklist.repository.TaskRepository;
import ru.kerporation.tasklist.service.ImageService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private ImageService imageService;

    @Autowired
    private TaskServiceImpl taskService;

    @Test
    void getById() {
        final Long id = 1L;
        final Task task = new Task();
        task.setId(id);
        Mockito.when(taskRepository.findById(id))
                .thenReturn(Optional.of(task));
        final Task testTask = taskService.getById(id);
        Mockito.verify(taskRepository).findById(id);
        Assertions.assertEquals(task, testTask);
    }

    @Test
    void getByNotExistingId() {
        final Long id = 1L;
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> taskService.getById(id));
        Mockito.verify(taskRepository).findById(id);
    }

    @Test
    void getAllByUserId() {
        final Long userId = 1L;
        final List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            tasks.add(new Task());
        }
        Mockito.when(taskRepository.findAllByUserId(userId)).thenReturn(tasks);
        final List<Task> testTasks = taskService.getAllByUserId(userId);
        Mockito.verify(taskRepository).findAllByUserId(userId);
        Assertions.assertEquals(tasks, testTasks);
    }

    @Test
    void getSoonTasks() {
        final Duration duration = Duration.ofHours(1);
        final List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            tasks.add(new Task());
        }
        Mockito.when(taskRepository.findAllSoonTasks(Mockito.any(), Mockito.any())).thenReturn(tasks);
        final List<Task> testTasks = taskService.getAllSoonTasks(duration);
        Mockito.verify(taskRepository).findAllSoonTasks(Mockito.any(), Mockito.any());
        Assertions.assertEquals(tasks, testTasks);
    }

    @Test
    void update() {
        final Long id = 1L;
        final Task task = new Task();
        task.setId(id);
        task.setStatus(Status.DONE);
        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        final Task testTask = taskService.update(task);
        Mockito.verify(taskRepository).save(task);
        Assertions.assertEquals(task, testTask);
        Assertions.assertEquals(task.getTitle(), testTask.getTitle());
        Assertions.assertEquals(task.getDescription(), testTask.getDescription());
        Assertions.assertEquals(task.getStatus(), testTask.getStatus());
    }

    @Test
    void updateWithEmptyStatus() {
        final Long id = 1L;
        final Task task = new Task();
        task.setId(id);
        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        final Task testTask = taskService.update(task);
        Mockito.verify(taskRepository).save(task);
        Assertions.assertEquals(task.getTitle(), testTask.getTitle());
        Assertions.assertEquals(task.getDescription(), testTask.getDescription());
        Assertions.assertEquals(testTask.getStatus(), Status.TODO);
    }

    @Test
    void create() {
        final Long userId = 1L;
        final Long taskId = 1L;
        final Task task = new Task();
        Mockito.doAnswer(invocation -> {
            Task savedTask = invocation.getArgument(0);
            savedTask.setId(taskId);
            return savedTask;
        }).when(taskRepository).save(task);
        final Task testTask = taskService.create(task, userId);
        Mockito.verify(taskRepository).save(task);
        Assertions.assertNotNull(testTask.getId());
        Mockito.verify(taskRepository).assignTask(userId, task.getId());
    }

    @Test
    void delete() {
        final Long id = 1L;
        taskService.delete(id);
        Mockito.verify(taskRepository).deleteById(id);
    }

    @Test
    void uploadImage() {
        final Long id = 1L;
        final String imageName = "imageName";
        final TaskImage taskImage = new TaskImage();
        Mockito.when(imageService.upload(taskImage)).thenReturn(imageName);
        taskService.uploadImage(id, taskImage);
        Mockito.verify(taskRepository).addImage(id, imageName);
    }

}