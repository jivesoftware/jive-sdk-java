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
import com.jivesoftware.jivesdk.api.tiles.TableItem;
import com.jivesoftware.jivesdk.api.tiles.TileAction;
import com.jivesoftware.jivesdk.api.tiles.TileData;
import com.jivesoftware.jivesdk.api.tiles.TileStyle;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 */
public class SampleTableTile extends SampleTileRunner {
	public void run() {
		try {
			TileData<TableItem> data = new TileData<TableItem>(TileStyle.TABLE);
			data.setTitle("New Title");
			TableItem l1 = new TableItem("push 1 ", String.valueOf(new Date()));
			TileAction action1 = new TileAction("blah", "http://lmgtfy.com/?q=jive+purposeful+place+docs");

			l1.setAction(action1);
			data.getContents().add(l1);
			TableItem l2 = new TableItem("push 2 ", String.valueOf(new Date()));
			TileAction action2 = new TileAction();
			action2.getContext().put("q", "jive software");
			l2.setAction(action2);
			data.getContents().add(l2);
			data.setDescription("Sample list with Java");
			TileAction action = new TileAction();
			action.setUrl("http://lmgtfy.com/");
			action.setText("action");
			data.setAction(action);
			JiveSDKManager.getInstance().getJiveClient().sendPutTileUpdate(tileInstance, data);
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
