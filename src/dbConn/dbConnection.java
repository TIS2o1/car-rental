/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbConn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author TIS_2o1
 */
public class dbConnection {
      public static Connection ConnectionStart() throws SQLException{
        Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/rental", "sanjana", "root");
        System.out.println("Connection established.");
        return conn;
    }
}
