package com.ftec.utils;

import com.ftec.resources.Resources;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;

public class Logger {

    public static void log(String message){
        String callerName = Thread.currentThread().getStackTrace()[1].getClassName();
        String messageCompleted = new Date()+"["+callerName+"]"+message;
        if(!Resources.loggerEnabledStatic){
            System.out.println(messageCompleted);
            return;
        }
        try {
            logToFile("/Logs/"+callerName+".txt", messageCompleted);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void logException(String location, Exception e, boolean printStackTrace){
        if(!Resources.loggerEnabledStatic){
            e.printStackTrace();
            return;
        }
        StringBuilder message = new StringBuilder("\n\n").append(location);
        if(printStackTrace) message.append("\nSOE-------------------------------------------------");
        message.append("\n").append(e.getClass()).append(", message: ").append(e.getMessage());
        if(printStackTrace)
            for(StackTraceElement ste: e.getStackTrace()){
                message.append(ste.toString()).append("\n");
            }
        if(printStackTrace) message.append("EOE-------------------------------------------------");
        try {
            String callerName = Thread.currentThread().getStackTrace()[1].getClassName();
            logToFile("/exceptions/"+callerName+".txt", message.toString());
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private static void logToFile(String path, String message) throws Exception{
        File f = new File(path);
        if(f.getParentFile().mkdirs() && f.createNewFile())
            try (FileWriter fw = new FileWriter(f, true)) {
                fw.write(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
