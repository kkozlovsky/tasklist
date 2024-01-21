package ru.kerporation.tasklist.service.impl;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.kerporation.tasklist.domain.exception.ImageUploadException;
import ru.kerporation.tasklist.domain.task.TaskImage;
import ru.kerporation.tasklist.service.ImageService;
import ru.kerporation.tasklist.service.props.MinioProperties;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    @Override
    public String upload(final TaskImage image) {
        try {
            createBucket();
        } catch (final Exception e) {
            throw new ImageUploadException("Image upload failed: " + e.getMessage());
        }
        final MultipartFile file = image.getFile();
        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new ImageUploadException("Image must have name.");
        }
        final String fileName = generateFileName(file);
        try (final InputStream inputStream = file.getInputStream()) {
            saveImage(inputStream, fileName);
        } catch (final Exception e) {
            throw new ImageUploadException("Image upload failed: " + e.getMessage());
        }
        return fileName;
    }

    @SneakyThrows
    private void createBucket() {
        boolean found = minioClient.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(minioProperties.getBucket())
                        .build());
        if (!found) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .build());
        }
    }

    private String generateFileName(final MultipartFile file) {
        String extension = getExtension(file);
        return UUID.randomUUID() + "." + extension;
    }

    private String getExtension(final MultipartFile file) {
        return file
                .getOriginalFilename()
                .substring(file.getOriginalFilename().lastIndexOf(".") + 1);
    }

    @SneakyThrows
    private void saveImage(final InputStream inputStream,
                           final String fileName) {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .stream(inputStream, inputStream.available(), -1)
                        .bucket(minioProperties.getBucket())
                        .object(fileName)
                        .build());
    }

}