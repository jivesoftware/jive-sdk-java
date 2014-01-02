/*
 * Copyright (c) 2014. Jive Software
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

package com.jivesoftware.jivesdk.api;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
*/
@JsonIgnoreProperties(ignoreUnknown = true)
public class TileUnregisterRequest {
	private String name;
	private String jiveUrl;
	private String url;
	private String tenantID;
	private String id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJiveUrl() {
		return jiveUrl;
	}

	public void setJiveUrl(String jiveUrl) {
		this.jiveUrl = jiveUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getGlobalTileInstanceID() {
		return tenantID + "_" + id;
	}


	public String getTenantID() {
		return tenantID;
	}

	public void setTenantID(String tenantID) {
		this.tenantID = tenantID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
