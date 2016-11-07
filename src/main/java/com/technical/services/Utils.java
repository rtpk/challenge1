package com.technical.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public class Utils {

    public boolean createFileByPath(String name) {
        File newFile = new File(name);
        try {
            newFile.createNewFile();
        } catch (IOException e) {
            log.error("Error while creating new file {}",e.getCause().toString());
             return false;
        }
        return true;

    }

}
