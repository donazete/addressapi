package com.echo.addressapi.addressapi;

import javax.validation.constraints.NotBlank;

public class AddressRequest {
    @NotBlank
    private String address1;

    public AddressRequest(){
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1){
        this.address1 = address1;
    }
}