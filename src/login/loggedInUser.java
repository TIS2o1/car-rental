/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

import static dbConn.dbConnection.ConnectionStart;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author TIS_2o1
 */
public class loggedInUser {
    Connection conn = null;
    
    ResultSet result = null;
    PreparedStatement ps = null;
    
    public loggedInUser() throws SQLException{
        conn = ConnectionStart();
    }
    
    public Company getCompanyInfo() throws SQLException{
        ps = conn.prepareStatement("Select email from users where loggedIn='YES'");
        result = ps.executeQuery();
        result.next();
        String email = result.getString("email");
        ps = conn.prepareStatement("Select cname,contact,location from company where email=? ");
        ps.setString(1, email);
        result = ps.executeQuery();
        result.next();
        String compName = result.getString("cname");
        String location = result.getString("location");
        int contact = result.getInt("contact");
        
        Company comp = new Company(compName,email,location,contact);
        return comp;
    }
    
    //public void LogOut(){
        //ps = conn.prepareStatement("Update users set loggedIN")
    //}
}
