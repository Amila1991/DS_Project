package org.sem8.ds.rest.controller;

import org.sem8.ds.rest.resource.NodeResource;
import org.sem8.ds.services.NodeService;
import org.sem8.ds.services.exception.ServiceException;
import org.sem8.ds.util.constant.NodeConstant;
import org.sem8.ds.util.constant.NodeConstant.RestRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
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
    public Response sendJoinRequest(NodeResource resource) {
        System.out.println(resource.getPort());
        try {
            return Response.status(200).entity(nodeService.sendJoinRequest(resource)).build();
        } catch (ServiceException e) {
            return handleServiceException(e);
        }
    }

    @GET
    @Path("/sendJoinAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendJoinRequestAll() {
        try {
            return Response.status(200).entity(nodeService.sendJoinRequestAll()).build();
        } catch (ServiceException e) {
            return handleServiceException(e);
        }
    }

    @POST
    @Path(RestRequest.JOIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response receiveJoinRequest(NodeResource resource) {
        System.out.println(resource.getPort());
        return Response.status(200).entity(nodeService.receiveJoinRequest(resource)).build();
    }


    @POST
    @Path("/sendLeave")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendLeaveRequest(NodeResource resource) {
        System.out.println(resource.getPort());
        try {
            return Response.status(200).entity(nodeService.sendLeaveRequest(resource)).build();
        } catch (ServiceException e) {
            return handleServiceException(e);
        }
    }

    @GET
    @Path("/sendLeaveAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendLeaveRequestAll() {
        try {
            return Response.status(200).entity(nodeService.sendLeaveRequestAll()).build();
        } catch (ServiceException e) {
            return handleServiceException(e);
        }
    }

    @POST
    @Path(RestRequest.LEAVE)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response receiveLeaveRequest(NodeResource resource) {
        System.out.println(resource.getPort());
        return Response.status(200).entity(nodeService.receiveLeaveRequest(resource)).build();
    }
}
