package org.sem8.ds.rest.controller;

import org.sem8.ds.rest.resource.NeighbourResource;
import org.sem8.ds.services.BootstrapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author amila karunathilaka.
 */

@Component
@Path("/bootstrap")
public class BootstrapController {

    @Autowired
    private BootstrapService bootstrapService;

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response Register(NeighbourResource resource) {
        System.out.println(resource.getIp() + " " + bootstrapService);
        return Response.status(200).entity(bootstrapService.register(resource)).build();
    }

    @POST
    @Path("/unregister")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response Unregister(NeighbourResource resource) {
        System.out.println(resource.getIp() + " " + bootstrapService);
        return Response.status(200).entity(bootstrapService.Unregister(resource)).build();
    }
}
