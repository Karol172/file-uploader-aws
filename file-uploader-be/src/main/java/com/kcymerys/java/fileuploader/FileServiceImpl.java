package com.kcymerys.java.fileuploader;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final AmazonS3 amazonS3;
    private final AmazonProperties amazonProperties;

    @Override
    public void uploadFile(Set<MultipartFile> multipartFiles) {
        multipartFiles.forEach(multipartFile -> {
            if (fileRepository.findByFilename(multipartFile.getResource().getFilename()).isPresent()) {
                throw new EntityAlreadyExistException(
                        "File " + multipartFile.getResource().getFilename() + " already exists.");
            }
        });
        multipartFiles.forEach(multipartFile -> uploadObjectToS3(multipartFile, new File()));
    }

    @Override
    public void reUploadFile(MultipartFile multipartFile) {
        Optional<File> file = fileRepository.findByFilename(multipartFile.getResource().getFilename());
        if (file.isEmpty()) {
            throw new EntityNotFoundException("File " + multipartFile.getResource().getFilename() + " not found.");
        }
        uploadObjectToS3(multipartFile, file.get());
    }

    @Override
    public Page<File> searchFile(Pageable pageable, String phrase) {
        return fileRepository.searchByFilename(pageable, phrase.trim());
    }

    @Override
    public void removeFile(String filename) {
        Optional<File> file = fileRepository.findByFilename(filename);
        if (file.isEmpty()) {
            throw new EntityNotFoundException("File " + filename + " not found.");
        }
        amazonS3.deleteObject(new DeleteObjectRequest(amazonProperties.getBucketName(), filename));
        fileRepository.delete(file.get());
        log.info("File " + filename + " has been deleted from S3 bucket.");
    }

    @Override
    public byte[] downloadFile(String filename) throws IOException {
        Optional<File> file = fileRepository.findByFilename(filename);
        if (file.isEmpty()) {
            throw new EntityNotFoundException("File " + filename + " not found.");
        }
        return amazonS3.getObject(
                new GetObjectRequest(amazonProperties.getBucketName(), filename))
                .getObjectContent()
                .readAllBytes();
    }

    private void uploadObjectToS3(MultipartFile multipartFile, File file) {
        String filename = multipartFile.getResource().getFilename();
        try (final InputStream stream = multipartFile.getInputStream()) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            amazonS3.putObject(
                    new PutObjectRequest(amazonProperties.getBucketName(), filename, stream, metadata)
            );
            file.setFilename(filename);
            file.setSize(multipartFile.getSize());
            file.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
            fileRepository.save(file);
            log.info("File " + filename + " has been uploaded to S3 bucket.");
        } catch (IOException e) {
            log.error("File " + filename + " cannot be uploaded to S3 bucket. Cannot read given file.");
        }
    }

}