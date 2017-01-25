package org.sem8.ds.util.constant;

/**
 * @author amila karunathilaka.
 */
public class NodeConstant {

    public static final String PROTOCOL = "http://";
    public static final String REST_API = "/rest";

    public static final String NODE_SERVICE = "/node";

    public static final class RestRequest {
        public static final String JOIN = "/join";
        public static final String LEAVE = "/leave";
    }

    public static final class BootstrapRequest {
        public static final String NODE_REG = "REG";
        public static final String NODE_UNREG = "UNREG";
    }
}
