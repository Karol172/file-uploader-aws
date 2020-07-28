package com.kcymerys.java.fileuploader;

import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface FileService {

    void uploadFile(Set<MultipartFile> multipartFiles);

}
