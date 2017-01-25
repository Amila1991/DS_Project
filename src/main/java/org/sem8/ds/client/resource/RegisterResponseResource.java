package org.sem8.ds.client.resource;

import org.sem8.ds.rest.resource.AbstractResponseResource;
import org.sem8.ds.rest.resource.NodeResource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author amila karunathilaka.
 */
public class RegisterResponseResource extends AbstractResponseResource {
    private int node_No;
    private List<NodeResource> nodesList;
    private String error;

    public RegisterResponseResource() {
        setResponseType(ResponseType.REGOK);
        this.nodesList = new ArrayList<NodeResource>();
    }

    public int getNode_No() {
        return node_No;
    }

    public void setNode_No(int node_No) {
        this.node_No = node_No;
    }

    public List<NodeResource> getNodesList() {
        return nodesList;
    }

    public void setNodesList(List<NodeResource> nodesList) {
        this.nodesList = nodesList;
    }

    public void addNode(String ip, int port) {
        nodesList.add(new NodeResource(ip, port));
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
