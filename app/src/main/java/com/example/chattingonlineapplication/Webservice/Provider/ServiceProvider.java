package com.example.chattingonlineapplication.Webservice.Provider;

import com.example.chattingonlineapplication.Webservice.BaseClient;
import com.example.chattingonlineapplication.Webservice.Client.CountryClient;
import com.example.chattingonlineapplication.Webservice.Client.ICountryClient;

public class ServiceProvider {

    private IHttpHeaderProvider httpHeaderProvider;
    private ICountryClient iCountryClient;

    private ServiceProvider () {
        httpHeaderProvider = new HttpHeaderProvider();
        iCountryClient = new CountryClient(httpHeaderProvider);
    }
    private static class SingletonHelper {
        private static final ServiceProvider INSTANCE = new ServiceProvider();
    }

    public static ServiceProvider getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public ICountryClient getCountryClient() {
        return iCountryClient;
    }
}
