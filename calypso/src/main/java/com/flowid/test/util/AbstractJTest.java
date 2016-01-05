package com.flowid.test.util;

import java.io.File;
import java.io.IOException;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flowid.futils.AppAssert;

public abstract class AbstractJTest {
    private static final Logger logger = LoggerFactory.getLogger(AbstractJTest.class);

    private static final String APP_CLIENT_TIME_ZONE = "America/New_York";
    protected static final String APP_CONFIG_DIR = "app.config.dir";

    static private final File inDir = new File("src/test/data/in");
    static private File outDir = new File("target/test/data/out");
    static private File ctrlDir = new File("src/test/data/ctrl");

    static public void setUp() {
        checkAndSetSystemProperty(APP_CONFIG_DIR, "src/main/config/env/local");
        TimeZone.setDefault(TimeZone.getTimeZone(APP_CLIENT_TIME_ZONE));
        DateTimeZone.setDefault(DateTimeZone.forID(APP_CLIENT_TIME_ZONE));
    }

    static public void teadDown() {

    }

    static protected boolean areEqual(File expected, File actual) throws IOException {
        boolean equal = FileUtils.contentEquals(expected, actual);
        if (!equal) {
            logger.debug("Files not equal. diff {} {}", actual, expected);
        }
        return equal;
    }

    static protected File getCtrlFile(File output) {
        String path = output.getPath();
        path = path.replace(outDir.getPath(), ctrlDir.getPath());
        return new File(path);
    }

    static protected boolean sameAsCtrlFile(File output) throws IOException {
        return areEqual(getCtrlFile(output), output);
    }

    protected static String checkAndSetSystemProperty(String propName, String dflt) {
        String val = System.getProperty(propName);
        if (val == null) {
            val = dflt;
            System.setProperty(propName, val);
        }
        return val;
    }

    public File outDir() {
        File od = new File(outDir, this.getClass().getSimpleName());
        boolean success = od.isDirectory() || od.mkdirs();
        AppAssert.isTrue(success, "Failed to create the directory " + od.getAbsolutePath());
        return od;
    }

    public File inDir() {
        return inDir;
    }
}
