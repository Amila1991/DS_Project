package org.sem8.ds.rest.controller;

import org.sem8.ds.rest.resource.ExceptionMessageResource;
import org.sem8.ds.services.exception.ServiceException;

import javax.ws.rs.core.Response;

/**
 * @author amila karunathilaka.
 */
public class AbstractController {

    protected Response handleServiceException(ServiceException e) {
        return Response.status(500).entity(new ExceptionMessageResource(e.getMessage())).build();
    }

}
