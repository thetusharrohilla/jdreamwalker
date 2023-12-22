package main.java.com.jdreamwalker.Authentication;

public abstract class ResponseBuilder {


    protected final ResponseContextBuilder responseContextBuilder;

    public ResponseBuilder(final ResponseContextBuilder responseContextBuilder){
        this.responseContextBuilder = responseContextBuilder;
    }

    public abstract Boolean buildResponse(String responseObject);

}
