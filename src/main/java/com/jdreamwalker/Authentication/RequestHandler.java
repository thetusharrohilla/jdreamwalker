package main.java.com.jdreamwalker.Authentication;

public abstract class RequestHandler {

    protected final RequestBuilder requestBuilder;

    protected final ResponseBuilder responseBuilder;


    public RequestHandler(RequestBuilder requestBuilder , ResponseBuilder responseBuilder){

        this.requestBuilder = requestBuilder;
        this.responseBuilder =  responseBuilder;
    }


    public abstract Boolean authorize(UserRequestDto userRequestDto);


}