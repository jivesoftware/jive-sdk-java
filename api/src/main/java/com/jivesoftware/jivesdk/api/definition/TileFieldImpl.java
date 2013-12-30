package com.jivesoftware.jivesdk.api.definition;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.annotation.Nonnull;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 16/5/13
 * Time: 5:53 PM
 */
public class TileFieldImpl implements TileField {
    private String id;
    private String name;
    private Type type;

    @JsonCreator
    public TileFieldImpl(@JsonProperty("id") String id,
                         @JsonProperty("name") String name,
                         @JsonProperty("type") String type) {
        this.id = id;
        this.name = name;
        this.type = type != null ? Type.fromName(type) : Type.STRING;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    @Nonnull
    public Type getType() {
        return type;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TileFieldImpl tileField = (TileFieldImpl) o;

        return !(id != null ? !id.equals(tileField.id) : tileField.id != null) && !(name != null ? !name.equals(tileField.name) : tileField.name != null) && type == tileField.type;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s%s%s%s%s", id, TILE_FIELDS_DELIMS, name, TILE_FIELDS_DELIMS, type);
    }
}
