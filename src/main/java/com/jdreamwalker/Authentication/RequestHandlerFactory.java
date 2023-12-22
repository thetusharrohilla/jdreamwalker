package main.java.com.jdreamwalker.Authentication;

public class RequestHandlerFactory {

    public RequestHandler createProtocol(RequestEnum requestEnum , Object request) {
        switch (requestEnum) {
            case HTTP:
                return new HttpRequestHandler(new HttpRequestBuilder(new RequestContextBuilderAndDecoratorImpl(request)), new HttpResponseBuilder(new ResponseContextBuilderImpl() ));
            default:
                throw new IllegalArgumentException("Invalid request protocol type");
        }
    }
}