package com.jivesoftware.jivesdk.example;


import com.jivesoftware.jivesdk.api.InstanceRegistrationHandler;
import com.jivesoftware.jivesdk.api.JiveSDKManager;
import com.jivesoftware.jivesdk.api.TileInstance;
import com.jivesoftware.jivesdk.example.db.Database;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.Binder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
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

	@Inject
	private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

	@Inject
	private Database database;

	private Binder binder;
	private ResourceConfig rc;
	private static ServiceLocator serviceLocator;
	private static Logger log = LoggerFactory.getLogger(Main.class);


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
		serviceLocator.inject(this);

		Map<String, TileInstance> stringTileInstanceMap = database.tileInstances();
		for(TileInstance tileInstance : stringTileInstanceMap.values()) {
			Runnable tileRunnable = createTileRunnable(tileInstance);
			if(tileRunnable != null) {
				scheduledThreadPoolExecutor.scheduleWithFixedDelay(tileRunnable, 1, 30,
						TimeUnit.SECONDS);
			}
		}
	}

	public static Runnable createTileRunnable(TileInstance tileInstance) {
		Runnable ret = null;
		if(tileInstance.getTileDefName().equals("samplelist")) {
			SampleListTile sampleListTile = serviceLocator.createAndInitialize(SampleListTile.class);
			sampleListTile.setTileInstance(tileInstance);
			ret = sampleListTile;
		} else if (tileInstance.getTileDefName().equals("sampletable")) {
			SampleTableTile sampleTableTile =  serviceLocator.createAndInitialize(SampleTableTile.class);
			sampleTableTile.setTileInstance(tileInstance);
			ret = sampleTableTile;
		} else {
			log.error("Unknown tileInstance type '" + tileInstance.getTileDefName() + "'");
		}

		return ret;
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
		binder = createBinder();

		rc.register(binder);

		ApplicationHandler handler = new ApplicationHandler(rc);

		// create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), handler, true);


		serviceLocator = handler.getServiceLocator();

        return httpServer;
    }

	private Binder createBinder() {
		return new AbstractBinder() {
			@Override
			protected void configure() {
				// These are the two required injections:
				bind(MyInstanceRegistrationHandlerImpl.class).to(InstanceRegistrationHandler.class).in(Singleton.class);
				bind(JiveSDKManager.class).to(JiveSDKManager.class);
				// These injections are specific to the example code:
				bindFactory(new ThreadPoolFactory()).to(ScheduledThreadPoolExecutor.class);
				bindFactory(Database.getDatabaseFactory()).to(Database.class);
			}
		};
	}

	private class ThreadPoolFactory implements org.glassfish.hk2.api.Factory<ScheduledThreadPoolExecutor> {
		ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
		@Override
		public ScheduledThreadPoolExecutor provide() {
			return scheduledThreadPoolExecutor;
		}

		@Override
		public void dispose(ScheduledThreadPoolExecutor threadPoolExecutor) {

		}
	}
}
