package com.technical;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication
class ChallengeApplication {

    @Bean
    public FileSystem fileSystem() {
        return Jimfs.newFileSystem(Configuration.unix());
    }

    @Bean
    public Path path() throws IOException {
      Files.createDirectories(fileSystem().getPath("/root/test"));
      return  fileSystem().getPath("/root");
    }


    public static void main(String[] args) {
        SpringApplication.run(ChallengeApplication.class, args);
    }
}
