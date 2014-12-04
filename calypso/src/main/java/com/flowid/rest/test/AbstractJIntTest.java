package com.flowid.rest.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractJIntTest extends AbstractJTest {
    private static final Logger logger = LoggerFactory.getLogger(AbstractJIntTest.class);
    private static final String APP_NAME = "calypso";
    private static final String APP_ENV = "app.env";
    protected static final String ENV_LOCAL = "local";
    protected static final String ENV_QA = "qa";
    private static final String DEFAULT_ENV = ENV_LOCAL;

    // runtime environment for this test
    private static String env;

    static protected void setEnv(String testEnv) {
        // command line system property overwrites this constructor's parameter
        String env = System.getProperty(APP_ENV);
        if (env == null || env.length() == 0)
        {
            // the environment is not set with an environment var
            if (testEnv == null)
            {
                logger.debug("No environment defined, it will be set to the default value.");
                env = DEFAULT_ENV;
            }
            else {
                env = testEnv;
            }

            System.setProperty(APP_ENV, env);
        }


        String appConfigDir = System.getProperty(APP_CONFIG_DIR);
        if (appConfigDir == null) {
            appConfigDir = "../" + APP_NAME + "-config/env/" + env;
        }
        System.setProperty(APP_CONFIG_DIR, appConfigDir);

        logger.debug("Environment: " + System.getProperty(APP_ENV));
        logger.debug("App config dir: " + System.getProperty(APP_CONFIG_DIR));
    }

    static protected String getEnv() {
        return env;
    }

    static protected boolean inEnv(String runtimeEnv) {
        return env.equals(runtimeEnv);
    }

}
