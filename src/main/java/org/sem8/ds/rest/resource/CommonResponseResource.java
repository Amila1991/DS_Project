package org.sem8.ds.rest.resource;

/**
 * @author amila karunathilaka.
 */
public class CommonResponseResource extends AbstractResponseResource {
    private int errorCode;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}