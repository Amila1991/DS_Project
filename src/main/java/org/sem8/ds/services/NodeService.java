package org.sem8.ds.services;

import org.glassfish.jersey.client.ClientProperties;
import org.sem8.ds.rest.resource.*;
import org.sem8.ds.services.exception.ServiceException;
import org.sem8.ds.util.FileList;
import org.sem8.ds.util.constant.NodeConstant;
import org.sem8.ds.util.constant.NodeConstant.RestRequest;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.Future;

import static org.sem8.ds.rest.resource.AbstractResponseResource.ResponseType;

/**
 * @author amila karunathilaka
 */
public class NodeService {
    private String ip;
    private int port;
    private String username;
    private List<NodeResource> neighbourList;
    private List<String> fileList = null;
    private Map<String, List<NodeResource>> searchMap;
    private RoutingTable routingTable;
    private FileTable fileTable;

    private ResponseInterface anInterface;

    public void init() throws SocketException {
        neighbourList = new ArrayList<NodeResource>();
        searchMap = new HashMap<String, List<NodeResource>>();
        routingTable = RoutingTable.getInstance();
        fileTable = FileTable.getInstance();

    }

    public GeneratedFileResponseResource generateFileList(int noofFiles) throws ServiceException {
        GeneratedFileResponseResource responseResource = new GeneratedFileResponseResource();
        if (noofFiles <= 5 && noofFiles >= 3) {
            fileList = FileList.generateFileList(noofFiles);
        } else {
            throw new ServiceException("Request file count invalid. file count : {}", String.valueOf(noofFiles));
        }
        responseResource.setNoofFiles(noofFiles);
        responseResource.setFileList(fileList);

        return responseResource;
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

    public void sendJoinRequestAll() throws ServiceException {
        Client client = ClientBuilder.newClient();
        String host;
        for (NodeResource resource : neighbourList) {
            host = NodeConstant.PROTOCOL + resource.getIp() + ":" + resource.getPort() + NodeConstant.REST_API;
            WebTarget target = client.target(host).path(NodeConstant.NODE_SERVICE + RestRequest.JOIN);

            NodeResource node = new NodeResource(getIp(), getPort());
            Future<Response> response = target.request(MediaType.APPLICATION_JSON_TYPE).async().post(
                    Entity.entity(node, MediaType.APPLICATION_JSON_TYPE), new InvocationCallback<Response>() {
                        public void completed(Response response) {
                            try {
                                CommonResponseResource responseResource = parseResponse(response,
                                        CommonResponseResource.class);
                                anInterface.executeCommonResponse(responseResource);
                            } catch (ServiceException e) {
                                e.printStackTrace();
                            }
                        }

                        public void failed(Throwable throwable) {

                        }
                    });
        }
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

    public void sendLeaveRequestAll() throws ServiceException {
        Client client = ClientBuilder.newClient();
        String host;
        for (NodeResource resource : neighbourList) {
            host = NodeConstant.PROTOCOL + resource.getIp() + ":" + resource.getPort() + NodeConstant.REST_API;
            WebTarget target = client.target(host).path(NodeConstant.NODE_SERVICE + RestRequest.LEAVE);

            NodeResource node = new NodeResource(getIp(), getPort());
            Future<Response> response = target.request(MediaType.APPLICATION_JSON_TYPE).async().post(
                    Entity.entity(node, MediaType.APPLICATION_JSON_TYPE), new InvocationCallback<Response>() {
                        public void completed(Response response) {
                            try {
                                CommonResponseResource responseResource = parseResponse(response,
                                        CommonResponseResource.class);
                                anInterface.executeCommonResponse(responseResource);
                            } catch (ServiceException e) {
                                e.printStackTrace();
                            }
                        }

                        public void failed(Throwable throwable) {

                        }
                    });
        }
    }

    private Future<Response> sendSearchFileRequest(NodeResource resource, String file, int hop,
                                                   final Set<String> fileSet, final int max_hop) throws
            ServiceException {
        Client client = ClientBuilder.newClient();
        String host = NodeConstant.PROTOCOL + resource.getIp() + ":" + resource.getPort() + NodeConstant.REST_API;
        String path = NodeConstant.NODE_SERVICE + RestRequest.SEARCH + "/" + file + "/" + hop;
        WebTarget target = client.target(host).path(path);

        NodeResource node = new NodeResource(getIp(), getPort());
        Future<Response> response = target.request(MediaType.APPLICATION_JSON_TYPE).async().post(
                Entity.entity(node, MediaType.APPLICATION_JSON_TYPE), new InvocationCallback<Response>() {
                    public void completed(Response response) {
                        try {
                            SearchResponseResource responseResource =
                                    parseResponse(response, SearchResponseResource.class);
                            fileSet.addAll(responseResource.getFileList());
                            if (max_hop < responseResource.getHop()) ;
                            ///max_hop = responseResource.getHop();
                        } catch (ServiceException e) {
                            e.printStackTrace();
                        }

                    }

                    public void failed(Throwable throwable) {

                    }
                });

        return response;//parseResponse(response, SearchResponseResource.class);
    }


    public SearchResponseResource searchFile(NodeResource resource, String file, int hop) throws ServiceException {
        //TODO search part
        if (searchMap.containsKey(file)) {
            searchMap.get(file).add(resource);
        } else {
            List addMapList = new ArrayList<NodeResource>();
            addMapList.add(resource);
            searchMap.put(file, addMapList);
        }
        SearchResponseResource responseResource = new SearchResponseResource();
        return responseResource;
    }

    public void pingNeighbourNodes(NodeResource resource) {
        Client client = ClientBuilder.newClient();
        client.property(ClientProperties.CONNECT_TIMEOUT, 1000);
        client.property(ClientProperties.READ_TIMEOUT, 1000);
        String host;
        // for (NodeResource resource : neighbourList) {
        host = NodeConstant.PROTOCOL + resource.getIp() + ":" + resource.getPort() + NodeConstant.REST_API;
        WebTarget target = client.target(host).path(NodeConstant.NODE_SERVICE + RestRequest.PING);

        NodeResource node = new NodeResource(getIp(), getPort());
        Future<Response> response = target.request(MediaType.APPLICATION_JSON_TYPE).async().post(
                Entity.entity(node, MediaType.APPLICATION_JSON_TYPE), new InvocationCallback<Response>() {
                    public void completed(Response response) {
                        System.out.println("success");
                    }

                    public void failed(Throwable throwable) {
                        System.out.println("fail");
                        System.err.println(throwable.getMessage());
                    }
                });
        // }
    }

    /**
     * hop count will be considered
     *
     * @param file
     * @return
     * @throws ServiceException
     */
    public Map<String, List<NodeResource>> searchFileService(String file) throws
            ServiceException {

        SearchResponseResource response = new SearchResponseResource();
        Map<String, List<NodeResource>> tempMap;
        tempMap = fileTable.searchFile(file);

        return tempMap;
    }

    /**
     * hop count will not be considered
     *
     * @param fileName
     * @return
     */
    public Map<String, List<NodeResource>> searchFileServiceWithHopCount(String fileName) {

        if (fileTable.getMyFilelist().contains(fileName)) {
            return null;
        } else {
            Map<String, List<NodeResource>> tempMap;
            tempMap = fileTable.searchFile(fileName);
            return tempMap;
        }
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

    private Set<String> searchFileinList(String file) {
        Set<String> toReturn = new HashSet<String>();
        String[] fileToken = file.split("-");
        L1:
        for (String tempFile : fileList) {
            if (!fileList.contains(tempFile)) {
                String[] tempToken = tempFile.split("_");
                for (int i = 0; i < tempToken.length; i++)
                    for (int j = 0; j < fileToken.length; j++) {
                        if (tempToken[i].equals(fileToken[j])) {
                            toReturn.add(tempFile);
                            continue L1;
                        }
                    }
            }
        }
        return toReturn;
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
