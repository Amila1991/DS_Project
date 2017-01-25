package org.sem8.ds.client.controller;

import org.sem8.ds.rest.resource.CommonResponseResource;
import org.sem8.ds.rest.resource.GeneratedFileResponseResource;
import org.sem8.ds.rest.resource.NodeResource;
import org.sem8.ds.services.NodeService;
import org.sem8.ds.services.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author amila karunathilaka.
 */
public class NodeClientController extends AbstractClientController {

    @Autowired
    NodeService nodeService;

    public GeneratedFileResponseResource generateFiles(int noofFiles) throws ServiceException {
        return nodeService.generateFileList(noofFiles);
    }

    public CommonResponseResource sendJoinRequest(NodeResource resource) throws ServiceException {
        return nodeService.sendJoinRequest(resource);
    }

    public void sendJoinRequestAll() throws ServiceException {
        nodeService.sendJoinRequestAll();
    }

    public CommonResponseResource sendLeaveRequest(NodeResource resource) throws ServiceException {
        return nodeService.sendLeaveRequest(resource);
    }

    public void sendLeaveRequestAll() throws ServiceException {
        nodeService.sendLeaveRequestAll();
    }
}
