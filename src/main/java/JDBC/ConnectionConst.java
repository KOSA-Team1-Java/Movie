package JDBC;

import io.github.cdimascio.dotenv.Dotenv;

public class ConnectionConst {
    private static final Dotenv dotenv = Dotenv.load();

    public static final String driver = "com.postgreSQL.jdbc.Driver";
    public static final String URL = dotenv.get("DB_URL");
    public static final String USERNAME = dotenv.get("DB_USERNAME");
    public static final String PASSWORD = dotenv.get("DB_PASSWORD");
}
