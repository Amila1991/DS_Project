package org.sem8.ds.services;

import org.sem8.ds.rest.resource.NodeResource;
import org.sem8.ds.rest.resource.RegisterResponseResource;
import org.sem8.ds.rest.resource.CommonResponseResource;
import org.sem8.ds.services.exception.ServiceException;
import org.sem8.ds.util.constant.NodeConstant.BootstrapRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.*;

import static org.sem8.ds.rest.resource.AbstractResponseResource.*;

/**
 * @author amila karunathilaka.
 */
public class BootstrapService {

    private String hostname;
    private int port;

    private DatagramSocket socket;

    @Autowired
    private NodeService nodeService;

    public void init() throws SocketException {
        socket = new DatagramSocket();
    }

    private void addNeighbourNode(int noOfNode, String[] nodeList) {
        for (int i = 1; i <= noOfNode; i++) {
            nodeService.addNeighbourNode(nodeList[2 * i], Integer.parseInt(nodeList[2 * i + 1]));
        }
    }

    private void sendMessage(String dataPacket) throws ServiceException{
        byte[] buffer = (String.format("%04d", dataPacket.length() + 5) + " " + dataPacket).getBytes();
        try {
            InetAddress ipAddress = InetAddress.getByName(hostname);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, ipAddress, port);
            socket.send(packet);
        } catch (UnknownHostException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (IOException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    /**
     *  send message to bootstrap server for node registation in bootstrap.
     * @param resource @NodeResource
     * @return @RegisterResponseResource
     * @throws ServiceException
     */
    public RegisterResponseResource register(NodeResource resource, String username)  throws ServiceException{
        RegisterResponseResource registerResResource = new RegisterResponseResource();
        registerResResource.setResponseType(ResponseType.REGOK);
        String dataPacket = BootstrapRequest.NODE_REG + " " + resource.getIp() + " " + resource.getPort() + " "
                + username + " ";

        sendMessage(dataPacket);

        byte[] buffer = new byte[65536];
        DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
        try {
            socket.receive(incoming);
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }

        int length = Integer.parseInt(new String(incoming.getData(), 0, 4));
        System.out.println(length);
        String[] responseSplit = new String(incoming.getData(), 5, length - 5).split(" ");
        if (!responseSplit[0].equals("REGOK")) {
            if (responseSplit[0].equals("ERROR"))
                throw new ServiceException("Message length error");
            throw new ServiceException("Message format isn't match - {}", responseSplit[0]);
        }

        nodeService.setIp(resource.getIp());
        nodeService.setPort(resource.getPort());
        nodeService.setUsername(username);

        int responseCode = Integer.parseInt(responseSplit[1]);

        switch (responseCode) {
            case 0:
                registerResResource.setNode_No(0);
                break;
            case 1:
                registerResResource.setNode_No(1);
                addNeighbourNode(1, responseSplit);
                break;
            case 2:
                registerResResource.setNode_No(2);
                addNeighbourNode(2, responseSplit);
                break;
            case 9999:
                registerResResource.setNode_No(9999);
                registerResResource.setError("Error 9999 – failed, there is some error in the command");
                break;
            case 9998:
                registerResResource.setNode_No(9998);
                registerResResource.setError("failed, already registered to you, unregister first");
                break;
            case 9997:
                registerResResource.setNode_No(9997);
                registerResResource.setError("failed, registered to another user, try a different IP and port");
                break;
            case 9996:
                registerResResource.setNode_No(9996);
                registerResResource.setError("Error 9996 – failed, can’t register. BS full");
        }

        registerResResource.setNodesList(nodeService.getNeighbourList());

        return registerResResource;
    }

    /**
     *  send message to bootstrap server for unregister node
     * @param resource @NodeResource
     * @return @CommonResponseResource
     * @throws ServiceException
     */
    public CommonResponseResource Unregister(NodeResource resource, String username) throws ServiceException {
        CommonResponseResource responseResource = new CommonResponseResource();
        responseResource.setResponseType(ResponseType.UNROK);
        responseResource.setIp(resource.getIp());
        responseResource.setPort(resource.getPort());
        responseResource.setErrorCode(9999);
        String dataPacket = BootstrapRequest.NODE_UNREG + " " + resource.getIp() + " " + resource.getPort() + " "
                + username + " ";

        sendMessage(dataPacket);

        byte[] buffer = new byte[65536];
        DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
        try {
            socket.receive(incoming);
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }

        int length = Integer.parseInt(new String(incoming.getData(), 0, 4));
        System.out.println(length);
        String[] responseSplit = new String(incoming.getData(), 5, length - 5).split(" ");
        if (!responseSplit[0].equals("UNROK")) {
            if (responseSplit[0].equals("ERROR"))
                throw new ServiceException("Message length error");
            throw new ServiceException("Message format isn't match - {}", responseSplit[0]);
        }

        int responseCode = Integer.parseInt(responseSplit[1]);

        if (responseCode == 0) {
            responseResource.setErrorCode(0);
        }

        return responseResource;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public NodeService getNodeService() {
        return nodeService;
    }

    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }
}
