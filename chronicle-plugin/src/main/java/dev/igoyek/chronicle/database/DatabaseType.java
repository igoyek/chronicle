package dev.igoyek.chronicle.database;

import java.io.File;

public enum DatabaseType {

    SQLITE("org.sqlite.JDBC") {
        @Override
        public String buildJdbcUrl(DatabaseConfig config, File dataFolder) {
            File file = new File(dataFolder, config.fileName);
            return "jdbc:sqlite:" + file.getAbsolutePath() + "?journal_mode=WAL&busy_timeout=5000";
        }
    },
    MYSQL("com.mysql.cj.jdbc.Driver") {
        @Override
        public String buildJdbcUrl(DatabaseConfig config, File dataFolder) {
            return "jdbc:mysql://" + config.hostname + ":" + config.port + "/" + config.database
                    + "?useSSL=" + config.ssl + "&useUnicode=true&characterEncoding=utf8";
        }
    },
    MARIADB("org.mariadb.jdbc.Driver") {
        @Override
        public String buildJdbcUrl(DatabaseConfig config, File dataFolder) {
            return "jdbc:mariadb://" + config.hostname + ":" + config.port + "/" + config.database
                    + "?useSSL=" + config.ssl;
        }
    },
    POSTGRESQL("org.postgresql.Driver") {
        @Override
        public String buildJdbcUrl(DatabaseConfig config, File dataFolder) {
            return "jdbc:postgresql://" + config.hostname + ":" + config.port + "/" + config.database
                    + "?ssl=" + config.ssl;
        }
    };

    private final String driverClassName;

    DatabaseType(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String driverClassName() {
        return this.driverClassName;
    }

    public abstract String buildJdbcUrl(DatabaseConfig config, File dataFolder);

    public boolean isFileBased() {
        return this == SQLITE;
    }
}