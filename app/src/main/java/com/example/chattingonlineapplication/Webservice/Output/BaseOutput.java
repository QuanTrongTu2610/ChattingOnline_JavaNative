package com.example.chattingonlineapplication.Webservice.Output;

public class BaseOutput<T> {
    private int statusCode;
    private String statusMessage;
    private T data;

    /** Status code for a successful request. */
    public static final int STATUS_CODE_OK = 200;

    /** Status code for a new resource has successfully created. */
    public static final int STATUS_CODE_CREATED = 201;

    /** Status code for a successful request with no content information. */
    public static final int STATUS_CODE_NO_CONTENT = 204;

    /** Status code for a resource corresponding to any one of a set of representations. */
    public static final int STATUS_CODE_MULTIPLE_CHOICES = 300;

    /** Status code for a resource that has permanently moved to a new URI. */
    public static final int STATUS_CODE_MOVED_PERMANENTLY = 301;

    /** Status code for a resource that has temporarily moved to a new URI. */
    public static final int STATUS_CODE_FOUND = 302;

    /** Status code for a resource that has moved to a new URI and should be retrieved using GET. */
    public static final int STATUS_CODE_SEE_OTHER = 303;

    /** Status code for a resource that access is allowed but the document has not been modified. */
    public static final int STATUS_CODE_NOT_MODIFIED = 304;

    /** Status code for a resource that has temporarily moved to a new URI. */
    public static final int STATUS_CODE_TEMPORARY_REDIRECT = 307;

    /** Status code for a request that requires user authentication. */
    public static final int STATUS_CODE_UNAUTHORIZED = 401;

    /** Status code for a server that understood the request, but is refusing to fulfill it. */
    public static final int STATUS_CODE_FORBIDDEN = 403;

    /** Status code for a server that has not found anything matching the Request-URI. */
    public static final int STATUS_CODE_NOT_FOUND = 404;

    /** Status code for an internal server error. */
    public static final int STATUS_CODE_SERVER_ERROR = 500;

    /** Status code for a bad gateway. */
    public static final int STATUS_CODE_BAD_GATEWAY = 502;

    /** Status code for a service that is unavailable on the server. */
    public static final int STATUS_CODE_SERVICE_UNAVAILABLE = 503;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
