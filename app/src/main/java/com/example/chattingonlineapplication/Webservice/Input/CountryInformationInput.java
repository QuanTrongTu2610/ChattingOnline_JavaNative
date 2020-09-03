package com.example.chattingonlineapplication.Webservice.Input;

import com.example.chattingonlineapplication.Webservice.CountryAPIs;
import com.example.chattingonlineapplication.Webservice.Input.BaseInput;

public class CountryInformationInput extends BaseInput {

    @Override
    public String getResource() {
        return CountryAPIs.GET_COUNTRY_NAME;
    }

    @Override
    public HTTPMethod getMethod() {
        return HTTPMethod.GET;
    }
}
