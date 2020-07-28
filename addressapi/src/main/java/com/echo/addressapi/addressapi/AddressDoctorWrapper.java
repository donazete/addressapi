package com.echo.addressapi.addressapi;

import com.addressdoctor.AddressDoctor;
import com.addressdoctor.AddressDoctorException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import com.addressdoctor.AddressObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
//import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import java.text.ParseException;
import java.util.*;
/*import java.lang.reflect.Method;
import java.lang.Class;
import org.json.JSONException;
import org.json.XML;*/


public class AddressDoctorWrapper {
    Gson gson = new Gson();
    int iLastError = 0;
    //private static AddressObject myAddyObj;
    String message = "";
    String version = "";
    Utility utility = new Utility();
    org.json.JSONObject jsonObj = null;
    Map<String, Object> sub = new HashMap<String, Object>();
    Logger log = new Logger();
    String logid = null; 
    DateFormat dateFormat = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
    DateFormat print = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date date = null;
    Date dateAfter = null;
    long duration = 0;
    
    public AddressDoctorWrapper() {
    }

    // Method for initializing Address doctor
    public String addressDrInit() {
        
        int iLastError = 0;
        //System.out.println(new File(".").getAbsolutePath());
        /*try{
            
            BufferedReader oXMLFile = new BufferedReader(new FileReader("SetConfig.xml"));
            System.out.println(oXMLFile);
        }catch(FileNotFoundException fex){
            message = fex.toString() + ". Error message: " + fex.getMessage();
        }*/
        try
        {

            //AddressDoctor.initialize(null, ".\\config\\SetConfig.xml", null, ".\\config\\Parameters.xml");
            AddressDoctor.initialize(null, "config3//SetConfig.xml", null, "config//Parameters.xml");
            iLastError = AddressDoctor.getLastError();
            version = AddressDoctor.getVersion();
            message = "Init returned " + iLastError + ". AddressDoctor " + version + " initialized!";
            //AddressDoctor.deinitialize();
        } catch (AddressDoctorException ex)
        {
            message = "Exception while Initializing the AddressDoctor " + ex.toString() + ". Error message: " + ex.getExtendedMessage();
            
            if (ex.isCriticalError() || (ex.isError() && ex.getNumber() > 902 ))
            {
                message = message + ". Further processing not possible, application ends!";	
            }
            
        }
        
        //System.out.println(message);
        return message;
    }

    // Method to initialize using fast complete mode
    public String addressDrInitFastComplete() {
        
        int iLastError = 0;
        //System.out.println(new File(".").getAbsolutePath());
        
        try
        {

            //AddressDoctor.initialize(null, ".\\config2\\SetConfig.xml", null, ".\\config2\\Parameters.xml");
            AddressDoctor.initialize(null, "config3//SetConfig.xml", null, "config2//Parameters.xml");
            iLastError = AddressDoctor.getLastError();
            iLastError = AddressDoctor.getLastError();
            version = AddressDoctor.getVersion();
            message = "Init returned " + iLastError + ". AddressDoctor " + version + " initialized!";
            //AddressDoctor.deinitialize();
        } catch (AddressDoctorException ex)
        {
            message = "Exception while Initializing the AddressDoctor in fast completion mode " + ex.toString() + ". Error message: " + ex.getExtendedMessage();
            
            if (ex.isCriticalError() || (ex.isError() && ex.getNumber() > 902 ))
            {
                message = message + ". Further processing not possible, application ends!";	
            }
            
        }
        
        //System.out.println(message);
        return message;
    }

