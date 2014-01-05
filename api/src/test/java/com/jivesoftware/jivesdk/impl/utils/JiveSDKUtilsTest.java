package com.jivesoftware.jivesdk.impl.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JiveSDKUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(JiveSDKUtilsTest.class);

    @Before
    public void setUp() throws Exception {
        log.info("Starting test: JiveSDKUtilsTest");
    }

    @Test
    public void trimValueTest_longNumberWithE_goodTrimg() {
        // do the push
        String trimedValue = JiveSDKUtils.trimValue("3.5E8", 30, false);
        Assert.assertEquals("the trim should be good", "350,000,000.00", trimedValue);
    }

    @Test
    public void trimValueTest_longNumberWithDolar_goodTrimg() {
        // do the push
        String trimedValue = JiveSDKUtils.trimValue("$2300000.0", 30, false);
        Assert.assertEquals("the trim should be good", "$2,300,000.00", trimedValue);
    }

    @Test
    public void trimValueTest_longNumber_goodTrimg() {
        // do the push
        String trimedValue = JiveSDKUtils.trimValue("2300000.0", 30, false);
        Assert.assertEquals("the trim should be good", "2,300,000.00", trimedValue);
    }

    @Test
    public void trimValueTest_date_goodTrimg() {
        // do the push
        String trimedValue = JiveSDKUtils.trimValue("2013-12-2", 30, false);
        Assert.assertEquals("the trim should be good", "2013-12-2", trimedValue);
    }

    @Test
    public void trimValueTest_string_goodTrimg() {
        // do the push
        String trimedValue = JiveSDKUtils.trimValue("sharon.grihovod", 30, false);
        Assert.assertEquals("the trim should be good", "sharon.grihovod", trimedValue);
    }

    @Test
    public void trimValueTest_longNumberWithoutDot_goodTrimg() {
        // do the push
        String trimedValue = JiveSDKUtils.trimValue("2300000", 30, true);
        Assert.assertEquals("the trim should be good", "2,300,000.00", trimedValue);
    }

    @Test
    public void trimValueTest_longNumberWithDolarWithoutDot_goodTrimg() {
        // do the push
        String trimedValue = JiveSDKUtils.trimValue("$2300000", 30, true);
        Assert.assertEquals("the trim should be good", "$2,300,000.00", trimedValue);
    }

    @Test
    public void trimValueTest_StringValue() {
        String value = "Closed Won";
        String trimmedValue = JiveSDKUtils.trimValue(value, 30, false);
        Assert.assertEquals("The trim should be good", value, trimmedValue);
    }

    @Test
    public void isAllExistCheckPositive() {
        Assert.assertTrue("Should be positive", JiveSDKUtils.isAllExist("a"));
        Assert.assertTrue("Should be positive", JiveSDKUtils.isAllExist("a", "b"));
        Assert.assertTrue("Should be positive", JiveSDKUtils.isAllExist("a", "b", "1", " 2"));
    }

    @Test
    public void isAllExistCheckNegative() {
        Assert.assertFalse("Should be negative", JiveSDKUtils.isAllExist((Object[]) null));
        Assert.assertFalse("Should be negative", JiveSDKUtils.isAllExist(""));
        Assert.assertFalse("Should be negative", JiveSDKUtils.isAllExist(null, ""));
        Assert.assertFalse("Should be negative", JiveSDKUtils.isAllExist("a", null, ""));
        Assert.assertFalse("Should be negative", JiveSDKUtils.isAllExist(" a", null, ""));
        Assert.assertFalse("Should be negative", JiveSDKUtils.isAllExist("a ", null, ""));
    }
}