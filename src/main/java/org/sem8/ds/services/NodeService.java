package org.sem8.ds.services;

import org.glassfish.jersey.client.ClientProperties;
import org.sem8.ds.client.remote.ResponseInterface;
import org.sem8.ds.rest.resource.*;
import org.sem8.ds.client.remote.ResponseInterface.UpdateType;
import org.sem8.ds.services.exception.ServiceException;
import org.sem8.ds.util.FileList;
import org.sem8.ds.util.constant.NodeConstant;
import org.sem8.ds.util.constant.NodeConstant.NodeMsgType;
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
    private List<String> fileList = null;
    private Map<String, NodeResource> searchMap;
    private RoutingTable routingTable;
    private FileTable fileTable;

    private Map<NodeMsgType, Integer> msgCount;

    private static NodeService nodeService;

    private ResponseInterface anInterface;

    public void init() throws SocketException {
        searchMap = new HashMap<>();
        routingTable = RoutingTable.getInstance();
        fileTable = FileTable.getInstance();
        msgCount = new HashMap<>();
        msgCount.put(NodeMsgType.JOIN, 0);
        msgCount.put(NodeMsgType.LEAVE, 0);
        msgCount.put(NodeMsgType.SEARCH, 0);
        msgCount.put(NodeMsgType.SEARCHRESPONSE, 0);

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
        client.property(ClientProperties.CONNECT_TIMEOUT, 1000);
        client.property(ClientProperties.READ_TIMEOUT, 1000);
        String host = NodeConstant.PROTOCOL + resource.getIp() + ":" + resource.getPort() + NodeConstant.REST_API;
        WebTarget target = client.target(host).path(NodeConstant.NODE_SERVICE + RestRequest.JOIN);

        NodeResource node = new NodeResource(getIp(), getPort());
        Response response = target.request(MediaType.APPLICATION_JSON_TYPE).post(
                Entity.entity(node, MediaType.APPLICATION_JSON_TYPE));

        return parseResponse(response, CommonResponseResource.class);
    }

    public void sendJoinRequestAll(List<NodeResource> resourceList) throws ServiceException {
        Client client = ClientBuilder.newClient();
        client.property(ClientProperties.CONNECT_TIMEOUT, 2000);
        client.property(ClientProperties.READ_TIMEOUT, 2000);
        String host;
        for (final NodeResource resource : resourceList) {
            host = NodeConstant.PROTOCOL + resource.getIp() + ":" + resource.getPort() + NodeConstant.REST_API;
            WebTarget target = client.target(host).path(NodeConstant.NODE_SERVICE + RestRequest.JOIN);

            NodeResource node = new NodeResource(getIp(), getPort());
            Future<Response> response = target.request(MediaType.APPLICATION_JSON_TYPE).async().post(
                    Entity.entity(node, MediaType.APPLICATION_JSON_TYPE), new InvocationCallback<Response>() {
                        public void completed(Response response) {
                            routingTable.addNeighBour(resource);
                            System.out.println(resource.getIp() + ":" + resource.getPort() + "ADD");
                            CommonResponseResource responseResource = new CommonResponseResource();
                            responseResource.setResponseType(ResponseType.JOINOK);
                            responseResource.setIp(resource.getIp());
                            responseResource.setPort(resource.getPort());
                            anInterface.executeCommonResponse(responseResource);
                        }

                        public void failed(Throwable throwable) {
                            System.err.println(throwable.getMessage());
                        }
                    });
        }
    }

    public CommonResponseResource sendLeaveRequest(NodeResource resource) throws ServiceException {
        Client client = ClientBuilder.newClient();
        client.property(ClientProperties.CONNECT_TIMEOUT, 1000);
        client.property(ClientProperties.READ_TIMEOUT, 1000);
        String host = NodeConstant.PROTOCOL + resource.getIp() + ":" + resource.getPort() + NodeConstant.REST_API;
        WebTarget target = client.target(host).path(NodeConstant.NODE_SERVICE + RestRequest.LEAVE);

        NodeResource node = new NodeResource(getIp(), getPort());
        Response response = target.request(MediaType.APPLICATION_JSON_TYPE).post(
                Entity.entity(node, MediaType.APPLICATION_JSON_TYPE));

        return parseResponse(response, CommonResponseResource.class);
    }

    public void sendLeaveRequestAll() throws ServiceException {
        Client client = ClientBuilder.newClient();
        client.property(ClientProperties.CONNECT_TIMEOUT, 2000);
        client.property(ClientProperties.READ_TIMEOUT, 2000);
        String host;
        for (final NodeResource resource : routingTable.getNodeList()) {
            host = NodeConstant.PROTOCOL + resource.getIp() + ":" + resource.getPort() + NodeConstant.REST_API;
            WebTarget target = client.target(host).path(NodeConstant.NODE_SERVICE + RestRequest.LEAVE);

            NodeResource node = new NodeResource(getIp(), getPort());
            Future<Response> response = target.request(MediaType.APPLICATION_JSON_TYPE).async().post(
                    Entity.entity(node, MediaType.APPLICATION_JSON_TYPE), new InvocationCallback<Response>() {
                        public void completed(Response response) {
                            routingTable.removeNeighbour(resource);
                            System.out.println(resource.getIp() + ":" + resource.getPort() + "REMOVE");
                            CommonResponseResource responseResource = new CommonResponseResource();
                            responseResource.setResponseType(ResponseType.LEAVEOK);
                            responseResource.setIp(resource.getIp());
                            responseResource.setPort(resource.getPort());
                            anInterface.executeCommonResponse(responseResource);
                        }

                        public void failed(Throwable throwable) {
                            System.err.println(throwable.getMessage());
                        }
                    });
        }
    }


    /**
     * send search request for neighbours
     *
     * @param resourceList
     * @param file
     * @param hop
     */
    public void sendSearchFileRequest(List<NodeResource> resourceList, String file, int hop) {
        Client client = ClientBuilder.newClient();
        client.property(ClientProperties.CONNECT_TIMEOUT, 1000);
        client.property(ClientProperties.READ_TIMEOUT, 1000);
        String path = NodeConstant.NODE_SERVICE + RestRequest.SEARCH + "/" + file + "/" + hop;
        String host;

        System.out.println("search request starting " + path);

        NodeResource senderNode = resourceList.remove(1);
        resourceList.add(1, new NodeResource(getIp(), getPort()));
        for (NodeResource resource : routingTable.getNodeList()) {
            System.out.println(resource.getIp() + " : " + resource.getPort());
            if(resource.equals(senderNode)){
                System.out.println(resource.getIp() + " : " + resource.getPort() +" search sender");
                continue;
            }
            host = NodeConstant.PROTOCOL + resource.getIp() + ":" + resource.getPort() + NodeConstant.REST_API;
            WebTarget target = client.target(host).path(path);
            Future<Response> response = target.request(MediaType.APPLICATION_JSON_TYPE).async().post(
                    Entity.entity(resourceList, MediaType.APPLICATION_JSON_TYPE), new InvocationCallback<Response>() {
                        public void completed(Response response) {
                            try {
                                System.out.println("completed Search request");
                                SearchResponseResource responseResource =
                                        parseResponse(response, SearchResponseResource.class);
                                Iterator<String> keyIterator = responseResource.getFileList().keySet().iterator();
                                while (keyIterator.hasNext()) {
                                    String file = keyIterator.next();
                                    for (NodeResource nodeResource :
                                            responseResource.getFileList().get(file)) {
                                        fileTable.addFile(file, nodeResource);
                                    }
                                }
                            } catch (ServiceException e) {
                                e.printStackTrace();
                            }

                        }

                        public void failed(Throwable throwable) {

                        }
                    });

        }//parseResponse(response, SearchResponseResource.class);
    }


    /**
     * serach file in this service & if didn't find send
     *
     * @param resourceList
     * @param file
     * @param hop
     * @return
     * @throws ServiceException
     */
    public SearchResponseResource searchFile(List<NodeResource> resourceList, String file, int hop) throws ServiceException {
        System.out.println("search " + hop);
        SearchResponseResource responseResource = new SearchResponseResource();
        if (resourceList != null) {
            System.out.println(file + " " + resourceList.get(0).toString());
            System.out.println("resourceList is not null");
            if (searchMap.get(file) == null || !searchMap.get(file).equals(resourceList.get(0))) {
                searchMap.put(file, resourceList.get(0));
                System.out.println("true");
                if (--hop != 0)
                    sendSearchFileRequest(resourceList, file, hop);

                Map<String, List<NodeResource>> result = searchFileServiceWithHopCount(file);
                if (result != null && !result.isEmpty()) {
                    System.out.println("result is not null");

                    Client client = ClientBuilder.newClient();
                    client.property(ClientProperties.CONNECT_TIMEOUT, 1000);
                    client.property(ClientProperties.READ_TIMEOUT, 1000);
                    String host = NodeConstant.PROTOCOL + resourceList.get(0).getIp() + ":" +
                            resourceList.get(0).getPort() + NodeConstant.REST_API;
                    System.out.println(host);
                    WebTarget target = client.target(host).path(NodeConstant.NODE_SERVICE +
                            RestRequest.SEARCH_RESPONSE + "/" + hop);

                    Response response = target.request(MediaType.APPLICATION_JSON_TYPE).post(
                            Entity.entity(result, MediaType.APPLICATION_JSON_TYPE));
                    responseResource.setFileList(result);
                }
            }
        } else {
            System.out.println("resourceList is null");
            resourceList = new ArrayList<>(2);
            resourceList.add(new NodeResource(getIp(), getPort()));
            resourceList.add(new NodeResource(getIp(), getPort()));
            sendSearchFileRequest(resourceList, file, hop);
        }
        return responseResource;
    }


    /**
     * ping neighbour nodes to check their availability
     *
     */
    public void pingNeighbourNodes() {
        Client client = ClientBuilder.newClient();
        client.property(ClientProperties.CONNECT_TIMEOUT, 1000);
        client.property(ClientProperties.READ_TIMEOUT, 1000);
        String host;
        for (final NodeResource resource : routingTable.getNodeList()) {
            host = NodeConstant.PROTOCOL + resource.getIp() + ":" + resource.getPort() + NodeConstant.REST_API;
            WebTarget target = client.target(host).path(NodeConstant.NODE_SERVICE + RestRequest.PING);

            Future<Response> response = target.request(MediaType.APPLICATION_JSON_TYPE).async().get(
            new InvocationCallback<Response>() {
                        public void completed(Response response) {
                            //System.out.println("success");
                        }

                        public void failed(Throwable throwable) {
                            System.out.println("fail");
                            System.err.println(throwable.getMessage());
                            routingTable.removeNeighbour(resource);
                            anInterface.updateRoutingTable(UpdateType.LEAVE, resource);
                        }
                    });
        }
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
        NodeResource node = new NodeResource();
        node.setIp(this.ip);
        node.setPort(this.port);
        List<NodeResource> tempNodeList = new ArrayList<>();
        tempNodeList.add(node);

        Map<String, List<NodeResource>> tempMap;
        tempMap = fileTable.searchFile(fileName);

        List<String> tempList = fileTable.searchMyFileList(fileName);
        if (tempList != null) {
            tempMap = new HashMap<>();
            for (int i = 0; i < tempList.size(); i++) {
                tempMap.put(tempList.get(i), tempNodeList);
            }
            return tempMap;
        } else if (tempList == null && tempMap != null) {
            return tempMap;
        } else {
            return null;
        }
    }

    /**
     * TODO : update routing table
     *
     * @param resource
     * @return
     */
    public CommonResponseResource receiveJoinRequest(NodeResource resource) {
        System.out.println("web Service Join");
        CommonResponseResource responseResource = new CommonResponseResource();
        responseResource.setResponseType(ResponseType.JOINOK);
        responseResource.setIp(resource.getIp());
        responseResource.setPort(resource.getPort());
        responseResource.setErrorCode(0); // todo routingTable add error 9999
        routingTable.addNeighBour(resource);
        anInterface.updateRoutingTable(UpdateType.JOIN, resource);
        return responseResource;
    }

    public CommonResponseResource receiveLeaveRequest(NodeResource resource) {
        CommonResponseResource responseResource = new CommonResponseResource();
        responseResource.setResponseType(ResponseType.LEAVEOK);
        responseResource.setIp(resource.getIp());
        responseResource.setPort(resource.getPort());
        responseResource.setErrorCode(0); // todo routingTable add error 9999
        routingTable.removeNeighbour(resource);
        anInterface.updateRoutingTable(UpdateType.LEAVE, resource);
        return responseResource;
    }


    public void receiveSearchResponse(Map<String, List<NodeResource>> resultListMap, int currentHop) {
        System.out.println("search result");
        Iterator<String> keyIterator= resultListMap.keySet().iterator();
        while (keyIterator.hasNext()) {
            String tempFile = keyIterator.next();
            System.out.println(tempFile);
            if (!fileTable.checkContainFile(tempFile)){
                fileTable.initMyList(tempFile);
            }
        }
        anInterface.searchFileResult(resultListMap, currentHop);
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

    public void startPing() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    NodeService.this.pingNeighbourNodes();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
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

    public RoutingTable getRoutingTable() {
        return routingTable;
    }

    public void setRoutingTable(RoutingTable routingTable) {
        this.routingTable = routingTable;
    }

    public ResponseInterface getAnInterface() {
        return anInterface;
    }

    public FileTable getFileTable() {
        return fileTable;
    }

    public void setFileTable(FileTable fileTable) {
        this.fileTable = fileTable;
    }

    public static NodeService getNodeService() {
        return nodeService;
    }

    public static void setNodeService(NodeService nodeService) {
            NodeService.nodeService = nodeService;
    }

    public void setMyFileList(List<String> fileList) {
        for (String file: fileList) {
            fileTable.initMyList(file);
        }
    }

    public void setAnInterface(ResponseInterface anInterface) {
        this.anInterface = anInterface;
    }

    public void increaseMsgCount(NodeMsgType msgType) {
        msgCount.put(msgType, msgCount.get(msgType)+1);
        anInterface.setTotalMsgCount(totalMsgCount());
    }

    public int totalMsgCount() {
        int tot = msgCount.get(NodeMsgType.JOIN);
        tot+= msgCount.get(NodeMsgType.LEAVE);
        tot+= msgCount.get(NodeMsgType.SEARCH);
        tot+= msgCount.get(NodeMsgType.SEARCHRESPONSE);
        return tot;
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
