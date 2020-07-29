package com.kcymerys.java.fileuploader;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/api/file")
public class FileController {

    private final FileService fileService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create (@RequestParam("file") Set<MultipartFile> multipartFiles){
        fileService.uploadFile(multipartFiles);
    }

    @GetMapping
    public Page<FileDTO> search (Pageable pageable,
                                 @RequestParam(value = "phrase", defaultValue = "") String phrase) {
        return fileService.searchFile(pageable, phrase);
    }

    @GetMapping("/{filename}")
    public ResponseEntity<byte[]> download (@PathVariable("filename") String filename) throws IOException {
        return ResponseEntity.ok()
                .body(fileService.downloadFile(filename));
    }

    @DeleteMapping("/{filename}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove (@PathVariable("filename") String filename) {
        fileService.removeFile(filename);
    }

}

// TODOLIST
// TODO: Swagger docs
// TODO: Dockerize app