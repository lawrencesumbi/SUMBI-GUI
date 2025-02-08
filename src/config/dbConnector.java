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

}
