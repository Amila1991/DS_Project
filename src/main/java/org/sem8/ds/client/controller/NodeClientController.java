package org.sem8.ds.client.controller;

import org.sem8.ds.client.remote.ResponseInterface;
import org.sem8.ds.client.resource.SearchStatResource;
import org.sem8.ds.rest.resource.FileTable;
import org.sem8.ds.rest.resource.NodeResource;
import org.sem8.ds.services.NodeService;
import org.sem8.ds.services.exception.ServiceException;

import java.util.Iterator;
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
        nodeService.setSearchQueryStartedTime(System.currentTimeMillis());
        nodeService.setSearchQueryMaxHop(hop);
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

    public void showFileTable() {
        FileTable fileTable = nodeService.getFileTable();
        System.out.println("===================================================================================\n\n");
        Iterator<String> iterator = fileTable.getFileMap().keySet().iterator();
        while ( iterator.hasNext()) {
            String key = iterator.next();
            System.out.println("key : " + key);
            for (NodeResource resource :
                    fileTable.getFileMap().get(key)) {
                System.out.println(resource.getIp() + ":" + resource.getPort() );
            }
        }

        System.out.println("\n My file list :");
        for (String s :
                fileTable.getMyFilelist()) {
            System.out.println(s);
        }
        System.out.println("\n======================================================================");

    }
}
