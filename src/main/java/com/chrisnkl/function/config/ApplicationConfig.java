package com.chrisnkl.function.config;

import com.chrisnkl.function.exception.EnvironmentVariableMissingException;

public class ApplicationConfig {

    public static final String STORAGE_ACCOUNT = getEnvironmentVariable(ApplicationSetting.STORAGE_ACCOUNT_NAME.name());
    public static final String INVOICES_CONTAINER = getEnvironmentVariable(ApplicationSetting.INVOICES_CONTAINER_NAME.name());

    public static String getEnvironmentVariable(String key) throws EnvironmentVariableMissingException {
        String variable = System.getenv(key);
        if (variable == null) {
            variable = System.getProperty(key);
        }
        if (variable == null) throw new EnvironmentVariableMissingException(String.format("Environment variable %s is missing.", key));
        return variable;
    }

}
