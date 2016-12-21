package com.technical.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class Utils {

    @Autowired
    FileSystem fileSystem;

    @Autowired
    Path path;

    public Path createByPath(String name) {


        try {
            return Files.createDirectories(fileSystem.getPath(name));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
