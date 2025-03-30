package config;

import java.sql.*;

public class dbConnector {
 
    public Connection connect;
    
    public dbConnector(){
        try{
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/sumbi_db", "root", "");
        }catch(SQLException ex){
            System.out.println("Can't connect to database: "+ex.getMessage());
        }
    } 
    
    public ResultSet getData(String sql)throws SQLException{
        Statement stmt = connect.createStatement();
        ResultSet rst = stmt.executeQuery(sql);       
        return rst;        
    }
    
    public Connection getConnection() {
        return connect;
    }
    
    public void logActivity(int user_id, String logs_action) {
        String query = "INSERT INTO tbl_logs (user_id, log_action) VALUES (?, ?)";
        try (PreparedStatement pstmt = connect.prepareStatement(query)) {
            pstmt.setInt(1, user_id);
            pstmt.setString(2, logs_action);
            pstmt.executeUpdate();
            System.out.println("Activity logged: " + logs_action);
        } catch (SQLException e) {
            System.out.println("Error logging activity: " + e.getMessage());
        }
    }

    public ResultSet getLogs() throws SQLException {
        String query = "SELECT l.logs_id AS 'Log ID', " +
                       "u.user_fname AS 'Username', " +
                       "l.logs_action AS 'Action', " +
                       "l.logs_stamp AS 'Timestamp' " +
                       "FROM logs_table l " +
                       "JOIN user_table u ON l.user_id = u.u_id " +
                       "ORDER BY l.log_timestamp DESC";

        return getData(query);
    }

}
