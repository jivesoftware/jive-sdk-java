package com.jivesoftware.jivesdk.example;


import com.jivesoftware.jivesdk.api.JiveSDKManager;
import com.jivesoftware.jivesdk.api.TileInstance;
import com.jivesoftware.jivesdk.example.db.Database;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 */
public class Main {

    /**
     * Main class.
     */
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8099/";

	private ResourceConfig rc;
	private static Logger log = LoggerFactory.getLogger(Main.class);
	private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
	private Database database;


	public static void main(String[] args) throws IOException {
		Main main = new Main();
    }

	private Main() throws IOException {
		final HttpServer server = startServer();
		init();

		System.out.println(String.format("Jersey app started with WADL available at "
				+ "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
		System.in.read();
		server.stop();
	}

	private void init() throws IOException {
		MyInstanceRegistrationHandlerImpl instanceRegistrationHandler = new MyInstanceRegistrationHandlerImpl();
		JiveSDKManager.getInstance().setInstanceRegistrationHandler(instanceRegistrationHandler);
		scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
		instanceRegistrationHandler.setScheduledThreadPoolExecutor(scheduledThreadPoolExecutor);
		database = Database.getInstance();

		Map<String, TileInstance> stringTileInstanceMap = database.tileInstances();
		for(TileInstance tileInstance : stringTileInstanceMap.values()) {
			instanceRegistrationHandler.startRunnable(tileInstance);
		}
	}

	/**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     *
     * @return Grizzly HTTP server.
     */
    public HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in com.example package

		rc = new ResourceConfig().packages("com.jivesoftware").register(JacksonFeature.class);

		ApplicationHandler handler = new ApplicationHandler(rc);

		// create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), handler, true);

        return httpServer;
    }
}
