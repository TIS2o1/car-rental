/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

import static dbConn.dbConnection.ConnectionStart;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author TIS_2o1
 */
public class ClientHomeController implements Initializable {

    private Client clnt;
    @FXML private Label ClientName;
    
    Connection conn = null;
    
    ResultSet result = null;
    PreparedStatement ps = null;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            conn = ConnectionStart();
        } catch (SQLException ex) {
            Logger.getLogger(ClientHomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }   
    
    public void initData(Client clnt){
        this.clnt = clnt;
        String str = "Welcome, "+this.clnt.getName();
        ClientName.setText(str);
    }
    
    public void showAllCars(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("allCarView.fxml"));
        Parent parent = loader.load();
        
        Scene scene = new Scene(parent);
        
        AllCarViewController controller = loader.getController();
        controller.initData(clnt);
        
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        
        window.setTitle("View all Cars");
        window.setScene(scene);
        window.show();
    }
    
    public void LogOut(ActionEvent event) throws IOException{
        SceneChanger scene = new SceneChanger();
        scene.changeScenes(event, "home.fxml", "Sign In");
    }
    
    public void showRentInfo() throws SQLException{
        ps = conn.prepareStatement("Select end_date from rent where email=?");
        ps.setString(1, clnt.getEmail());
        result = ps.executeQuery();
        if(result.next()){
            LocalDate date = convertDate(result.getDate("end_date"));
            if(date.compareTo(LocalDate.now())<0){
                
            }
        }
    }
    
    public LocalDate convertDate(java.sql.Date sqlDate){
        return sqlDate.toLocalDate();
    }
}
