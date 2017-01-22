package org.sem8.ds.rest.resource;

/**
 * @author amila karunathilaka.
 */
public class CommonResponseResource extends AbstractResponseResource {
    private String ip;
    private int port;
    private int errorCode;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}