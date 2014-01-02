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

import com.jivesoftware.jivesdk.api.InvalidRequestException;
import com.jivesoftware.jivesdk.api.JiveSDKManager;
import com.jivesoftware.jivesdk.api.TileInstance;
import com.jivesoftware.jivesdk.api.TileUninstalledException;
import com.jivesoftware.jivesdk.api.tiles.*;
import com.jivesoftware.jivesdk.example.db.Database;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 */
public class SampleActivity extends SampleTileRunner {

	@Override
	public void run() {
		try {
			ActivityData data = new ActivityData(String.valueOf(System.currentTimeMillis()));
			String countString = tileInstance.getConfig().get("startSequence");
			int count = countString == null ? 1: Integer.parseInt(countString);
			ActivityAction action = new ActivityAction();
			action.setDescription("Activity Action Description " + new Date());
			data.setAction(action);
			data.getActor().put("name", "JavaSDK");
			data.getObject().put("type", "website");
			data.getObject().put("url", "https://developer.jivesoftware.com/");
			data.getObject().put("title", "Activity count=" + count);
			data.getObject().put("description", "Activity was created on " + new Date());
			data.getObject().put("image", "https://community.jivesoftware.com/servlet/JiveServlet/showImage/102-99994-1-1023036/j.png");

			JiveSDKManager.getInstance().getJiveClient().sendExternalStreamActivity(tileInstance, data);
			tileInstance.getConfig().put("startSequence", String.valueOf(count + 1));
			database.save();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (InvalidRequestException e) {
			e.printStackTrace();
		} catch (TileUninstalledException e) {
			unregister();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
