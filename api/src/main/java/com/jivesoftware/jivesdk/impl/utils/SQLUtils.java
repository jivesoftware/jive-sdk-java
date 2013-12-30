package com.jivesoftware.jivesdk.impl.utils;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 12/1/13
 * Time: 10:24 PM
 */
public class SQLUtils {
    @Nonnull
    public static String toSqlList(boolean wrapInQuotes, @Nonnull Object... objects) {
        StringBuilder sb = new StringBuilder();
        for (Object s : objects) {
            if (sb.length() > 0) {
                sb.append(',');
            }

            if (wrapInQuotes) {
                sb.append('\'');
            }

            sb.append(s.toString());

            if (wrapInQuotes) {
                sb.append('\'');
            }
        }
        return sb.toString();
    }

    @Nonnull
    public static String toSqlList(boolean wrapInQuotes, @Nonnull Collection objects) {
        return toSqlList(wrapInQuotes, objects.toArray());
    }

    public static String createUpdateStringFromMap(Map<String, Object> values, List<Object> insertedValues){
        StringBuilder queryBuilder = new StringBuilder();
        boolean empty = true;
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            // Check if need to insert an separator
            if (!empty) {
                queryBuilder.append(", ");
            }
            else{
                empty = false;
            }

            queryBuilder.append(entry.getKey());
            Object entryValue = entry.getValue();
            if(entryValue == null){
                queryBuilder.append("=NULL");
            }
            else {
                queryBuilder.append("=?");
                insertedValues.add(entryValue);
            }
        }
        return queryBuilder.toString();
    }

    public static String createUpdateStringFromMap(Map<String, Object> values)
    {
        StringBuilder queryBuilder = new StringBuilder();
        boolean empty = true;
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            // Check if need to insert an separator
            if (!empty) {
                queryBuilder.append(", ");
            }
            else{
                empty = false;
            }
            queryBuilder.append(entry.getKey()).append("=?");
        }
        return queryBuilder.toString();
    }
}
