package dev.igoyek.chronicle.database;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Exclude;

public class DatabaseConfig extends OkaeriConfig {

    @Comment({"Type of database to use.", "Available: SQLITE, MYSQL, MARIADB, POSTGRESQL"})
    public DatabaseType databaseType = DatabaseType.SQLITE;

    @Comment("File name used when databaseType is SQLITE. Ignored for other database types.")
    public String fileName = "chronicle.db";

    @Comment("Hostname of the database server. Ignored for SQLITE.")
    public String hostname = "localhost";

    @Comment({"Port of the database server. Common defaults:", " - MySQL/MariaDB: 3306", " - PostgreSQL: 5432"})
    public int port = 3306;

    @Comment("Name of the database/schema to connect to. Ignored for SQLITE.")
    public String database = "chronicle";

    @Comment("Username used to authenticate. Ignored for SQLITE.")
    public String username = "root";

    @Comment("Password used to authenticate. Ignored for SQLITE.")
    public String password = "password";

    @Comment("Whether to use SSL/TLS for the connection. Ignored for SQLITE.")
    public boolean ssl = false;

    @Comment({"Maximum number of pooled connections.", "Forced to 1 for SQLITE regardless of this value, since SQLite does not support concurrent writers."})
    public int poolSize = 10;

    @Comment("Maximum time (in milliseconds) to wait for a connection from the pool before failing.")
    public int connectionTimeout = 10000;

    @Comment({"Prefix added to every table name.", "Useful when sharing one database between multiple plugins or server instances."})
    public String tablePrefix = "chronicle_";
}
