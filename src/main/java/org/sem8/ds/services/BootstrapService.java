package org.sem8.ds.services;

import org.sem8.ds.rest.resource.NeighbourResource;
import org.sem8.ds.rest.resource.RegisterResponseResource;
import org.sem8.ds.rest.resource.UnregisterResponseResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.sem8.ds.rest.resource.AbstractResponseResource.*;

/**
 * @author amila karunathilaka.
 */
public class BootstrapService {

    private List<NeighbourResource> nodeList;

    public void init() {
        nodeList = new ArrayList<NeighbourResource>();
    }

    private RegisterResponseResource setResponseNodeList(RegisterResponseResource responseResource) {
        if (nodeList.size() == 1) {
            responseResource.setNode_No(1);
            responseResource.addNode(nodeList.get(0).getIp(), nodeList.get(0).getPort());
        } else if (nodeList.size() == 2) {
            responseResource.setNode_No(2);
            responseResource.addNode(nodeList.get(0).getIp(), nodeList.get(0).getPort());
            responseResource.addNode(nodeList.get(1).getIp(), nodeList.get(1).getPort());
        } else {
            Random r = new Random();
            int Low = 0;
            int High = nodeList.size();
            int random_1 = r.nextInt(High - Low) + Low;
            int random_2 = r.nextInt(High - Low) + Low;
            while (random_1 == random_2) {
                random_2 = r.nextInt(High - Low) + Low;
            }
            echo(random_1 + " " + random_2);
            responseResource.setNode_No(2);
            responseResource.addNode(nodeList.get(random_1).getIp(), nodeList.get(random_1).getPort());
            responseResource.addNode(nodeList.get(random_2).getIp(), nodeList.get(random_2).getPort());
        }

        return responseResource;
    }

    public RegisterResponseResource register(NeighbourResource resource) {
        RegisterResponseResource registerResResource = new RegisterResponseResource();
        registerResResource.setResponseType(ResponseType.REGOK);

        if (nodeList.isEmpty()) {
            registerResResource.setNode_No(0);
            nodeList.add(resource);
        } else {
            if (nodeList.contains(resource)) {
                if (resource.getUsername().equals(nodeList.get(nodeList.indexOf(resource)).getUsername())) {
                    registerResResource.setNode_No(9998);
                } else {
                    registerResResource.setNode_No(9997);
                }
            } else {
                setResponseNodeList(registerResResource);
                nodeList.add(resource);
            }
        }
        return registerResResource;
    }

    public UnregisterResponseResource Unregister(NeighbourResource resource) {
        UnregisterResponseResource responseResource = new UnregisterResponseResource();
        responseResource.setResponseType(ResponseType.UNROK);
        responseResource.setErrorCode(9999);
        boolean isRemove = nodeList.remove(resource);
        if (isRemove)
            responseResource.setErrorCode(0);

        for (NeighbourResource resource1 :
                nodeList) {
            System.out.println(resource1.getIp() + " " + resource1.getPort() + " " + resource1.getUsername());
        }

        return responseResource;
    }

    //simple function to echo data to terminal
    public static void echo(String msg) {
        System.out.println(msg);
    }
}
