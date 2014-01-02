/*
 * Copyright (c) 2013. Jive Software
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 */

package com.jivesoftware.jivesdk.example;

import com.jivesoftware.jivesdk.api.*;
import com.jivesoftware.jivesdk.example.db.Database;
import com.jivesoftware.jivesdk.example.db.JiveInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 */
public class MyInstanceRegistrationHandlerImpl implements InstanceRegistrationHandler {

	Database database = Database.getInstance();
	private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
	private Logger log = LoggerFactory.getLogger(getClass());

	private Map<String, Future> runningTasks = new HashMap<String, Future>();

	public MyInstanceRegistrationHandlerImpl() {
		System.out.println("MyInstanceRegistrationHandlerImpl");
	}

	public Runnable createTileRunnable(TileInstance tileInstance) {
		SampleTileRunner ret;
		if(tileInstance.getTileDefName().equals("samplelist")) {
			SampleListTile sampleListTile = new SampleListTile();
			ret = sampleListTile;
		} else if (tileInstance.getTileDefName().equals("sampletable")) {
			SampleTableTile sampleTableTile =  new SampleTableTile();
			ret = sampleTableTile;
		} else if (tileInstance.getTileDefName().equals("sampleactivity")) {
			SampleActivity sampleTableTile =  new SampleActivity();
			ret = sampleTableTile;

		} else {
			log.error("Unknown tileInstance type '" + tileInstance.getTileDefName() + "'");
			return null;
		}
		ret.setTileInstance(tileInstance);
		ret.setInstanceRegistrationHandler(this);

		return ret;
	}

	@Nonnull
	@Override
	public RegisteredInstance register(@Nonnull InstanceRegistrationRequest request) {
		System.out.println("onRegistration:" + request);
		JiveInstance instance = new JiveInstance(request.getTenantId(), request.getJiveUrl(), request.getClientId(),
				request.getClientSecret());
		database.getJiveInstanceMap().put(request.getTenantId(), instance);
		try {
			database.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return instance;
	}

	@Override
	public RegisteredInstance getByTenantId(String tenantId) {
		return database.getJiveInstanceMap().get(tenantId);

	}

	@Nonnull
	@Override
	public void register(@Nonnull TileRegistrationRequest request) {
		TileInstance tileInstance = new TileInstance(request);
		Credentials credentials = JiveSDKManager.getInstance().getJiveCredentialsAcquirer().acquireCredentials(request);
		tileInstance.setCredentials(credentials);
		database.tileInstances().put(request.getGlobalTileInstanceID(), tileInstance);
		try {
			database.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
		startRunnable(tileInstance);
	}

	public void startRunnable(TileInstance tileInstance) {
		Runnable tileRunnable = createTileRunnable(tileInstance);
		if(tileRunnable != null) {
			ScheduledFuture<?> scheduledFuture = scheduledThreadPoolExecutor.scheduleWithFixedDelay(tileRunnable, 1, 30,
					TimeUnit.SECONDS);
			runningTasks.put(tileInstance.getGlobalTileInstanceId(), scheduledFuture);
		}
	}

	@Override
	public void unregister(TileUnregisterRequest unRegistrationRequest) {
		TileInstance tileInstance = database.tileInstances().get(unRegistrationRequest.getGlobalTileInstanceID());
		if(tileInstance != null) {
			unregister(tileInstance);
		}
	}

	public void unregister(TileInstance tileInstance) {
		database.tileInstances().remove(tileInstance.getGlobalTileInstanceId());
		Future future = runningTasks.get(tileInstance.getGlobalTileInstanceId());
		if(future != null) {
			future.cancel(false);
		}
		runningTasks.remove(tileInstance);
		try {
			database.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Object object) {
		try {
			database.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setScheduledThreadPoolExecutor(ScheduledThreadPoolExecutor scheduledThreadPoolExecutor) {
		this.scheduledThreadPoolExecutor = scheduledThreadPoolExecutor;
	}

	public ScheduledThreadPoolExecutor getScheduledThreadPoolExecutor() {
		return scheduledThreadPoolExecutor;
	}
}
