package com.kcymerys.java.fileuploader;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="FILE")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "FILENAME", nullable = false, unique = true)
    private String filename;

    @Column(name = "SIZE", nullable = false)
    private Long size;

    @Column(name = "TIMESTAMP", nullable = false)
    private Timestamp timestamp;

}
