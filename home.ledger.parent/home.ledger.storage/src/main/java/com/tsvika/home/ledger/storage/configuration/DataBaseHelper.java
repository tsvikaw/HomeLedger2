package com.tsvika.home.ledger.storage.configuration;

import com.tsvika.home.ledger.common.EnvironmentVariablesWrapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * DataBaseHelper
 * Sonar
 * Sonar
 * Sonar
 * Sonar
 * DB_URL=jdbc:mysql://localhost:3306/hl;DB_SCHEMA=hl;DB_USER=admin;DB_PWD=password
 * for debug
 * Sonar
 * Sonar
 */
public class DataBaseHelper {

    // DB_URL default and EnvVar param name
    private static final String DEF_DB_URL = "jdbc:mysql://localhost:3306/hl?useSSL=false";
    private static final String ENV_VAR_DB_URL = "DB_URL";
    // DB_SCHEMA default and EnvVar param name
    private static final String DEF_DB_SCHEMA = "public";
    private static final String ENV_VAR_DB_SCHEMA = "DB_SCHEMA";
    // DB_USER default and EnvVar param name
    private static final String DEF_DB_USER = "admin";
    private static final String ENV_VAR_DB_USER = "DB_USER";
    // DB_P default and EnvVar param name
    private static final String DEF_DB_P = "password";
    private static final String ENV_VAR_DB_P = "DB_PWD";

    public DataSource getDBDataSource() {
        // CPPLogger.trace(SecretManagerHelper.class,"Getting DB DataSource");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        String dbUrl = getDbUrl();
        String dbSchema = getDbSchema();
        List<String[]> userAndPassword = getDbUserAndPass();

        dataSource.setUrl(dbUrl);
        dataSource.setSchema(dbSchema);
        dataSource.setUsername(String.join("", userAndPassword.get(0)));
        dataSource.setPassword(String.join("", userAndPassword.get(1)));

        return dataSource;
    }

    private List<String[]> getDbUserAndPass() {
//        List<String[]> userAndPassword = SecretManagerHelper.getDbUserAndPassword();
//
//        if(userAndPassword.isEmpty()){
            // CPPLogger.warn(SecretManagerHelper.class,"DB U and P not taken from secrets manager - falling back to default");
        List<String[]> userAndPassword = getDefaultUserAndPass();
//        }
        return userAndPassword;
    }

    private List<String[]> getDefaultUserAndPass() {
        List<String[]> defUserPass = new ArrayList<>();
        String[] user = getDbUser().split("");
        String[] pwd = getDbP();
        defUserPass.add(user);
        defUserPass.add(pwd);
        return defUserPass;
    }

    private String getDbUrl() {
        // CPPLogger.trace(SecretManagerHelper.class,"Getting DB URL");
        EnvironmentVariablesWrapper envVarWrapper = new EnvironmentVariablesWrapper();
        String dbUrl = envVarWrapper.getValue(ENV_VAR_DB_URL);
        if (StringUtils.isEmpty(dbUrl)) {
            // CPPLogger.warn(SecretManagerHelper.class,"DB URL env var not set - falling back to default");
            dbUrl = DEF_DB_URL;
        }
        return dbUrl;
    }

    private String getDbSchema() {
        // CPPLogger.trace(SecretManagerHelper.class,"Getting DB SCHEMA");
        EnvironmentVariablesWrapper envVarWrapper = new EnvironmentVariablesWrapper();
        String dbSchema = envVarWrapper.getValue(ENV_VAR_DB_SCHEMA);
        if (StringUtils.isEmpty(dbSchema)) {
            // CPPLogger.warn(SecretManagerHelper.class,"DB SCHEMA env var not set - falling back to default");
            dbSchema = DEF_DB_SCHEMA;
        }
        return dbSchema;
    }

    private String getDbUser() {
        // CPPLogger.trace(SecretManagerHelper.class,"Getting DB USER");
        EnvironmentVariablesWrapper envVarWrapper = new EnvironmentVariablesWrapper();
        String dbUser = envVarWrapper.getValue(ENV_VAR_DB_USER);
        if (StringUtils.isEmpty(dbUser)) {
            // CPPLogger.warn(SecretManagerHelper.class,"DB USER env var not set - falling back to default");
            dbUser = DEF_DB_USER;
        }
        return dbUser;
    }

    private String[] getDbP() {
        // CPPLogger.trace(SecretManagerHelper.class,"Getting DB P");
        EnvironmentVariablesWrapper envVarWrapper = new EnvironmentVariablesWrapper();
        String dbP = envVarWrapper.getValue(ENV_VAR_DB_P);
        if (StringUtils.isEmpty(dbP)) {
            // CPPLogger.warn(SecretManagerHelper.class,"DB P env var not set - falling back to default");
            dbP = DEF_DB_P;
        }
        return dbP.split("");
    }
}
