package com.kcymerys.java.fileuploader;

import com.amazonaws.services.workmail.model.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Slf4j
@Profile("local")
public class FileServiceLocalImpl implements FileService {

    private final LocalProperties localProperties;

    @Override
    public void uploadFile(Set<MultipartFile> multipartFiles) {
        checkIfWorkDirExist();
        multipartFiles.forEach(multipartFile -> {
            String filename = multipartFile.getResource().getFilename();
            try (final InputStream stream = multipartFile.getInputStream()) {
                Files.write(Path.of(localProperties.getWorkDir() + '/' + filename),
                        stream.readAllBytes());
                log.info("File " + filename + " has been uploaded.");
            } catch (IOException e) {
                throw new IORuntimeException("File " + filename + " cannot be uploaded. Cannot read given file.");
            }
        });
    }

    @Override
    public Page<FileDTO> searchFile(Pageable pageable, String phrase) {
        checkIfWorkDirExist();
        Optional<Sort.Order> sortOrder = Optional.ofNullable(pageable.getSort().getOrderFor("filename"));
        List<File> files = Optional.ofNullable(new File(localProperties.getWorkDir()).listFiles())
                .map(Arrays::stream)
                .orElseGet(Stream::empty)
                .collect(Collectors.toList());
        Stream<File> objects = files.stream()
                .filter(file -> file.getName().startsWith(phrase.trim()));
        if (sortOrder.isPresent()) {
            if (sortOrder.get().isAscending()) {
                objects = objects.sorted(Comparator.comparing(File::getName));
            } else {
                objects = objects.sorted((obj1, obj2) -> -obj1.getName().compareTo(obj2.getName()));
            }
        }
        List<FileDTO> result = objects.skip(pageable.getPageNumber()*pageable.getPageSize())
                .limit(pageable.getPageSize())
                .map(obj -> new FileDTO(obj.getName(), obj.length()))
                .collect(Collectors.toList());
        return new PageImpl<>(result, pageable, files.size());
    }

    @Override
    public void removeFile(String filename) {
        checkIfWorkDirExist();
        try {
            Files.delete(Path.of(localProperties.getWorkDir() + '/' + filename.trim()));
            log.info("File " + filename.trim() + " has been deleted from S3 bucket.");
        } catch (IOException e) {
            log.info("File " + filename.trim() + " doesn't exist.");
        }
    }

    @Override
    public byte[] downloadFile(String filename) throws IOException {
        checkIfWorkDirExist();
        File file = new File(localProperties.getWorkDir() + '/' + filename.trim());
        if (!file.exists()) {
            throw new EntityNotFoundException("File " + filename.trim() + " doesn't exist.");
        }
        return new FileInputStream(file).readAllBytes();
    }

    private void checkIfWorkDirExist() {
        if (!Files.exists(Path.of(localProperties.getWorkDir()))) {
            new File(localProperties.getWorkDir()).mkdir();
        }
    }

}
