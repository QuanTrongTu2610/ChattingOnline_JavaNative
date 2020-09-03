package com.example.chattingonlineapplication.Webservice.Client;

import com.example.chattingonlineapplication.Webservice.BaseClient;
import com.example.chattingonlineapplication.Webservice.Input.CountryInformationInput;
import com.example.chattingonlineapplication.Webservice.Model.CountryModel;
import com.example.chattingonlineapplication.Webservice.Output.BaseOutput;
import com.example.chattingonlineapplication.Webservice.Provider.IHttpHeaderProvider;

import bolts.Task;
import okhttp3.HttpUrl;

public class CountryClient extends BaseClient implements ICountryClient {

    public CountryClient(IHttpHeaderProvider httpHeaderProvider) {
        super(httpHeaderProvider);
    }
    
    @Override
    protected HttpUrl baseUrl() {
        HttpUrl.Builder builder = new HttpUrl.Builder()
                .scheme("https")
                .host("restcountries.eu")
                .addPathSegment("rest")
                .addPathSegment("v2");

        return builder.build();
    }

    @Override
    public Task<BaseOutput<CountryModel[]>> getCountryInformation(CountryInformationInput countryInformationInput) {
        return executeAsync(countryInformationInput, CountryModel[].class);
    }
}
