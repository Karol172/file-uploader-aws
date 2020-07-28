package com.kcymerys.java.fileuploader;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    @Query("SELECT f FROM File f WHERE LOWER(f.filename) = LOWER(:filename)")
    Optional<File> findByFilename (@Param("filename") String filename);

    @Query("SELECT f FROM File f WHERE LOWER(f.filename) LIKE CONCAT('%', LOWER(:phrase), '%')")
    Page<File> searchByFilename (Pageable pageable, @Param("phrase") String phrase);

}
