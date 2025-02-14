package io.dummymaker.util;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import org.junit.Assert;
import org.junit.Test;

/**
 * "default comment"
 *
 * @author GoodforGod
 * @since 04.03.2018
 */
public class DateUtilsErrorTests extends Assert {

    @Test
    public void convertNullToTimestampDateOldType() {
        Date date = null;
        final Timestamp t = DateUtils.convertToTimestamp(date);
        assertNull(t);
    }

    @Test
    public void convertNullToTimestampDate() {
        LocalDate localDate = null;
        final Timestamp t = DateUtils.convertToTimestamp(localDate);
        assertNull(t);
    }

    @Test
    public void convertNullToTimestampTime() {
        LocalTime localTime = null;
        final Timestamp t = DateUtils.convertToTimestamp(localTime);
        assertNull(t);
    }

    @Test
    public void convertNullToTimestampDateTime() {
        LocalDateTime localDateTime = null;
        final Timestamp t = DateUtils.convertToTimestamp(localDateTime);
        assertNull(t);
    }

    @Test
    public void parseDateNull() {
        final LocalDateTime result = DateUtils.parseDateTime(null);
        assertNull(result);
    }

    @Test
    public void parseDateEmpty() {
        final LocalDateTime result2 = DateUtils.parseDateTime(" 5125  ");
        assertNull(result2);
    }

    @Test
    public void parseDateOldTypeNull() {
        final LocalTime result = DateUtils.parseTime(null);
        assertNull(result);
    }

    @Test
    public void parseDateOldEmpty() {
        final LocalTime result2 = DateUtils.parseTime("  124  ");
        assertNull(result2);
    }

    @Test
    public void parseTimeNull() {
        final LocalDate result = DateUtils.parseDate(null);
        assertNull(result);
    }

    @Test
    public void parseTimeEmpty() {
        final LocalDate result2 = DateUtils.parseDate("  61361  ");
        assertNull(result2);
    }

    @Test
    public void parseDateTimeNull() {
        final Date result = DateUtils.parseSimpleDateLong(null);
        assertNull(result);
    }

    @Test
    public void parseDateTimeEmpty() {
        final Date result2 = DateUtils.parseSimpleDateLong("  61361  ");
        assertNull(result2);
    }

    @Test
    public void parseDateInvalidFormat() {
        final String asString = "10:101515125/";
        final LocalDateTime result = DateUtils.parseDateTime(asString);
        assertNull(result);
    }

    @Test
    public void parseDateOldTypeInvalidFormat() {
        final String asString = "10:10112515/";
        final LocalTime result = DateUtils.parseTime(asString);
        assertNull(result);
    }

    @Test
    public void parseTimeInvalidFormat() {
        final String asString = "10:101125125/";
        final LocalDate result = DateUtils.parseDate(asString);
        assertNull(result);
    }

    @Test
    public void parseDateTimeInvalidFormat() {
        final String asString = "10:101512512/";
        final Date result = DateUtils.parseSimpleDateLong(asString);
        assertNull(result);
    }
}
