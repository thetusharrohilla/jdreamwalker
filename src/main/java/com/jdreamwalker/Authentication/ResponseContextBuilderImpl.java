package main.java.com.jdreamwalker.Authentication;

import main.java.com.jdreamwalker.util.TransformUtil;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ResponseContextBuilderImpl implements ResponseContextBuilder {


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ResponseObject implements Serializable {

        @JsonProperty("user")
        private Object user;

        @JsonAlias("auth_valid")
        private Boolean authValid;

        @JsonAlias("authorized")
        private Boolean authorized;

        @JsonAlias("app_authorized")
        private Boolean appAuthorized;

        @JsonAlias("app_client")
        private Object appClient;

        public ResponseObject(){

        }

        public ResponseObject(Object user, Boolean authValid, Boolean authorized, Boolean appAuthorized,
                              Object appClient) {
            this.user = user;
            this.authValid = authValid;
            this.authorized = authorized;
            this.appAuthorized = appAuthorized;
            this.appClient = appClient;
        }

        public Object getUser() {
            return user;
        }

        public void setUser(Object user) {
            this.user = user;
        }

        public Boolean getAuthValid() {
            return authValid;
        }

        public void setAuthValid(Boolean authValid) {
            this.authValid = authValid;
        }

        public Boolean getAuthorized() {
            return authorized;
        }

        public void setAuthorized(Boolean authorized) {
            this.authorized = authorized;
        }

        public Boolean getAppAuthorized() {
            return appAuthorized;
        }

        public void setAppAuthorized(Boolean appAuthorized) {
            this.appAuthorized = appAuthorized;
        }

        public Object getAppClient() {
            return appClient;
        }

        public void setAppClient(Object appClient) {
            this.appClient = appClient;
        }
    }

    @Override
    public Boolean parseAndValidateResponse(final String response) {
        ResponseObject responseObject = (ResponseObject) TransformUtil.fromJson(response, ResponseObject.class);
        return responseObject.getAuthValid();
    }
}