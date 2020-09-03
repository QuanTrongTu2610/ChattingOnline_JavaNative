package com.example.chattingonlineapplication.Webservice.Input;

import java.util.Hashtable;

public abstract class BaseInput {
    public enum HTTPMethod {
        GET,
        DELETE,
        PUT,
        POST
    }

    private Hashtable<String, String> queryParamters;
    private Hashtable<String, String> pathSegments;

    public abstract String getResource();

    public abstract HTTPMethod getMethod();

    public Hashtable<String, String> getQueryParamters() {
        if (queryParamters == null) {
            queryParamters = new Hashtable<>();
        }
        return queryParamters;
    }

    public Hashtable<String, String> getPathSegments() {
        if (pathSegments == null) {
            pathSegments = new Hashtable<>();
        }
        return pathSegments;
    }
}
