package com.ftec.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileUtils {
    public static String loadAsString(String path){
        try {
            File yourFile = new File(path);
            yourFile.getParentFile().mkdirs();
            yourFile.createNewFile();
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            return new String(encoded, Charset.forName("utf8"));
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }
    public static void appendToFile(String path, String textToAppend){
        try {
            Files.write(Paths.get(path), textToAppend.getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeFile(String path, String textToAppend){
        try {
            Files.write(Paths.get(path), textToAppend.getBytes(), StandardOpenOption.CREATE);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
