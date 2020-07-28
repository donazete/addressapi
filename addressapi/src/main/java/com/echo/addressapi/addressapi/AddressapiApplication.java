package com.echo.addressapi.addressapi;

//import javax.validation.Valid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
public class AddressapiApplication {
    public AddressDoctorWrapper apiObject = new AddressDoctorWrapper();
	public static void main(String[] args) {
		SpringApplication.run(AddressapiApplication.class, args);
    }
    
    @GetMapping("/")
     public String home(){
         return "Echo Address Verification API v1.0!";
     }
     
     @GetMapping("/health")
     public String healthCheck(){
         return apiObject.filesHealthCheck();
     }

    /*@GetMapping("/hello")
    public String hello(@RequestParam(value="name", defaultValue = "World")String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping("/validate")
    public String validateAddress(@Valid AddressRequest addressRequest){
        return apiObject.addressDrInit();
    }*/
    

    @PostMapping("/resolve")
    public String processFileRequest(@RequestParam(value="path")String path){
        String result = null;
        //System.out.println("Input parameter: "+ path);
                
        try{
            result = apiObject.processApiRequest(path);
        }catch(Exception ex){
            System.out.println("Exception occured: "+ ex.toString());
        }
        return result;
    }

    @PostMapping("/validate")
    public String processDirectRequest(@RequestParam(value="streetname") String addy, @RequestParam(value="housenum") String housenum, @RequestParam(value="city") String locality, 
                                        @RequestParam(value="postcode") String postcode, @RequestParam(value="state") String province, @RequestParam(value="country") String country, 
                                        @RequestParam(value="outputformat") String output){

        //System.out.printf("Data to be assigned: %s, %s, %s, %s, %s, %s", addy, housenum, locality, postcode, province, country);
        return apiObject.processApiRequest(addy, housenum, locality, postcode, province, country, output);

    }

    @PostMapping("/suggest")
    public String processFastCompletion(@RequestParam(value="input") String input, @RequestParam(value="country") String country){
        return apiObject.fastCompletionProcess(input, country);
    }
}
