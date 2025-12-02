package id.taufiq.latihan.spring_ai.util;

import id.taufiq.latihan.spring_ai.model.dto.StoredObject;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
public class ObjectStorage {

    private final S3Template s3Template;

    public ObjectStorage(S3Template s3Template) {
        this.s3Template = s3Template;
    }

    public StoredObject upload(MultipartFile file, String bucketName) {
        String originalFilename = file.getOriginalFilename();
        Assert.hasText(originalFilename, "Original filename must not be empty");

        try (InputStream inputStream = file.getInputStream()) {
            S3Resource uploaded = s3Template.upload(bucketName, originalFilename, inputStream);
            return new StoredObject(bucketName, uploaded.getFilename(), originalFilename);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to upload file to S3", e);
        }
    }

    public Resource download(String bucketName, String key) {
        return s3Template.download(bucketName, key);
    }
}
