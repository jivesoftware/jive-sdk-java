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

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 */
public class MyInstanceRegistrationHandlerImpl implements InstanceRegistrationHandler {

	@Inject
	Database database;

	@Inject
	private ScheduledExecutorService scheduledThreadPoolExecutor;

	public MyInstanceRegistrationHandlerImpl() {
		System.out.println("MyInstanceRegistrationHandlerImpl");
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
	public void register(@Nonnull RegistrationRequest request) {
		TileInstance tileInstance = new TileInstance(request);
		Credentials credentials = JiveSDKManager.getInstance().getJiveCredentialsAcquirer().acquireCredentials(request);
		tileInstance.setCredentials(credentials);
		database.tileInstances().put(request.getGuid(), tileInstance);
		try {
			database.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Runnable tileRunnable = Main.createTileRunnable(tileInstance);
		if(tileRunnable != null) {
			scheduledThreadPoolExecutor.scheduleWithFixedDelay(tileRunnable, 1, 30,
				TimeUnit.SECONDS);
		}


/* activity sample
				"{\"activity\": {\n" +
				"            \"action\": {\n" +
				"                \"name\": \"posted\",\n" +
				"                \"description\": description\n" +
				"            },\n" +
				"            \"actor\": {\n" +
				"                \"name\": \"Actor Name\",\n" +
				"                \"email\": \"actor@email.com\"\n" +
				"            },\n" +
				"            \"object\": {\n" +
				"                \"type\": \"website\",\n" +
				"                // Create a url based on the tile name and canvas view of the app.  This makes the link go into an app:\n" +
				"                \"url\": \"/apps/{{{TILE_NAME_BASE}}}_{{{GENERATED_UUID}}}/todoDetail/\"  + encodeURIComponent(JSON.stringify({\"id\": id})),\n" +
				"                \"image\": jive.context.config.clientUrl + \":\" +  jive.context.config.port + \"/checkbox_checked.png\",\n" +
				"                \"title\": description,\n" +
				"                \"description\": description\n" +
				"            },\n" +
				"            \"properties\": {\n" +
				"                // This allows the group context to be exposed to the app:\n" +
				"                \"relativeUrl\": \"true\",\n" +
				"                // This makes the title like to the url above.\n" +
				"                \"showGoToItemAsTitle\" :\"true\"\n" +
				"            },\n" +
				"            \"externalID\": '' + id + \"-\" + (new Date())" +
				"}");
*/
	}

	@Override
	public void update(Object object) {
		try {
			database.save();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
