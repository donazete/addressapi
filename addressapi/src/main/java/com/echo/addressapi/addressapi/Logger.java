package com.echo.addressapi.addressapi;

//import java.util.UUID;
import java.util.Date;
import static org.apache.commons.lang3.RandomStringUtils.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import com.google.gson.Gson;
//import com.google.gson.JsonObject;
import com.google.gson.GsonBuilder;

public class Logger{
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Map<String, Object> subData = new HashMap<String, Object>();
    //GsonBuilder gsonMapBuilder = new GsonBuilder();
    //Gson gsonObject = gsonMapBuilder.create();
    Gson gsonObject = new GsonBuilder().setPrettyPrinting().create();

    public Logger(){
        
    }

    protected String generateLoggerID(){
        String logid = null;
        int length = 10;
        Boolean useLetters = true;
        Boolean useNumbers = true;

        logid = random(length, useLetters, useNumbers);
        
        return logid;
    }

    protected String logFormatter(String app, Map<String, Object> subData){
        Map<String, Object> jsonMessage = new HashMap<String, Object>();
        Map<String, Object> subMessage = new HashMap<String, Object>();
        String jsonObj = null;
    
        jsonMessage.put("timestamp", LocalDateTime.now());
        jsonMessage.put("level", "DEBUG");
        subMessage.put("response", 200);
        subMessage.put("status", "OK");
        jsonMessage.put("metadata", subMessage);
        jsonMessage.put("source", app);
        jsonMessage.put("message", subData);
        jsonObj =  gsonObject.toJson(jsonMessage);
        jsonMessage.clear();
        return jsonObj;
    }

    protected void logOps(String logID, String type, String data, String app, String method, Date dt, long duration, int logStyle){

        if (logStyle == 1){
            logConsole(logID, type, data, app, method, dt, duration);
        }else if (logStyle == 2){
            logDb(logID, type, data, app, method, dt, duration);
        }else {
            logFile(logID, type, data, app, method, dt, duration);
        }
    }

    private void logConsole(String logID, String type, String data, String app, String method, Date dt, long duration){
        subData.put("calltime", dateFormat.format(dt));
        subData.put("logid", logID);
        subData.put("logtype", type);
        subData.put("data", data);
        subData.put("source", app);
        subData.put("method", method);
        subData.put("duration", duration +" sec");
        System.out.println(logFormatter(app, subData));
        subData.clear();
    }

    private void logDb(String logID, String type, String data, String app, String method,Date dt, long duration){

    }

    private void logFile(String logID, String type, String data, String app, String method,Date dt, long duration){
        FileOutputStream fos = null;
        File file = new File("AddressApiLog.txt");
        subData.put("calltime", dateFormat.format(dt));
        subData.put("logid", logID);
        subData.put("logtype", type);
        subData.put("data", data);
        subData.put("source", app);
        subData.put("method", method);
        subData.put("duration", duration +" sec");
        String mycontent = "\r\n"+ logFormatter(app, subData);
        subData.clear();

        try{
            if (!file.exists()){
                file = new File("AddressApiLog.txt");
            }
            
            fos = new FileOutputStream(file, true);
            byte[] bytesArray = mycontent.getBytes();

            fos.write(bytesArray);
            fos.flush();
            fos.close();
        }catch (IOException ex){
            subData.put("error", ex.toString());
            System.out.println(logFormatter("Logger.java", subData));
        }
    }
}