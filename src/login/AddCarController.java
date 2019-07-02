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
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import static security.SecurePassword.encrypt;

/**
 * FXML Controller class
 *
 * @author TIS_2o1
 */
public class AddCarController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   
    @FXML
    private TextField id;
    @FXML
    private TextField model;
    @FXML
    private TextField no_of_seats;
    @FXML
    private TextField price;
    @FXML 
    private TextField name;
    
    
    Stage dialogStage = new Stage();
    Scene scene;
    
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet result = null;
    
    private Company comp;
 
    public AddCarController() throws SQLException {
        conn = ConnectionStart();
    }
    
    
    public void AddCarAction(ActionEvent event) throws SQLException{
        String car_id = id.getText().toString();
        String car_model = model.getText().toString();
        String car_price = price.getText().toString();
        double carPrice = Double.parseDouble(car_price);
        String noOfSeats = no_of_seats.getText().toString();
        int seats = Integer.parseInt(noOfSeats);
        String comp_name = comp.getName();
    
        String sql1 = "INSERT INTO cars values (?,?,?,?,?,'AVAILABLE')";
        
        try{
            
            ps = conn.prepareStatement(sql1);
            ps.setString(1,car_id);
            ps.setString(2,comp_name);
            ps.setString(3,car_model);
            ps.setDouble(4,carPrice);
            ps.setInt(5,seats);
            ps.executeUpdate();
            
            infoBox("Successfully Added",null,"Success" );
            Node node = (Node)event.getSource();
            dialogStage = (Stage) node.getScene().getWindow();
            dialogStage.close();
            scene = new Scene(FXMLLoader.load(getClass().getResource("AddCar.fxml")));
            dialogStage.setScene(scene);
            dialogStage.show();
        }
          
        catch(Exception e){
            e.printStackTrace();
        }
        
    }
    public static void infoBox(String infoMessage, String headerText, String title){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }
    
    public void goBackHome(ActionEvent event) throws IOException{
        Parent AnchorParent = FXMLLoader.load(getClass().getResource("CompanyHome.fxml"));
        Scene AnchorScene = new Scene(AnchorParent);
        
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(AnchorScene);
        window.show();
    }
    
   public void companyInformation(Company comp){
        this.comp = comp;
    }
   
   
}

    

