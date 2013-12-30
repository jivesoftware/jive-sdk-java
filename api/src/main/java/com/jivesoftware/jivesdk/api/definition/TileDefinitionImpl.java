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

package com.jivesoftware.jivesdk.api.definition;

import com.google.common.collect.Lists;
import org.apache.commons.codec.digest.DigestUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 19/5/13
 * Time: 2:05 PM
 */
public class TileDefinitionImpl implements TileDefinition {
    private static final Logger log = LoggerFactory.getLogger(TileDefinitionImpl.class);

    private long id;
    @Nonnull
    private String title;
    @Nonnull
    private String tileDefinitionGuid;
    @Nonnull
    private List<TileField> fieldsDefinitions;

    public TileDefinitionImpl(@Nonnull String instanceName, @Nonnull String tileDefName, @Nonnull String title, @Nonnull List<TileField> fieldsDefinitions) {
        this.id = NEW_TILE_DEFAULT_ID;
        this.title = title;
        this.tileDefinitionGuid = createGuid(instanceName, tileDefName, fieldsDefinitions);
        this.fieldsDefinitions = fieldsDefinitions;
    }

    public TileDefinitionImpl(long id, @Nonnull String tileDefinitionGuid, @Nonnull String title, @Nonnull String fieldsDefinitions) {
        this.id = id;
        this.tileDefinitionGuid = tileDefinitionGuid;
        this.title = title;
        this.fieldsDefinitions = translateFieldsString(fieldsDefinitions);
    }

    @Override
    public long getId() {
        return id;
    }

    @Nonnull
    @Override
    public String getTileDefinitionGuid() {
        return tileDefinitionGuid;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return title;
    }

    @Nonnull
    @Override
    public List<TileField> getAllFieldsDefinitions() {
        return fieldsDefinitions;
    }

    // The String will look like : "id,name,type|id,name,type|id,name,type"
    @Nonnull
    @Override
    public String getAllFieldsDefinitionsString() {
        StringBuilder fieldsString = new StringBuilder();
        Iterator<TileField> iterator = fieldsDefinitions.iterator();
        if (iterator.hasNext()) {
            String currTileDefString = iterator.next().toString();
            fieldsString.append(currTileDefString);
        }

        while (iterator.hasNext()) {
            String currTileDefString = TILE_DEFINITIONS_DELIMS + iterator.next().toString();
            fieldsString.append(currTileDefString);
        }

        return fieldsString.toString();
    }

    @Override
    public boolean isNew() {
        return this.id == NEW_TILE_DEFAULT_ID;
    }

    @Override
    public void setId(Long newId) {
        this.id = newId;
    }

    @Nonnull
    public List<TileField> translateFieldsString(String fieldsDefinitionsString) throws IllegalArgumentException {
        List<TileField> fieldsDefinitions = Lists.newArrayList();

        String[] tileDefinitions = fieldsDefinitionsString.split(TILE_DEFINITIONS_DELIMS);
        for (String tileDefinition : tileDefinitions) {
            String[] tileFields = tileDefinition.split(TileField.TILE_FIELDS_DELIMS);
            if (tileFields.length != 3) {
                log.error("bad string:" + fieldsDefinitions);
                throw new IllegalArgumentException("Invalid tilesField: tileField was not the correct size.  It was " + tileFields.length);
            }

            String id = tileFields[0];
            String name = tileFields[1];
            String type = tileFields[2];
            fieldsDefinitions.add(new TileFieldImpl(id, name, type));
        }

        log.debug("success");
        return fieldsDefinitions;
    }

    private String createGuid(@Nonnull String instanceName, @Nonnull String tileDefName, @Nonnull List<TileField> fields) {
        String fieldsString;
        try {
            fieldsString = new ObjectMapper().writeValueAsString(fields);
        } catch (IOException e) {
            fieldsString = UUID.randomUUID().toString();
            log.error("Failed converting fields to a string, generated UUID instead: " + fieldsString, e);
        }

        String part1 = DigestUtils.md5Hex(instanceName + tileDefName);
        String part2 = DigestUtils.md5Hex(fieldsString);
        return part1 + part2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TileDefinitionImpl that = (TileDefinitionImpl) o;

        return id == that.id && getAllFieldsDefinitionsString().equals(that.getAllFieldsDefinitionsString()) && tileDefinitionGuid.equals(that.tileDefinitionGuid) && title.equals(that.title);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (title.hashCode());
        result = 31 * result + (tileDefinitionGuid.hashCode());
        result = 31 * result + (fieldsDefinitions.hashCode());
        return result;
    }
}
