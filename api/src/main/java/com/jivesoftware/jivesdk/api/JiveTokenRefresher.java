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

package com.jivesoftware.jivesdk.api;

import com.jivesoftware.externalclient.JiveOAuthResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 24/7/13
 * Time: 5:31 PM
 */
public interface JiveTokenRefresher {
    @Nullable
	JiveOAuthResponse refreshToken(@Nonnull TileInstance item) throws InvalidRequestException;
}
