package main.java.com.jdreamwalker.Authentication;

public class HttpResponseBuilder extends ResponseBuilder {

    public HttpResponseBuilder(final ResponseContextBuilder responseContextBuilder){
        super(responseContextBuilder);
    }
    @Override
    public Boolean buildResponse(String responseObject) {
        return responseContextBuilder.parseAndValidateResponse(responseObject);

    }
}
