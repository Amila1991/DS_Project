package org.sem8.ds.rest.resource;

/**
 * @author amila karunathilaka.
 */
public abstract class AbstractResponseResource {
    private ResponseType responseType;

    public ResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }

    public enum ResponseType {

        REGOK {
            @Override
            public String toString() {
                return "REGOK";
            }
        },
        UNROK {
            @Override
            public String toString() {
                return "UNROK";
            }
        }
    }
}
