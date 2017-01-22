package org.sem8.ds.rest.resource;


/**
 * @author amila karunathilaka
 */

public class NodeResource {

    private String ip;
    private int port;

    public NodeResource() {
    }

    public NodeResource(String ip, int port) {
        this.ip = ip;
        this.port = port;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeResource that = (NodeResource) o;

        return port == that.port;

    }

    @Override
    public int hashCode() {
        return port;
    }
}