    //Method to get Address object
    private static AddressObject getAddyObj(){
        String message;
        AddressObject activeAddyObj = null;
       
        try
		{
			activeAddyObj = AddressDoctor.getAddressObject();
		} catch (AddressDoctorException ex)
		{
			message = "Exception while trying to get an AddressObject " + ex.toString();
			message += ". Error message: " + ex.getExtendedMessage();
            message += ". Further processing not possible, application ends!";
            formatOutput(message, "error");
            //System.out.println(message);
			/*try
			{
				AddressDoctor.deinitialize();
			} catch (AddressDoctorException ex2){}*/
				
        }
        
        return activeAddyObj; 
    }

    
    // WRITE METHOD TO PROCESS INPUT FILE
    private static void processInputFile(String fileName, AddressObject myAddyObj){
        String message = null;
        
        if (fileName.length() > 0) //&& fileName.compareToIgnoreCase("-xml") == 0
		{
			//System.out.println("Assign the address using XML");
			//System.out.println("============================");
			
			try
			{
				assignAsXML(fileName, myAddyObj);
			} catch (Exception ex)
			{
                message = "Data could not be assigned! Closing application! ";
			    message += "Exception while trying to assign into AddressObject " + ex.toString();
                formatOutput(message, "error");
				/* try
				{
					AddressDoctor.releaseAddressObject(myAddyObj);
					AddressDoctor.deinitialize();
				} catch (AddressDoctorException ex2){}*/
				
				return;
			}
		}
    }

    // Method to consume input file
    static void assignAsXML (String sInputDataXMLFileName, AddressObject myAddyObj) throws Exception
	{	BufferedReader oXMLFile;
		String sInputDataXML = "";
        String sLine;

		try
		{
			oXMLFile = new BufferedReader(new FileReader(sInputDataXMLFileName));
		} catch (Exception ex)
		{
            formatOutput("Could not open the XML File '" + sInputDataXMLFileName +"'", "error");
            throw ex;
		}
		
		while ((sLine = oXMLFile.readLine()) != null)
		{
			sInputDataXML += sLine;
		}
		
        myAddyObj.setInputDataXML(sInputDataXML);
        oXMLFile.close();
	}

    // METHOD TO PROCESS DIRECT INPUT
    private static void processInputDirect(String addy, String housenum, String locality, String postcode, String province, String country, AddressObject myAddyObj){
        // Should accept parameters from a post action
        //System.out.printf("Data to be assigned: %s, %s, %s, %s, %s, %s", addy, housenum, locality, postcode, province, country);
        
        try
        {
            myAddyObj.setInputAddressElement("Street", 1, null, addy);
            myAddyObj.setInputAddressElement("Number", 1, null, housenum);
            myAddyObj.setInputAddressElement("Locality", 1, null, locality);
            myAddyObj.setInputAddressElement("PostalCode", 1, null, postcode);
            myAddyObj.setInputAddressElement("Province", 1, null, province);
            myAddyObj.setInputAddressElement("Country", 1, null, country);
        } catch (Exception ex)
        {
            formatOutput("Data could not be assigned! Closing application! "+ ex.toString(), "error");
            /*try
            {
                AddressDoctor.releaseAddressObject(myAddyObj);
                AddressDoctor.deinitialize();
            } catch (AddressDoctorException ex2){}*/    
        }
        //return myAddyObj;
    }

    // Method that receives address, processes it and returns output

    // Prints the XML Settings and Input 
	static void showInputXML (AddressObject myAddyObj)
	{
        String sXMLOutput = "";
        		 
		try
		{
			sXMLOutput = AddressDoctor.getConfigXML();
		} catch (AddressDoctorException ex)
		{
            formatOutput("Exception while trying to get Config XML: " + ex.toString(), "error");
			return;
		}
		formatOutput("GetConfig.xml: "+ sXMLOutput, "message");
		try
		{
			sXMLOutput = myAddyObj.getParametersXML();
		} catch (AddressDoctorException ex)
		{
			formatOutput("Exception while trying to get Parameters XML: " + ex.toString(), "error");
			return;
		}
		formatOutput("Parameters.xml: "+ sXMLOutput, "message");
		
        try
        {
            sXMLOutput = myAddyObj.getInputDataXML();
        } catch (AddressDoctorException ex)
        {
            formatOutput("Exception while trying to get Input XML: " + ex.toString(), "error");
            return;
        }
		//System.out.println("InputData.xml:");
		formatOutput(sXMLOutput, "message");
    }
    
    static void formatOutput(String message, String type){
        Logger logging = new Logger();
        Map<String, Object> subbed = new HashMap<String, Object>();
        subbed.put(type, message);
        System.out.println(logging.logFormatter("AddressDoctorWrapper.java", subbed));
        subbed.clear();
    }

