package com.kcymerys.java.fileuploader;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    private final AmazonS3 amazonS3;
    private final AmazonProperties amazonProperties;

    @Override
    public void uploadFile(Set<MultipartFile> multipartFiles) {
         multipartFiles.forEach(multipartFile -> {
            try (final InputStream stream = multipartFile.getInputStream()) {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(multipartFile.getSize());
                amazonS3.putObject(
                        new PutObjectRequest(amazonProperties.getBucketName(),
                                multipartFile.getResource().getFilename(), stream, metadata)
                );
                log.info("File " + multipartFile.getResource().getFilename() + " has been uploaded to S3 bucket.");
            } catch (IOException e) {
                log.error("File " + multipartFile.getResource().getFilename() + " cannot be uploaded to S3 bucket.");
            }
        });
    }

}
