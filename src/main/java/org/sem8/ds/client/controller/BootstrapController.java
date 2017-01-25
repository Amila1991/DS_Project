package org.sem8.ds.client.controller;

import org.sem8.ds.client.resource.RegisterResponseResource;
import org.sem8.ds.rest.resource.CommonResponseResource;
import org.sem8.ds.rest.resource.NodeResource;
import org.sem8.ds.services.BootstrapService;
import org.sem8.ds.services.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author amila karunathilaka
 */
public class BootstrapController extends AbstractClientController {

    @Autowired
    private BootstrapService bootstrapService;

    public RegisterResponseResource register(NodeResource resource, String username) throws ServiceException {
        System.out.println(resource.getIp() + " " + bootstrapService);
        return bootstrapService.register(resource, username);
    }

    public CommonResponseResource unregister(NodeResource resource, String username) throws ServiceException {
        System.out.println(resource.getIp() + " " + bootstrapService);
        return bootstrapService.Unregister(resource, username);
    }
}
