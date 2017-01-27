package org.sem8.ds.rest.controller;

import org.sem8.ds.rest.resource.ExceptionMessageResource;
import org.sem8.ds.rest.resource.NodeResource;
import org.sem8.ds.services.NodeService;
import org.sem8.ds.services.exception.ServiceException;
import org.sem8.ds.util.constant.NodeConstant.RestRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

/**
 * @author amila karunathilaka
 */
@Component
@Path("/node")
public class NodeController {

    @Autowired
    NodeService nodeService;

    @POST
    @Path(RestRequest.JOIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response receiveJoinRequest(NodeResource resource) {
        System.out.println(resource.getPort());
        return Response.status(200).entity(nodeService.receiveJoinRequest(resource)).build();
    }

    @POST
    @Path(RestRequest.LEAVE)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response receiveLeaveRequest(NodeResource resource) {
        System.out.println(resource.getPort());
        return Response.status(200).entity(nodeService.receiveLeaveRequest(resource)).build();
    }

    @POST
    @Path(RestRequest.SEARCH + "/{file}/{hop}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchFileRequest(List<NodeResource> resource, @PathParam("file") String file, @PathParam("hop") int hop) {
        System.out.println("Search request");
        try {
            return Response.status(200).entity(nodeService.searchFile(resource, file, hop)).build();
        } catch (ServiceException e) {
            return handleServiceException(e);
        }
    }

    @GET
    @Path(RestRequest.PING)
    public Response pingNode() {
        return Response.status(200).allow("ping").build();
    }

    @GET
    @Path("check")
    public Response pingCheck() {
        nodeService.setNodeService(nodeService);
        nodeService.startPing();
        return Response.status(200).allow("ping").build();
    }

    @POST
    @Path(RestRequest.SEARCH_RESPONSE)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response searchFileResponse(Map<String, List<NodeResource>> result){
        System.out.println("Search response");
        nodeService.receiveSearchResponse(result);
        return Response.status(200).build();
    }

    private Response handleServiceException(ServiceException e) {
        return Response.status(500).entity(new ExceptionMessageResource(e.getMessage())).build();
    }
}
