package com.ftec.logger;

import com.ftec.resources.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;

@Service
public class Logger {
    @Autowired
    private Resources resources;

    public enum Categories{
        INFO, BOTS, CHECKS, PAYMENTS, WARNING
    }
    private String[] categories = {"info", "bots", "checks", "payments", "warnings"};
    public void log(String filename, Categories category, String message){
        if(!resources.getLogger().isEnabled()){
            System.out.println(message);
            return;
        }
        try {
            logToFile(resources.getLogger().getPath()+categories[category.ordinal()]+"/"+filename, message+"\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void logException(String filename, String location, Exception e){
        if(!resources.getLogger().isEnabled()){
            e.printStackTrace();
            return;
        }
        String message ="\n\n"+location + "\n"+
                "SOE-------------------------------------------------";
        message+="\n"+e.getMessage()+"\n";
        for(int i=0;i<e.getStackTrace().length||i<resources.logger.exceptionHeight;i++){
            message+=e.getStackTrace()[i].toString()+"\n";
        }
        message+="EOE-------------------------------------------------";
        try {
            logToFile(resources.getLogger().getPath()+"exceptions/"+filename+".txt", message);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void logToFile(String path, String message) throws Exception{
        FileWriter fw = null;
        try{
            File f = new File(path);
            f.getParentFile().mkdirs();
            f.createNewFile();
            fw = new FileWriter(f, true);
            fw.write(message);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            fw.close();
        }
    }
}