    // POST API method. Accept parameters via post, processes the content and returns response in JSON
    public String processApiRequest(String addy, String housenum, String locality, String postcode, String province, String country, String output){
        String initString = addressDrInit();
        //System.out.println("Processing address with the Direct API call setting:");
        formatOutput("================== "+initString, "message");
        AddressObject addyObj = getAddyObj();
        //System.out.println("================== "+addyObj);       
        //JsonObject result = new JsonObject();
        String input = "Address: "+ addy + "; House No: "+ housenum +"; Locality: "+ locality +"; Post code: "+ postcode +"; Province: "+ province +"; Country: "+ country +"; Output type: "+ output ;
        String result = null;
        logid = log.generateLoggerID();
        try{
            date = dateFormat.parse(new Date().toString());
            //date = print.parse(date.toString());
        }catch (Exception ex){
            formatOutput(ex.toString(), "error");
        }
        
        log.logOps(logid, "input", input, "AddressDoctorWrapper.java", "processApiRequest - Direct", date, duration, 1);
        processInputDirect(addy, housenum, locality, postcode, province, country, addyObj);
                
        try
		{            
            //showInputXML();
			AddressDoctor.process(addyObj);
			iLastError = addyObj.getLastError();
            formatOutput("Process returned " + iLastError, "message");
            if (output.equals("summary"))
            {
                result =  getResultByElement(addyObj).toString() ;  //getFullResult(addyObj).toString();
                //result = jsonObj.toString(4);
            }
            else if (output.equals("full")){
                //result = getResultByElement(addyObj).toString();
                result = addyObj.getResultXML();
                //formatOutput(result, "message");
                result = utility.convertToJson(result);
            }
            else{
                result = "Wrong output format! Available formats are full or summary.";
            }
            
		} catch (AddressDoctorException ex)
		{
			result = "Exception during process!\n" + ex.toString();
            result += "Error message: " + ex.getExtendedMessage();
            formatOutput(result, "error");
        }

        // Convert result to JSON
        
        
        try
        {
            AddressDoctor.releaseAddressObject(addyObj);
            AddressDoctor.deinitialize();
        } catch (AddressDoctorException ex2){}

        try{
            dateAfter = dateFormat.parse(new Date().toString());
            //dateAfter = print.parse(dateAfter.toString());
        }catch (Exception ex){
            formatOutput(ex.toString(), "error");
        }
        
        duration = (dateAfter.getTime() - date.getTime()) / 1000;
        log.logOps(logid, "output", result, "AddressDoctorWrapper.java", "processApiRequest - Direct", dateAfter, duration, 1);
        return result;
    }

    public String processApiRequest(String fullFilePath) throws IOException{
        String initString = addressDrInit();
        formatOutput("Processing address with the File option setting:", "message");
        formatOutput("=================="+ initString, "message");
        String getOutputFilePath = utility.setOutputFile(fullFilePath);
        //System.out.println("Output file path is: " + getOutputFilePath);
        AddressObject addyObj = getAddyObj();
        processInputFile(fullFilePath, addyObj);
        //System.out.println("================== "+addyObj);
        String message = null;
        logid = log.generateLoggerID();
        try{
            date = dateFormat.parse(new Date().toString());
            //date = print.parse(date.toString());
        }catch (Exception ex){
            formatOutput(ex.toString(), "error");
        }
        
        log.logOps(logid, "input", fullFilePath, "AddressDoctorWrapper.java", "processApiRequest - FileRequest", date, duration, 1);
        
        try
		{
			//showInputXML();
			AddressDoctor.process(addyObj);
            iLastError = addyObj.getLastError();
            resultXMLToFile(getOutputFilePath, addyObj);
            //System.out.println("Process returned " + iLastError);
            message = "Response written to file at location "+ getOutputFilePath;
		} catch (AddressDoctorException ex)
		{
			message = "Exception during process!\n" + ex.toString();
            message += " Error message: " + ex.getExtendedMessage();
            formatOutput(message, "error");
        }
        
        try
        {
            AddressDoctor.releaseAddressObject(addyObj);
            AddressDoctor.deinitialize();
        } catch (AddressDoctorException ex2){
            formatOutput(ex2.toString(), "error");
        }

        try{
            dateAfter = dateFormat.parse(new Date().toString());
            //dateAfter = print.parse(dateAfter.toString());
        }catch (Exception ex){
            formatOutput(ex.toString(), "error");
        }
        
        duration = (dateAfter.getTime() - date.getTime()) / 1000;
        log.logOps(logid, "output", message, "AddressDoctorWrapper.java", "processApiRequest - FileRequest", dateAfter, duration, 1);
        return message;
    }

