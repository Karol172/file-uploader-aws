package com.kcymerys.java.fileuploader;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface FileService {

    void uploadFile(Set<MultipartFile> multipartFiles);

    Page<File> searchFile (Pageable pageable, String phrase);

}
