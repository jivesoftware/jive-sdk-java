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

package com.jivesoftware.jivesdk.impl;

import javax.annotation.Nonnull;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 5/2/13
 * Time: 2:18 PM
 */
public class PropertyConfiguration {
    @Nonnull
    private final Properties properties;
    private static PropertyConfiguration instance;

    public PropertyConfiguration(@Nonnull Properties properties) {
        this.properties = properties;
    }

    public synchronized static PropertyConfiguration getInstance() {
        if(instance == null) {
            instance = new PropertyConfiguration(System.getProperties());
        }
        return instance;
    }

    public String getProperty(@Nonnull JiveSDKConfig.CommonConfiguration commonConfiguration) {
        return properties.getProperty(commonConfiguration.getKey(), commonConfiguration.getDefault());
    }


    public String getProperty(@Nonnull JiveSDKConfig.ServiceConfiguration serviceConfiguration) {
        return properties.getProperty(serviceConfiguration.getKey(), serviceConfiguration.getDefault());
    }

}