package ru.kerporation.tasklist.domain.exception;

public class ImageUploadException extends RuntimeException {
    public ImageUploadException(final String message) {
        super(message);
    }
}