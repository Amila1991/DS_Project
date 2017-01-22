package org.sem8.ds.rest.resource;

import org.sem8.ds.model.Node;
import java.util.ArrayList;
import java.util.List;

/**
 * @author amila karunathilaka.
 */
public class RegisterResponseResource extends AbstractResponseResource {
    private int node_No;
    private List<Node> nodesList;
    private String error;

    public RegisterResponseResource() {
        this.nodesList = new ArrayList<Node>();
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

    public void setNodesList(List<Node> nodesList) {
        this.nodesList = nodesList;
    }

    public void addNode(String ip, int port) {
        nodesList.add(new Node(ip, port));
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
