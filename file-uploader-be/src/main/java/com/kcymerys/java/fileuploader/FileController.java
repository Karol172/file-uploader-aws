package com.kcymerys.java.fileuploader;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/api/file")
public class FileController {

    private final FileService fileService;
    private final ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create (@RequestParam("file") Set<MultipartFile> multipartFiles){
        fileService.uploadFile(multipartFiles);
    }

    @GetMapping()
    public Page<FileDTO> search (Pageable pageable,
                                 @RequestParam(value = "phrase", defaultValue = "") String phrase) {
        return fileService.searchFile(pageable, phrase).map(file -> modelMapper.map(file, FileDTO.class));
    }

}
