package com.jivesoftware.jivesdk.api.definition;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 19/5/13
 * Time: 1:59 PM
 */
public interface TileDefinition {
    public static final String TILE_DEFINITIONS_DELIMS = ":";
    public static final long NEW_TILE_DEFAULT_ID = -1;

    long getId();

    @Nonnull
    String getTitle();
    
    @Nonnull
    String getTileDefinitionGuid();

    @Nonnull
    List<TileField> getAllFieldsDefinitions();

    void setId(Long newId);

    // The String will look like : "id,name,type|id,name,type|id,name,type"
    @Nonnull
    String getAllFieldsDefinitionsString();

    boolean isNew();
}
