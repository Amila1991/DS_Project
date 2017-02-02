package org.sem8.ds.client.controller;

import org.sem8.ds.client.remote.ResponseInterface;
import org.sem8.ds.client.resource.SearchStatResource;
import org.sem8.ds.services.NodeService;
import org.sem8.ds.services.exception.ServiceException;

import java.util.List;

/**
 * @author amila karunathilaka.
 */
public class NodeClientController extends AbstractClientController {

    NodeService nodeService = NodeService.getNodeService();


    public void setResponseInterface(ResponseInterface responseInterface) {
        nodeService.setAnInterface(responseInterface);
    }

    public void searchFile(String file, int hop) throws ServiceException {
        nodeService.searchSingleFile(null, file, hop);
    }

    public void pingNode() {
        nodeService.pingNeighbourNodes();
    }

    public void setFileList(List<String> fileList) {
        nodeService.setMyFileList(fileList);
    }

    public SearchStatResource getStatistic(String userName){
        return nodeService.getNodeStatService().generateStat(userName);
    }
}
