package pzionit.lisenceplate;

import java.sql.*;

public class DBUtils {

    public static final String DECISION_TABLE = "ParkingDecisionTable";
    public static final String TIME_TABLE = "ParkingTimeTable";

    static final String selectPlateQuery = "SELECT * FROM %s WHERE plate=\"%s\"";

    static final String DB_NAME = "License.db";
    static final String URL = "jdbc:sqlite:C:/sqlite/" + DB_NAME;
    public static final String createDecisionTableQuery = "CREATE TABLE IF NOT EXISTS %s (\n"
            + "	id integer PRIMARY KEY,\n"
            + "	plate text NOT NULL,\n"
            + "	decision text NOT NULL\n"
            + ");";
    public static final String createTimeTableQuery = "CREATE TABLE IF NOT EXISTS %s (\n"
            + "	id integer PRIMARY KEY,\n"
            + "	plate text NOT NULL,\n"
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
    public static void createDBTables() {
        try (Connection conn = connect()) {
            Statement statement = conn.createStatement();
            statement.execute(String.format(createDecisionTableQuery, DECISION_TABLE));
            statement.execute(String.format(createTimeTableQuery, TIME_TABLE));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * insert to Time Table
     *
     * @param plate     - plate
     * @param timestamp - timestamp
     */
    public static void insertTimeTable(String plate, String timestamp) {
        String sql = "INSERT INTO " + TIME_TABLE + "(plate,timestamp) VALUES(?,?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, plate);
            pstmt.setString(2, timestamp);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * insert to Data Table
     *
     * @param plate    - plate
     * @param decision - decision
     */
    public static void insertDataTable(String plate, String decision) {
        String sql = "INSERT INTO " + DECISION_TABLE + "(plate,decision) VALUES(?,?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, plate);
            pstmt.setString(2, decision);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Check if the plate exists in the decision table, no need to check or to insert it again.
     *
     * @param tableName - tableName
     * @param plate     - plate
     * @return - true if plate exists in table
     */
    public static boolean isAlreadyExist(String tableName, String plate) {
        try (Connection conn = connect()) {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(String.format(selectPlateQuery, tableName, plate));
            if (rs.next()) {
                boolean isFound = rs.getBoolean(1);
                if (isFound) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}