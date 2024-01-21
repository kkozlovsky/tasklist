package ru.kerporation.tasklist.service;

import ru.kerporation.tasklist.domain.task.TaskImage;

public interface ImageService {

    String upload(final TaskImage image);

}