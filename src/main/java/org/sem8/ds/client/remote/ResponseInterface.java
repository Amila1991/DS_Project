package org.sem8.ds.client.remote;

import org.sem8.ds.rest.resource.CommonResponseResource;
import org.sem8.ds.rest.resource.NodeResource;

import java.util.List;
import java.util.Map;

/**
 * @author amila karunathilaka.
 */
public interface ResponseInterface {

    void executeCommonResponse(CommonResponseResource responseResource);

    void updateRoutingTable(UpdateType updateType, NodeResource resource);

    void searchFileResult(Map<String, List<NodeResource>> resultMap, int currentHop, long latency);

    void setTotalMsgCount(int totalMsgCount);


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
