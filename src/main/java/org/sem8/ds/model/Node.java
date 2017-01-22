package org.sem8.ds.model;

/**
 * @author amila karunathilaka.
 */
public class Node {
    private String ip;
    private int port;

    public Node(String ip, int port) {
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

        Node node = (Node) o;

        return port == node.port;

    }

    @Override
    public int hashCode() {
        return port;
    }
}
