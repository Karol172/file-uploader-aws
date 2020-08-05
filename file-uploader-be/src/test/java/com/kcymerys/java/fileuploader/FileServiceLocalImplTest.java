package com.kcymerys.java.fileuploader;

import com.amazonaws.services.workmail.model.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class FileServiceLocalImplTest {

    @InjectMocks
    FileServiceLocalImpl fileService;

    @Mock
    LocalProperties localProperties;

    final String workDir = "./test";

    @AfterEach
    void clean() throws IOException {
        Files.list(Paths.get(workDir)).forEach(path -> {
            try {
                Files.delete(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Files.delete(Path.of(workDir));
    }

    @Test
    void shouldUploadFile() {
        Set<MultipartFile> files = Set.of(new MockMultipartFile("file1", "f1.txt",
                        "text/plain", "example text".getBytes()),
                new MockMultipartFile("file2", "f2.txt",
                        "text/plain", "some other type".getBytes()),
                new MockMultipartFile("json", "f3.json",
                        "application/json", "{\"json\": \"someValue\"}".getBytes()));

        Mockito.when(localProperties.getWorkDir()).thenReturn(workDir);

        Assertions.assertAll(() -> fileService.uploadFile(files));
    }

    @Test
    void shouldListFiles() throws IOException {
        String phrase = "";
        createWorkDirIfNotExist();
        Path filepath = Path.of(workDir + '/' + "f1");
        Files.write(filepath, "empty file".getBytes());
        File file = new File(filepath.toString());
        Mockito.when(localProperties.getWorkDir()).thenReturn(workDir);
        Assertions.assertArrayEquals(Collections.singletonList(
                new FileDTO(file.getName(), file.length())).toArray(),
                fileService.searchFile(PageRequest.of(0, 20), phrase).getContent().toArray());
    }

    @Test
    void shouldRemoveFile() {
        Mockito.when(localProperties.getWorkDir()).thenReturn(workDir);
        Assertions.assertAll(() -> fileService.removeFile("example-name"));
    }

    @Test
    void shouldDownloadFile() throws IOException {
        createWorkDirIfNotExist();
        String filename = "ex";
        byte[] content = "empty file".getBytes();
        Files.write(Paths.get(workDir + '/' + filename), content);
        Mockito.when(localProperties.getWorkDir()).thenReturn(workDir);
        Assertions.assertArrayEquals(content, fileService.downloadFile(filename));
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenDownloadFile() {
        createWorkDirIfNotExist();
        String filename = "ex";
        Mockito.when(localProperties.getWorkDir()).thenReturn(workDir);
        Assertions.assertThrows(EntityNotFoundException.class, () -> fileService.downloadFile(filename));
    }

    private void createWorkDirIfNotExist() {
        if (!Files.exists(Path.of(workDir))) {
            new File(workDir).mkdir();
        }
    }

}
