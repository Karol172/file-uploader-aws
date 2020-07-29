package com.kcymerys.java.fileuploader;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class FileServiceAWSImplTest {

    @InjectMocks
    FileServiceAWSImpl fileService;

    @Mock
    AmazonProperties amazonProperties;

    @Mock
    AmazonS3 amazonS3;

    @Test
    void shouldUploadFile() {
        Set<MultipartFile> files = Set.of(new MockMultipartFile("file1", "f1.txt",
                        "text/plain", "example text".getBytes()),
                new MockMultipartFile("file2", "f2.txt",
                        "text/plain", "some other type".getBytes()),
                new MockMultipartFile("json", "f3.json",
                        "application/json", "{\"json\": \"someValue\"}".getBytes()));

        Mockito.when(amazonProperties.getBucketName()).thenReturn("text-bucket");

        Assertions.assertAll(() -> fileService.uploadFile(files));
    }

    @Test
    void shouldListFiles(@Mock Pageable pageable) {
        String phrase = "";
        ListObjectsV2Result res = new ListObjectsV2Result();
        Mockito.when(pageable.getSort()).thenReturn(Sort.unsorted());
        Mockito.when(amazonS3.listObjectsV2(Mockito.any(ListObjectsV2Request.class)))
                .thenReturn(res);

        Assertions.assertArrayEquals(Collections.emptyList().toArray(),
                fileService.searchFile(pageable, phrase).getContent().toArray());
    }

    @Test
    void shouldRemoveFile() {
        Assertions.assertAll(() -> fileService.removeFile("example-name"));
    }

    @Test
    void shouldDownloadFile() throws IOException {
        String filename = "ex";
        MultipartFile mockFile = new MockMultipartFile(filename, filename,
                "text/plain", "example text".getBytes());
        S3Object obj = new S3Object();
        obj.setObjectContent(mockFile.getInputStream());
        Mockito.when(amazonS3.getObject(Mockito.any())).thenReturn(obj);
        byte[] output = fileService.downloadFile(filename);
        Assertions.assertArrayEquals(mockFile.getBytes(), output);
    }

    @Test
    void shouldThrowAmazonS3ExceptionWhenDownloadFile() {
        String filename = "ex";
        String bucketName = "text-bucket";
        Mockito.when(amazonProperties.getBucketName()).thenReturn(bucketName);
        Mockito.when(amazonS3.getObject(new GetObjectRequest(bucketName, filename)))
                .thenThrow(AmazonS3Exception.class);

        Assertions.assertThrows(AmazonS3Exception.class, () -> fileService.downloadFile(filename));
    }

}
