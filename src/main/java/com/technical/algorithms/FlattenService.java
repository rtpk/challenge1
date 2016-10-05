package com.technical.algorithms;

import java.io.File;

/**
 * Created by Robert Piotrowski on 04/10/2016.
 */

public class FlattenService {


    public static void showFiles(File[] files) {
        for (File file : files) {
            if (file.isDirectory()) {
                System.out.println("Directory: " + file.getName());
                showFiles(file.listFiles()); // Calls same method again.
            } else {
                System.out.println("File: " + file.getName());
            }
        }
    }

}
