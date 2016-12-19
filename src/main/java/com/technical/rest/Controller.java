package com.technical.rest;

import com.technical.file.NodePath;
import com.technical.node.NodeIterable;
import com.technical.rx.PathRx;
import com.technical.services.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rx.Observable;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.ArrayList;
import java.util.List;


@RestController
public class Controller {

    private FileSystem fileSystem;
    private PathRx pathRx;
    private Observable<WatchEvent<?>> observable;
    private final Utils utils;
    private final SimpMessagingTemplate template;

    @Autowired
    public Controller(Utils utils, SimpMessagingTemplate template, FileSystem fileSystem, PathRx pathRx) {
        this.utils = utils;
        this.fileSystem = fileSystem;
        this.pathRx = pathRx;
        this.template = template;
    }


    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public ResponseEntity register() throws IOException, InterruptedException {
        Files.createDirectories(fileSystem.getPath("/root/test54/asd"));
        Files.createDirectories(fileSystem.getPath("/root/test524/22asd"));
        NodeIterable<Path> root = new NodeIterable<>(new NodePath(fileSystem.getPath("/root")));
        List<String> treeFile = new ArrayList<>();
        root.forEach(p -> treeFile.add(p.toAbsolutePath().toString()));
        return new ResponseEntity<>(treeFile, HttpStatus.OK);
    }


    @RequestMapping(value = "/addFile", method = RequestMethod.GET)
    public ResponseEntity getActiveOrders(@RequestParam String name) {

        if (utils.createByPath(name) != null)
            return new ResponseEntity<>(HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
