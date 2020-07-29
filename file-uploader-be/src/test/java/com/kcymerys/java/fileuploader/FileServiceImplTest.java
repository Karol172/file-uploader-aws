package com.kcymerys.java.fileuploader;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class FileServiceImplTest {

    @InjectMocks
    FileServiceImpl fileService;

    @Mock
    AmazonProperties amazonProperties;

    @Mock
    AmazonS3 amazonS3;

    @Mock
    FileRepository fileRepository;

    @Test
    void shouldUploadFile() {
        Set<MultipartFile> files = Set.of(new MockMultipartFile("file1", "f1.txt",
                        "text/plain", "example text".getBytes()),
                new MockMultipartFile("file2", "f2.txt",
                        "text/plain", "some other type".getBytes()),
                new MockMultipartFile("json", "f3.json",
                        "application/json", "{\"json\": \"someValue\"}".getBytes()));

        Mockito.when(fileRepository.findByFilename(Mockito.anyString())).thenReturn(Optional.empty());
        Mockito.when(amazonProperties.getBucketName()).thenReturn("text-bucket");

        Assertions.assertAll(() -> fileService.uploadFile(files));
    }

    @Test
    void shouldThrowEntityAlreadyExistExceptionWhenUploadFile() {
        Set<MultipartFile> files = Set.of(new MockMultipartFile("file1", "f1.txt",
                        "text/plain", "example text".getBytes()),
                new MockMultipartFile("file2", "f2.txt",
                        "text/plain", "some other type".getBytes()),
                new MockMultipartFile("json", "f3.json",
                        "application/json", "{\"json\": \"someValue\"}".getBytes()));

        Mockito.when(fileRepository.findByFilename(Mockito.any()))
                .thenReturn(Optional.of(new File()));

        Assertions.assertThrows(EntityAlreadyExistException.class, () -> fileService.uploadFile(files));
    }

    @Test
    void shouldReUploadFile() {
        MultipartFile file = new MockMultipartFile("file1", "f1.txt",
                        "text/plain", "example text".getBytes());

        Mockito.when(fileRepository.findByFilename(Mockito.anyString()))
                .thenReturn(Optional.of(new File()));
        Mockito.when(amazonProperties.getBucketName()).thenReturn("text-bucket");

        Assertions.assertAll(() -> fileService.reUploadFile(file));
    }

    @Test
    void shouldThrowEntityNotFoundWhenReUploadFile() {
        MultipartFile file = new MockMultipartFile("file1", "f1.txt",
                "text/plain", "example text".getBytes());

        Mockito.when(fileRepository.findByFilename(Mockito.anyString()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> fileService.reUploadFile(file));
    }

    @Test
    void shouldListFiles(@Mock Pageable pageable) {
        String phrase = "";
        List<File> results = List.of(new File(), new File());
        Mockito.when(fileRepository.searchByFilename(pageable, phrase))
                .thenReturn(new PageImpl<>(results));

        Assertions.assertArrayEquals(results.toArray(),
                fileService.searchFile(pageable, phrase).getContent().toArray());
    }

    @Test
    void shouldRemoveFile() {
        File file = new File(1L, "exampel", 256L, Timestamp.valueOf(LocalDateTime.now()));
        Mockito.when(fileRepository.findByFilename(file.getFilename())).thenReturn(Optional.of(file));

        Assertions.assertAll(() -> fileService.removeFile(file.getFilename()));
    }

    @Test
    void shouldThrowEntityNotFoundWhenRemovingFile() {
        String filename = "ex";
        Mockito.when(fileRepository.findByFilename(filename)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> fileService.removeFile(filename));
    }

    @Test
    void shouldDownloadFile() throws IOException {
        String filename = "ex";
        File file = new File(1L, filename, 256L, Timestamp.valueOf(LocalDateTime.now()));
        MultipartFile mockFile = new MockMultipartFile(filename, filename,
                "text/plain", "example text".getBytes());
        S3Object obj = new S3Object();
        obj.setObjectContent(mockFile.getInputStream());
        Mockito.when(fileRepository.findByFilename(file.getFilename())).thenReturn(Optional.of(file));
        Mockito.when(amazonS3.getObject(Mockito.any())).thenReturn(obj);
        byte[] output = fileService.downloadFile(filename);
        Assertions.assertArrayEquals(mockFile.getBytes(), output);
    }

    @Test
    void shouldThrowEntityNotFoundWhenDownloadFile() throws IOException {
        String filename = "ex";
        Mockito.when(fileRepository.findByFilename(filename)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> fileService.downloadFile(filename));
    }

}