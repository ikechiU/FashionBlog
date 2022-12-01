package com.example.blog.repositories;

import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Repository
public class FileSystemRepository {

    String RESOURCE_DIR = FileSystemRepository.class.getResource("/").getPath();

    public String save(byte[] content) throws IOException {
        Path newFile = Paths.get(RESOURCE_DIR + System.currentTimeMillis());
        Files.createDirectories(newFile.getParent());
        Files.write(newFile, content);
        return newFile.toAbsolutePath().toString();
    }
}
