package com.technical;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication
@EnableScheduling
public class ChallengeApplication {

    @Bean
    public FileSystem fileSystem() {
        return Jimfs.newFileSystem(Configuration.unix());
    }

    @Bean
    public Path path() throws IOException {
      Files.createDirectories(fileSystem().getPath("/root"));
      return  fileSystem().getPath("/root");
    }

    @Bean
    public TaskScheduler taskScheduler() {
        //org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
        return new ThreadPoolTaskScheduler();
    }

    public static void main(String[] args) {
        SpringApplication.run(ChallengeApplication.class, args);
    }
}
