package com.example.chattingonlineapplication.Webservice.Provider;

public interface IAuthorizationProvider {

    String authorize();

    void setAccessToken(String accessToken);

}
