package com.kcymerys.java.fileuploader;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="FILE")
@Data
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "FILENAME", nullable = false)
    private String filename;

    @Column(name = "PATH", unique = true, nullable = false)
    private String path;

    @Column(name = "SIZE", nullable = false)
    private Long size;

    @Column(name = "TIMESTAMP", nullable = false)
    private Timestamp timestamp;

}
