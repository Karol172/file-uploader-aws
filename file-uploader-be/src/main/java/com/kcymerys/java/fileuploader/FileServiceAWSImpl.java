package com.kcymerys.java.fileuploader;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Slf4j
@Profile("aws")
public class FileServiceAWSImpl implements FileService {

    private final AmazonS3 amazonS3;
    private final AmazonProperties amazonProperties;

    @Override
    public void uploadFile(Set<MultipartFile> multipartFiles) {
        multipartFiles.forEach(multipartFile -> {
            String filename = multipartFile.getResource().getFilename();
            try (final InputStream stream = multipartFile.getInputStream()) {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(multipartFile.getSize());
                amazonS3.putObject(
                        new PutObjectRequest(amazonProperties.getBucketName(), filename, stream, metadata)
                );
                log.info("File " + filename + " has been uploaded to S3 bucket.");
            } catch (IOException e) {
                throw new IORuntimeException("File " + filename + " cannot be uploaded. Cannot read given file.");
            }
        });
    }

    @Override
    public Page<FileDTO> searchFile(Pageable pageable, String phrase) {
        Optional<Sort.Order> sortOrder = Optional.ofNullable(pageable.getSort().getOrderFor("filename"));

        ListObjectsV2Result response = amazonS3.listObjectsV2(
                new ListObjectsV2Request()
                .withBucketName(amazonProperties.getBucketName())
                .withPrefix(phrase.trim()));
        Stream<S3ObjectSummary> objects = response.getObjectSummaries().stream();
        if (sortOrder.isPresent()) {
            if (sortOrder.get().isAscending()) {
                objects = objects.sorted(Comparator.comparing(S3ObjectSummary::getKey));
            } else {
                objects = objects.sorted((obj1, obj2) -> -obj1.getKey().compareTo(obj2.getKey()));
            }
        }
        List<FileDTO> result = objects.skip(pageable.getPageNumber()*pageable.getPageSize())
                .limit(pageable.getPageSize())
                .map(obj -> new FileDTO(obj.getKey(), obj.getSize()))
                .collect(Collectors.toList());
        return new PageImpl<>(result, pageable, response.getKeyCount());
    }

    @Override
    public void removeFile(String filename) {
        amazonS3.deleteObject(new DeleteObjectRequest(amazonProperties.getBucketName(), filename.trim()));
        log.info("File " + filename.trim() + " has been deleted from S3 bucket.");
    }

    @Override
    public byte[] downloadFile(String filename) throws IOException {
        return amazonS3.getObject(
                new GetObjectRequest(amazonProperties.getBucketName(), filename.trim()))
                .getObjectContent()
                .readAllBytes();
    }

}
