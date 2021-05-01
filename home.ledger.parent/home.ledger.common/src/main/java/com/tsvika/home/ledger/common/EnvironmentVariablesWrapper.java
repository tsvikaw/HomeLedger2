package com.tsvika.home.ledger.common;

public class EnvironmentVariablesWrapper implements IEnvironmentVariablesWrapper {
    @Override
    public String getValue(String key){
        try {
            return System.getenv(key);
        } catch (Exception e) { //NOSONAR
            // CPPLogger.warn(EnvironmentVariablesWrapper.class, String.format("No environment variable set to %s, Use default value", key));
            return null;
        }
    }
}