    public String fastCompletionProcess(String input, String country){
        addressDrInitFastComplete();
        AddressObject addyObj = getAddyObj();
        //System.out.println(input);
        //System.out.println(country);
        //String initResult = null;
        String result = null;
        logid = log.generateLoggerID();
        
        try{
            date = dateFormat.parse(new Date().toString());
            //date = print.parse(date.toString());
        }catch (Exception ex){
            //System.out.println("Date parse error 1 "+ex);
            formatOutput(ex.toString(), "error");
        }
        
        log.logOps(logid, "input", input, "AddressDoctorWrapper.java", "fastCompletionProcess", date, duration, 1);
        // Assign input to address dr object
        try{
            addyObj.setInputAddressElement("Street", 1, null, input);
            addyObj.setInputAddressElement("Country", 1, null, country);
            AddressDoctor.process(addyObj);
            iLastError = addyObj.getLastError();
            result = addyObj.getResultXML().toString();
            //System.out.println("Initial result "+result);
            //addyObj.getResultParameter("ProcessStatus");
            //addyObj.getResultAddressComplete(arg0);
            //addyObj.getResultAddressElement(arg0, arg1, arg2, arg3);
            //addyObj.getResultEnrichmentElement(arg0, arg1, arg2);
        } catch(AddressDoctorException ex){
            message = "Exception during fast completion processing!\n" + ex.toString();
            message += "Error message: " + ex.getExtendedMessage();
            formatOutput(message, "error");
        }

        // Convert result to JSON
        result = utility.convertToJson(result);
        //System.out.println("Final result "+result);

        try
        {
            AddressDoctor.releaseAddressObject(addyObj);
            AddressDoctor.deinitialize();
        } catch (AddressDoctorException ex2){
            formatOutput(ex2.toString(), "error");
        }

        try{
            dateAfter = dateFormat.parse(new Date().toString());
            //dateAfter = print.parse(dateAfter.toString());
        }catch (Exception ex){
            //System.out.println("Date parse error 2 "+ex.toString());
            formatOutput(ex.toString(), "error");
        }
        
        duration = (dateAfter.getTime() - date.getTime()) / 1000;
        log.logOps(logid, "output", result, "AddressDoctorWrapper.java", "fastCompletionProcess", dateAfter, duration, 1);

        return result;
    }

    static void resultXMLToFile (String fullFilePath, AddressObject addy) throws IOException
	{
		String sXMLOutput = "";
		FileWriter ResultXML = new FileWriter(new File(fullFilePath));
		
		try
		{
            sXMLOutput = addy.getResultXML();
            //System.out.println("Addy is: "+addy);
		} catch (AddressDoctorException ex)
		{
			formatOutput("Exception while trying to get Result XML: " + ex.toString(), "error");
			return;
		}
		formatOutput("Result written to Result.xml: " + sXMLOutput, "message");
 		ResultXML.write(sXMLOutput);
		ResultXML.close();
    }

    public String filesHealthCheck(){
        String response = null;
        String result = null;
        String path = "/home/refdata";
        //String path = "F:\\WorkFiles\\ReferenceData";
        //File folder = new File();
        File folder = new File(path);
        int count = utility.fileCount(path);
        File[] listOfFiles = folder.listFiles();
        int mdcounter = 0;
        Map<String, Object> mainData = new HashMap<String, Object>();
        Map<String, Object> subData = new HashMap<String, Object>();
        Map<String, Object> subDatax = new HashMap<String, Object>();
        Gson gsonObject = new GsonBuilder().setPrettyPrinting().create();
        mainData.put("fileCount", count);
        
        for (int i = 0; i < listOfFiles.length; i++){
            result = utility.fileExtensionCheck(listOfFiles[i].getName());
            subDatax.put("isMdFile", result);
            subDatax.put("timestamp", new Date().toString());
            subDatax.put("status", "Healthy");
            System.out.println("Boolean check: "+subDatax);
            //subData.clear();
            if (result.equals("true")){
                mdcounter++;
            }
            subData.put(listOfFiles[i].getName()+"", subDatax);
            //subDatax.clear();
        }
        mainData.put("Files", subData);
        mainData.put("mdFileCount", mdcounter);
        response = gsonObject.toJson(mainData);
        subData.clear();
        mainData.clear();
        return response;
    }
    
