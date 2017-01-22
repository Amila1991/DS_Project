package org.sem8.ds.rest.controller;

import org.sem8.ds.rest.resource.NodeResource;
import org.sem8.ds.services.NodeService;
import org.sem8.ds.services.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author amila karunathilaka
 */
@Component
@Path("/node")
public class NodeController extends AbstractController {

    @Autowired
    NodeService nodeService;

    @POST
    @Path("/sendJoin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response Register(NodeResource resource) {
        try {
            return Response.status(200).entity(nodeService.sendJoin(resource)).build();
        } catch (ServiceException e) {
            return handleServiceException(e);
        }
    }
}
