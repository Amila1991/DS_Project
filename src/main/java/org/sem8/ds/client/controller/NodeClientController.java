package org.sem8.ds.client.controller;

import org.sem8.ds.rest.resource.NodeResource;
import org.sem8.ds.services.NodeService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author amila karunathilaka.
 */
public class NodeClientController extends AbstractClientController {

    @Autowired
    NodeService nodeService;

    public void searchFile(String file, int hop) {

    }
}
