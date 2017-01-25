package org.sem8.ds.services;

import org.sem8.ds.rest.resource.CommonResponseResource;
import org.sem8.ds.rest.resource.ExceptionMessageResource;
import org.sem8.ds.rest.resource.NodeResource;
import org.sem8.ds.rest.resource.RoutingTable;
import org.sem8.ds.services.exception.ServiceException;
import org.sem8.ds.util.constant.NodeConstant;
import org.sem8.ds.util.constant.NodeConstant.RestRequest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    private List<NodeResource> neighbourList;
    private RoutingTable routingTable;

    public void init() throws SocketException {
        neighbourList = new ArrayList<NodeResource>();
        routingTable = RoutingTable.getInstance();
    }

    /**
     * @param resource
     * @return
     * @throws ServiceException
     */
    public CommonResponseResource sendJoinRequest(NodeResource resource) throws ServiceException {
        Client client = ClientBuilder.newClient();
        String host = NodeConstant.PROTOCOL + resource.getIp() + ":" + resource.getPort() + NodeConstant.REST_API;
        WebTarget target = client.target(host).path(NodeConstant.NODE_SERVICE + RestRequest.JOIN);

        NodeResource node = new NodeResource(getIp(), getPort());
        Response response = target.request(MediaType.APPLICATION_JSON_TYPE).post(
                Entity.entity(node, MediaType.APPLICATION_JSON_TYPE));

        return parseResponse(response, CommonResponseResource.class);
    }

    public List<CommonResponseResource> sendJoinRequestAll() throws ServiceException {
        List<CommonResponseResource> responseResourceList = new ArrayList<CommonResponseResource>();
        for (NodeResource resource : neighbourList) {
            responseResourceList.add(sendJoinRequest(resource));
        }
        return responseResourceList;

    }

    public CommonResponseResource sendLeaveRequest(NodeResource resource) throws ServiceException {
        Client client = ClientBuilder.newClient();
        String host = NodeConstant.PROTOCOL + resource.getIp() + ":" + resource.getPort() + NodeConstant.REST_API;
        WebTarget target = client.target(host).path(NodeConstant.NODE_SERVICE + RestRequest.LEAVE);

        NodeResource node = new NodeResource(getIp(), getPort());
        Response response = target.request(MediaType.APPLICATION_JSON_TYPE).post(
                Entity.entity(node, MediaType.APPLICATION_JSON_TYPE));

        return parseResponse(response, CommonResponseResource.class);
    }

    public List<CommonResponseResource> sendLeaveRequestAll() throws ServiceException {
        List<CommonResponseResource> responseResourceList = new ArrayList<CommonResponseResource>();
        for (NodeResource resource :
                neighbourList) {
            responseResourceList.add(sendLeaveRequest(resource));
        }
        return responseResourceList;

    }

    /**
     * TODO : update routing table
     *
     * @param resource
     * @return
     */
    public CommonResponseResource receiveJoinRequest(NodeResource resource) {
        CommonResponseResource responseResource = new CommonResponseResource();
        responseResource.setResponseType(ResponseType.JOINOK);
        responseResource.setIp(resource.getIp());
        responseResource.setPort(resource.getPort());
        responseResource.setErrorCode(0); // todo routingTable add error 9999
        routingTable.addNeighBour(resource);
        return responseResource;
    }

    public CommonResponseResource receiveLeaveRequest(NodeResource resource) {
        CommonResponseResource responseResource = new CommonResponseResource();
        responseResource.setResponseType(ResponseType.LEAVEOK);
        responseResource.setIp(resource.getIp());
        responseResource.setPort(resource.getPort());
        responseResource.setErrorCode(0); // todo routingTable add error 9999
        routingTable.removeNeighbour(resource);
        return responseResource;
    }

    public void searchFileService(NodeResource node, String fileName, int hops) {

    }

    private <T> T parseResponse(Response response, Class<T> entityType) throws ServiceException {
        if (response == null) {
            throw new ServiceException("response object is null");
        }
        if (response.getStatus() >= 200 && response.getStatus() < 300) {
            return response.readEntity(entityType);
        } else {
            ExceptionMessageResource resource = response.readEntity(ExceptionMessageResource.class);
            if (resource != null) {
                throw new ServiceException(resource.getMessage());
            } else {
                throw new ServiceException("response failed.....");
            }
        }
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<NodeResource> getNeighbourList() {
        return neighbourList;
    }

    public void addNeighbourNode(String ip, int port) {
        neighbourList.add(new NodeResource(ip, port));
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
