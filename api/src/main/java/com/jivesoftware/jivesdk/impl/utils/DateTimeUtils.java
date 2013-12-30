package com.jivesoftware.jivesdk.impl.utils;

import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 17/1/13
 * Time: 2:20 PM
 */
public class DateTimeUtils {
    private static final Logger log = LoggerFactory.getLogger(DateTimeUtils.class);
    private static final SimpleDateFormat fullISODate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private static final SimpleDateFormat simpleISODate = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Converts an ISO date String to a Java date object.
     *
     * @param dateString The ISO date String
     * @return The matching date object
     * @throws IllegalArgumentException In case parsing failed
     */
    @Nullable
    public static Date isoToDate(@Nonnull String dateString) {
        try {
            return fullISODate.parse(dateString);
        } catch (Exception e) {
            log.error("Failed converting ISO date string to date: " + dateString, e);
            return null;
        }
    }

    /**
     * Converts simple date String (YYYY-MM-DD) to a Java date object.
     *
     * @param dateString The date String
     * @return The matching date object
     * @throws IllegalArgumentException In case parsing failed
     */
    @Nullable
    public static Date simpleDateStringToDate(@Nonnull String dateString) {
        try {
            return simpleISODate.parse(dateString);
        } catch (Exception e) {
            log.error("Failed converting simple date string to date: " + dateString, e);
            return null;
        }
    }

    @Nullable
    public static String dateToIso(@Nonnull Optional<Date> date) {
        if (!date.isPresent()) {
            return null;
        }

        try {
            return fullISODate.format(date.get());
        } catch (Exception e) {
            log.error("Failed converting date to ISO date string: " + date, e);
            return null;
        }
    }
}
