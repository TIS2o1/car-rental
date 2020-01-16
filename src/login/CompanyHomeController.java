/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
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
public class CompanyHomeController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   
    
    private Company comp;
    @FXML private Label CompanyName;
    
    public void showAddCarView(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AddCar.fxml"));
        Parent parent = loader.load();
        
        Scene scene = new Scene(parent);
        
        AddCarController controller = loader.getController();
        controller.companyInformation(comp);
        
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        
        window.setTitle("Add Car");
        window.setScene(scene);
        window.show();
    }
    
    public void showViewCars(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("CarView.fxml"));
        Parent parent = loader.load();
        
        Scene scene = new Scene(parent);
        
        CarViewController controller = loader.getController();
        controller.createTable(comp);
        
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        
        window.setTitle("View all Cars");
        window.setScene(scene);
        window.show();
    }
    
    public void initData(Company comp){
        this.comp = comp;
        CompanyName.setText((this.comp).getName());
    }
    
    public void LogOut(ActionEvent event) throws IOException{
        SceneChanger scene = new SceneChanger();
        scene.changeScenes(event, "home.fxml", "Sign In");
    }
}
