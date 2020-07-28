package com.kcymerys.java.fileuploader;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class FileDTO {

    private Long id;
    private String filename;
    private Long size;
    private Timestamp timestamp;

}