    // Returns result as a single response
    static JsonObject getFullResult(AddressObject addyObj){
        int iResultCount = 0;
        //List <String> sOutputBuffer = new ArrayList<String>();
        JsonObject sOutputBuffer = new JsonObject();
		
		try
		{
			iResultCount = addyObj.getResultCount();
		} catch (AddressDoctorException ex)
		{
			System.out.println("Exception while trying to get the ResultCount: " + ex.toString());
		}
		System.out.println("Result count is " + iResultCount);
		
		for (int i = 1; i <= iResultCount; i++)
		{
			System.out.println("-  Result Nr. " + i);
			System.out.println("---------------------------");
			
			// Get the complete Address
			System.out.println("AddressComplete");
			System.out.println("---------------");
			try
			{
				sOutputBuffer.addProperty("Address"+i, addyObj.getResultAddressComplete(i));
                System.out.println(sOutputBuffer);
			} catch (AddressDoctorException ex)
			{
				System.out.println("Exception while trying to get AddressComplete: " + ex.toString());
            }
        }
        return sOutputBuffer;
    }

    // Returns result broken down to constituent elements
    static JsonObject getResultByElement(AddressObject addy){
        int iResultCount = 0;
        //String sOutputAddress = "";
        JsonObject json = new JsonObject();
        //JsonArray jsonArray = new JsonArray();
        JsonObject mainjsonlist = new JsonObject();
        JsonObject jsonlist = new JsonObject();
        JsonObject jsonsublist = new JsonObject();
				
		try
		{
			iResultCount = addy.getResultCount();
		} catch (AddressDoctorException ex)
		{
			System.out.println("Exception while trying to get the ResultCount: " + ex.toString());
		}
		System.out.println("Result count is " + iResultCount);
		
		for (int i = 1; i <= iResultCount; i++)
		{
			System.out.println("-  Result Nr. " + i);
            System.out.println("---------------------------");
            
			System.out.println("Address elements");
			System.out.println("---------------");
			try
			{
                jsonlist.addProperty("Street", addy.getResultAddressElement(i, "Street",1, "COMPLETE"));
                jsonlist.addProperty("Number", addy.getResultAddressElement(i, "Number",1, "COMPLETE"));
                jsonlist.addProperty("PostalCode", addy.getResultAddressElement(i, "PostalCode",1, "FORMATTED"));
                jsonlist.addProperty("Locality", addy.getResultAddressElement(i, "Locality",1, "NAME"));
                jsonlist.addProperty("Province", addy.getResultAddressElement(i, "Province",1, "COUNTRY_STANDARD"));
                jsonlist.addProperty("Country", addy.getResultAddressElement(i, "Country",1, "NAME_EN"));
                jsonsublist.addProperty("MailabilityScore", addy.getResultDataParameter(i, "MailabilityScore"));
                jsonsublist.addProperty("ElementInputStatus", addy.getResultDataParameter(i, "ElementInputStatus"));
                jsonsublist.addProperty("ElementResultStatus", addy.getResultDataParameter(i, "ElementResultStatus"));
                jsonsublist.addProperty("ElementRelevance", addy.getResultDataParameter(i, "ElementRelevance"));
                
			} catch (AddressDoctorException ex){
				System.out.println("Exception generated: " + ex.toString());
            } 
            mainjsonlist.add("Address"+i, jsonlist);
            mainjsonlist.add("Metadata"+i, jsonsublist);
		}
		// show the Process Status
		try
		{
            //jsonArray.add(mainjsonlist);
            json.add("result", mainjsonlist);
            json.addProperty("ProcessStatus", addy.getResultParameter("ProcessStatus"));
		} catch (Exception ex)//AddressDoctor
		{
			System.out.println("Exception while trying to get ProcessStatus: " + ex.toString());
        }
        
        return json;
    }
}