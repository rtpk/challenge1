package com.technical;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.nio.file.FileSystem;

@SpringBootApplication
class ChallengeApplication {

    @Bean
    public FileSystem fileSystem() {
        return Jimfs.newFileSystem(Configuration.unix());
    }

    public static void main(String[] args) {
        SpringApplication.run(ChallengeApplication.class, args);
    }
}
