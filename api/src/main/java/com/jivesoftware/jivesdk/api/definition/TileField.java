package com.jivesoftware.jivesdk.api.definition;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

import javax.annotation.Nonnull;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 19/5/13
 * Time: 2:03 PM
 */
@JsonDeserialize(as = TileFieldImpl.class)
public interface TileField {
    public static String TILE_FIELDS_DELIMS = ",";

    String getId();

    String getName();

    @Nonnull
    Type getType();

    public static enum Type {
        /**
         * Not to be used, it's only a fallback for serialization issues
         */
        UNKNOWN(""),
        STRING("String"),
        BOOLEAN("Boolean"),
        DOUBLE("Double"),
        DATE("Date"),
        URL("Url");

        @Nonnull
        private String name;

        Type(@Nonnull String name) {
            this.name = name;
        }

        @Nonnull
        public String getName() {
            return name;
        }

        public static Type fromName(@Nonnull String name) {
            for (Type type : values()) {
                if (!type.equals(UNKNOWN) && name.equalsIgnoreCase(type.getName())) {
                    return type;
                }
            }

            return UNKNOWN;
        }
    }
}
