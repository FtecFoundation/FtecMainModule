package com.ftec.utils.files;

import com.ftec.resources.Resources;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;

import static com.ftec.resources.Resources.loggerEnabledStatic;

public class Logger {

    public static void log(String message){
        String callerName = Thread.currentThread().getStackTrace()[1].getClassName();
        String messageCompleted = new Date()+"["+callerName+"]"+message;
        if(!loggerEnabledStatic){
            System.out.println(messageCompleted);
            return;
        }
        try {
            logToFile("/Logs/"+callerName+".txt", messageCompleted);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void logException(String location, Throwable e, boolean printStackTrace){
        StringBuilder message = new StringBuilder("\n\n").append(location);
        if(printStackTrace) message.append("\nSOE-----------").append(new Date().toString()).append("------------------------------------");
        message.append("\n").append(e.getClass()).append(", message: ").append(e.getMessage()).append("\n");

        if(printStackTrace)
            for(StackTraceElement ste: e.getStackTrace()){
                message.append(ste.toString()).append("\n");
            }
        Throwable cause = e.getCause();
        while(cause != null){
            message.append("\n**Caused by**\n").append(e.getClass()).append(", message: ").append(cause.getMessage()).append("\n");

            if(printStackTrace)
                for(StackTraceElement ste: cause.getStackTrace()){
                    message.append(ste.toString()).append("\n");
                }
            cause = cause.getCause();
        }
        if(printStackTrace) message.append("EOE-------------------------------------------------");

        if(!loggerEnabledStatic){
            System.out.println(message);
            return;
        }

        try {
            String callerName = null;
            for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
                if(!stackTraceElement.getClassName().contains("Logger")) {
                    callerName = stackTraceElement.getClassName();
                    break;
                }
            }
            logToFile("/exceptions/"+(callerName!=null?callerName.substring(callerName.lastIndexOf(".")+1)+".txt":"Unknown"), message.toString());
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
