package org.sem8.ds.rest.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author amila karunathilaka.
 */
public class SearchResponseResource extends AbstractResponseResource {
    private String ip;
    private int port;

    private List<String> fileList;
    private int hop;

    public SearchResponseResource() {
        setResponseType(ResponseType.SEROK);
        fileList = new ArrayList<String>();
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

    public List<String> getFileList() {
        return fileList;
    }

    public void setFileList(List<String> fileList) {
        this.fileList = fileList;
    }

    public void addFile(String file) {
        this.fileList.add(file);
    }

    public void addFile(Collection<String> file) {
        this.fileList.addAll(file);
    }

    public int getHop() {
        return hop;
    }

    public void setHop(int hop) {
        this.hop = hop;
    }
}
