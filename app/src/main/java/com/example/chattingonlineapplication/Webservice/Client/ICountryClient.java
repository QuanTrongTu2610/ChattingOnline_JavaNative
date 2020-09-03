package com.example.chattingonlineapplication.Webservice.Client;

import com.example.chattingonlineapplication.Webservice.Input.CountryInformationInput;
import com.example.chattingonlineapplication.Webservice.Model.CountryModel;
import com.example.chattingonlineapplication.Webservice.Output.BaseOutput;

import bolts.Task;

public interface ICountryClient {
    Task<BaseOutput<CountryModel[]>> getCountryInformation(CountryInformationInput countryInformationInput);
}
