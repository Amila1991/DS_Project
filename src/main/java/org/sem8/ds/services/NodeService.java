package org.sem8.ds.services;

import org.sem8.ds.model.Node;
import org.sem8.ds.rest.resource.CommonResponseResource;
import org.sem8.ds.rest.resource.NodeResource;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import static org.sem8.ds.rest.resource.AbstractResponseResource.ResponseType;

/**
 * @author amila karunathilaka
 */
public class NodeService {
    private String ip;
    private int port;
    private String username;
    private List<Node> neighbourList;




    public void init() throws SocketException {
        neighbourList = new ArrayList<Node>();
    }

    /**
     * TODO : update routing table
     * @param resource
     * @return
     */
    public CommonResponseResource receiveJoinNode(NodeResource resource) {
        CommonResponseResource responseResource = new CommonResponseResource();
        responseResource.setResponseType(ResponseType.JOINOK);
        responseResource.setErrorCode(0); // todo routingTable add error 9999
        return responseResource;
    }

    /*public void sendJoinNode() {
        List<NodeService> nodeList = nodeManagementService.getNodeList();
        NodeResource resource = new NodeResource();
        resource.setIp(ip);
        resource.setPort(port);
        for (Node node: neighbourList) {
            if (nodeList.contains(new NodeService(node.getPort()))) {
                nodeList.get(nodeList.indexOf(new NodeService(node.getPort()))).receiveJoinNode(resource); //// TODO: CommonResponseResource catch & maintain log
            }
        }
    }*/


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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Node> getNeighbourList() {
        return neighbourList;
    }

    public void addNeighbourNode(String ip, int port) {
        neighbourList.add(new Node(ip, port));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeService that = (NodeService) o;

        return port == that.port;

    }

    @Override
    public int hashCode() {
        return port;
    }
}
