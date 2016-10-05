package com.technical.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

/**
 * Created by Robert Piotrowski on 05/10/2016.
 */
@Slf4j
@RestController
public class Controller {

    @RequestMapping(value = "/addFile", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getActiveOrders(@RequestParam String name) {
        File newFile = new File(name);
        try {
            newFile.createNewFile();
        } catch (IOException e) {
            log.error("Error while creating new file {}",e.getCause().toString());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
