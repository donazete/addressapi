package com.echo.addressapi.addressapi;

import org.json.JSONException;
import org.json.XML;
import java.io.File;

public class Utility{
    

    // WRITE METHOD TO CONVERT XML TO JSON
    protected String convertToJson(String XMLContent){
        org.json.JSONObject jsonObj = null;
        String response = null;

        try {
			jsonObj = XML.toJSONObject(XMLContent);
			response = jsonObj.toString(2);
			//System.out.println(jsonObj);
		} catch (JSONException ex) {
			ex.printStackTrace();
        }
        
        return response;
    }

    // Method to parse full file path and return only file path
    protected String setOutputFile(String name){
        String amendedName = null;
        
        try{
            amendedName = name.replace(".xml", ".txt");
        
        }catch(Exception ex){
            System.out.println("Exception occured: "+ ex.toString());
        }
        
        return amendedName;
    }

    protected int fileCount(String filePath){
        File directory=new File(filePath);
        int count = directory.list().length;
        return count;
    }

    protected String fileExtensionCheck(String fileName){
        String response = "false";
        if (fileName.substring(fileName.length() - 3).equals(".MD")){
            response = "true";
        }
        return response;
    }
}