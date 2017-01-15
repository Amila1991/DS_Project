package org.sem8.ds.rest.resource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author amila karunathilaka.
 */
public class RegisterResponseResource extends AbstractResponseResource {
    private int node_No;
    private List<Node> nodesList;

    public RegisterResponseResource() {
        this.nodesList = new ArrayList<Node>();
    }

    public class Node{
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
    }

    public int getNode_No() {
        return node_No;
    }

    public void setNode_No(int node_No) {
        this.node_No = node_No;
    }

    public List<Node> getNodesList() {
        return nodesList;
    }

    public void addNode(String ip, int port) {
        nodesList.add(new Node(ip, port));

    }
}
