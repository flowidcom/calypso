package com.flowid.rest.test;

import java.io.File;
import java.io.IOException;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTimeZone;

/**
 * Shared functionality for testing based on data files.
 */
public abstract class AbstractJTest {
    private static final String AMERICAN_CHICAGO = "America/New York";
    protected static final String APP_CONFIG_DIR = "app.config.dir";

    static protected final File inDir = new File("src/test/data/in");
    static protected File outDir = new File("target/test/data/out");
    static protected File ctrlDir = new File("src/test/data/ctrl");
    static protected File appConfigDir = new File("src/test/data/config");

    static protected void setUp() {
        outDir.mkdirs();

        if (System.getProperty(APP_CONFIG_DIR) == null) {
            System.setProperty(APP_CONFIG_DIR, appConfigDir.getPath());
        }
        TimeZone.setDefault(TimeZone.getTimeZone(AMERICAN_CHICAGO));
        DateTimeZone.setDefault(DateTimeZone.forID(AMERICAN_CHICAGO));
    }

    static protected void setUp(String newOutDir) {
        if (newOutDir != null) {
            outDir = new File(outDir, newOutDir);
            ctrlDir = new File(ctrlDir, newOutDir);
        }
        setUp();
    }

    static protected boolean areEqual(File expected, File actual) throws IOException {
        return FileUtils.contentEquals(expected, actual);
    }

    static protected File getCtrlFile(File output) {
        String path = output.getPath();
        path = path.replace(outDir.getPath(), ctrlDir.getPath());
        return new File(path);
    }

    static protected boolean sameAsCtrlFile(File output) throws IOException {
        return areEqual(getCtrlFile(output), output);
    }
}
