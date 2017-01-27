package org.sem8.ds.rest.resource;

import org.sem8.ds.client.resource.RegisterResponseResource;

import java.util.List;
import java.util.Map;

/**
 * @author amila karunathilaka.
 */
public interface ResponseInterface {

    void executeCommonResponse(CommonResponseResource responseResource);

    void updateRoutingTable(UpdateType updateType, NodeResource resource);

    void searchFileResult(Map<String, List<NodeResource>> resultMap);



    enum UpdateType{
        JOIN {
            @Override
            public String toString() {
                return "Join";
            }
        },
        LEAVE {
            @Override
            public String toString() {
                return "Leave";
            }
        }
    }

}
