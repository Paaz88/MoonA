package pzionit.lisenceplate;

import java.sql.*;

public class DBUtils {

    static final String DB_NAME = "License.db";
    static final String URL = "jdbc:sqlite:C:/sqlite/" + DB_NAME;
    static final String TABLE_NAME = "ParkingData";
    public static final String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (\n"
            + "	id integer PRIMARY KEY,\n"
            + "	plate text NOT NULL,\n"
            + "	decision text NOT NULL,\n"
            + " timestamp text NOT NULL\n"
            + ");";

    /**
     * connect to SQLIte DB
     *
     * @return connection
     */
    private static Connection connect() {
        // SQLite connection string
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * execute CREATE TABLE IF NOT EXISTS request with table: TABLE_NAME param
     */
    public static void createDBTable() {
        try (Connection conn = connect()) {
            Statement statement = conn.createStatement();
            statement.execute(createTable);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * insert into TABLE_NAME table the following params:
     *
     * @param plate     - String
     * @param decision  - String
     * @param timestamp - String
     */
    public static void insert(String plate, String decision, String timestamp) {
        String sql = "INSERT INTO " + TABLE_NAME + "(plate,decision,timestamp) VALUES(?,?,?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, plate);
            pstmt.setString(2, decision);
            pstmt.setString(3, timestamp);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}