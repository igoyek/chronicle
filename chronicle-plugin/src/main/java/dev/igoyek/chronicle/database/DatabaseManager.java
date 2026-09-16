package dev.igoyek.chronicle.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DatabaseManager {

    private static final long LEAK_DETECTION_THRESHOLD_MILLIS = 5000L;
    private static final int QUERY_TIMEOUT_SECONDS = 5;

    private final Logger logger;
    private final File dataFolder;
    private final DatabaseConfig config;

    private HikariDataSource dataSource;

    public DatabaseManager(Logger logger, File dataFolder, DatabaseConfig config) {
        this.logger = logger;
        this.dataFolder = dataFolder;
        this.config = config;
    }

    public void connect() {
        this.validateTablePrefix();

        if (this.config.databaseType.isFileBased() && !this.dataFolder.exists() && !this.dataFolder.mkdirs()) {
            throw new IllegalStateException("Could not create plugin data folder: " + this.dataFolder.getAbsolutePath());
        }

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(this.config.databaseType.buildJdbcUrl(this.config, this.dataFolder));
        hikariConfig.setDriverClassName(this.config.databaseType.driverClassName());

        if (!this.config.databaseType.isFileBased()) {
            hikariConfig.setUsername(this.config.username);
            hikariConfig.setPassword(this.config.password);
        }

        hikariConfig.setMaximumPoolSize(this.config.databaseType.isFileBased() ? 1 : this.config.poolSize);
        hikariConfig.setConnectionTimeout(this.config.connectionTimeout);
        hikariConfig.setLeakDetectionThreshold(LEAK_DETECTION_THRESHOLD_MILLIS);
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        try {
            this.dataSource = new HikariDataSource(hikariConfig);
        } catch (Exception exception) {
            throw new IllegalStateException("Could not connect to the " + this.config.databaseType + " database", exception);
        }

        this.logger.info("Connected to " + this.config.databaseType + " database.");
    }

    /**
     * @return a pooled connection; caller is responsible for closing it (use try-with-resources).
     */
    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    public int queryTimeoutSeconds() {
        return QUERY_TIMEOUT_SECONDS;
    }

    public String tableName(String suffix) {
        return this.config.tablePrefix + suffix;
    }

    public void close() {
        if (this.dataSource != null && !this.dataSource.isClosed()) {
            this.dataSource.close();
        }
    }

    private void validateTablePrefix() {
        if (!this.config.tablePrefix.matches("[a-zA-Z0-9_]*")) {
            throw new IllegalStateException(
                    "Invalid database.tablePrefix '" + this.config.tablePrefix + "' — only letters, digits and underscore are allowed"
            );
        }
    }
}