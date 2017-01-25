package org.sem8.ds.rest.resource;

import org.sem8.ds.client.resource.RegisterResponseResource;

/**
 * @author amila karunathilaka.
 */
public interface ResponseInterface {

    void executeRegisterResponse(RegisterResponseResource responseResource);

    void executeCommonResponse(CommonResponseResource responseResource);

    void UpdateRoutingTable(UpdateType updateType, NodeResource resource);


    enum UpdateType{
        ADD {
            @Override
            public String toString() {
                return "add";
            }
        },
        REMOVE {
            @Override
            public String toString() {
                return "remove";
            }
        }
    }

}
