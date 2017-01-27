package org.sem8.ds.rest.resource;

import java.util.List;
import java.util.Map;

/**
 * @author amila karunathilaka.
 */
public class SearchResponseResource extends AbstractResponseResource {
    private String ip;
    private int port;

    private Map<String, List<NodeResource>> fileList;
    private int hop;

    public SearchResponseResource() {
        setResponseType(ResponseType.SEROK);
    }

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

    public Map<String, List<NodeResource>> getFileList() {
        return fileList;
    }

    public void setFileList(Map<String, List<NodeResource>> fileList) {
        this.fileList = fileList;
    }

    public int getHop() {
        return hop;
    }

    public void setHop(int hop) {
        this.hop = hop;
    }
}
