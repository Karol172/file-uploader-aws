package com.kcymerys.java.fileuploader;

import com.amazonaws.services.s3.AmazonS3;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FileServiceImplTest {

    @InjectMocks
    FileServiceImpl fileService;

    @Mock
    AmazonProperties amazonProperties;

    @Mock
    AmazonS3 amazonS3;

    @Test
    void uploadFileWhenFileIsOk() {
        Set<MultipartFile> files = Set.of(new MockMultipartFile("file1", "f1.txt",
                        "text/plain", "example text".getBytes()),
                new MockMultipartFile("file2", "f2.txt",
                        "text/plain", "some other type".getBytes()),
                new MockMultipartFile("json", "f3.json",
                        "application/json", "{\"json\": \"someValue\"}".getBytes()));

        Mockito.when(amazonProperties.getBucketName()).thenReturn("text-bucket");
        Assertions.assertAll(() -> fileService.uploadFile(files));
    }

}