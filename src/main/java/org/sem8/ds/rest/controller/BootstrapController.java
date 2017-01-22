package org.sem8.ds.rest.controller;

import org.sem8.ds.rest.resource.NodeResource;
import org.sem8.ds.services.BootstrapService;
import org.sem8.ds.services.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author amila karunathilaka
 */

@Component
@Path("/bootstrap")
public class BootstrapController extends AbstractController {

    @Autowired
    private BootstrapService bootstrapService;

    @Context
    HttpServletRequest request;

    @GET
    @Path("/register/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(@PathParam("username") String username) {
        NodeResource resource = new NodeResource(request.getServerName(), request.getServerPort());
        System.out.println(resource.getIp() + " " + bootstrapService);
        try {
            return Response.status(200).entity(bootstrapService.register(resource, username)).build();
        } catch (ServiceException e) {
            return handleServiceException(e);
        }
    }

    @GET
    @Path("/unregister/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response unregister(@PathParam("username") String username) {
        NodeResource resource = new NodeResource(request.getServerName(), request.getServerPort());
        System.out.println(resource.getIp() + " " + bootstrapService);
        try {
            return Response.status(200).entity(bootstrapService.Unregister(resource, username)).build();
        } catch (ServiceException e) {
            return handleServiceException(e);
        }
    }
}
