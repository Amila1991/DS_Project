package org.sem8.ds.util.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;
import org.sem8.ds.client.controller.BootstrapController;
import org.sem8.ds.client.resource.RegisterResponseResource;
import org.sem8.ds.rest.resource.NodeResource;
import org.sem8.ds.services.exception.ServiceException;
import org.sem8.ds.util.constant.NodeConstant;
import org.springframework.web.context.ContextLoaderListener;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author amila karunathilaka.
 */
public class JettyServer {

    private int serverPort;

    private Server server;

    public JettyServer(int serverPort) {
        setServerPort(serverPort);
        setup_web_xml(server);
    }

    public void startServer() throws Exception {
        if (!server.isStarted()) {
            server.start();
        }
    }

    public void stopServer() throws Exception {
        if (server.isRunning()) {
            server.stop();
        }
    }

    private void setServerPort(int serverPort) {
        if (server == null) {
            this.serverPort = serverPort;
            server = new Server(this.serverPort);
        }

    }

    public static void main(String[] args) {
        JettyServer jettyServer = new JettyServer(8081);
        try {
            jettyServer.startServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
        jettyServer.jettyPing();
        BootstrapController bootstrapController = new BootstrapController();
        NodeResource resource = new NodeResource("localhost", 8082);
        try {
            RegisterResponseResource response = bootstrapController.register(resource, "Amila");
            System.out.println(response.getResponseType() + " " + response.getNode_No() + " " + response.getError());

        } catch (ServiceException e) {
            e.printStackTrace();
        }

    }

    private void setup_web_xml (Server server) {
        ServletContextHandler context = new ServletContextHandler(server, "/*", ServletContextHandler.SESSIONS);
        ServletHolder servletHolder = new ServletHolder("jersey-servlet", new ServletContainer());
        servletHolder.setInitOrder(0);
        servletHolder.setInitParameter(ServerProperties.PROVIDER_PACKAGES, "org.sem8.ds.rest");
        servletHolder.setInitParameter("jersey.api.json.POJOMappingFeature", "true");
        context.addServlet(servletHolder, "/rest/*");
        context.addEventListener(new ContextLoaderListener());

        context.setInitParameter("contextConfigLocation", "classpath:spring.xml");
    }

    public void jettyPing() {
        Client client = ClientBuilder.newClient();
        client.property(ClientProperties.CONNECT_TIMEOUT, 1000);
        client.property(ClientProperties.READ_TIMEOUT, 1000);
        String host;
        host = NodeConstant.PROTOCOL + "localhost:" + serverPort + NodeConstant.REST_API;
        WebTarget target = client.target(host).path(NodeConstant.NODE_SERVICE + "/check");

        Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
    }

}
