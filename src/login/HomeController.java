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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import static security.SecurePassword.decrypt;

/**
 * FXML Controller class
 *
 * @author TIS_2o1
 */
public class HomeController implements Initializable {

    /**
     * Initializes the controller class.
     */
    public void changeScreenButtonPushed(ActionEvent event) throws IOException{
        Parent AnchorParent = FXMLLoader.load(getClass().getResource("signUpCompany.fxml"));
        Scene AnchorScene = new Scene(AnchorParent);
        
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(AnchorScene);
        window.show();
    }
    
    public void forgotPasswordButtonPushed(ActionEvent event) throws IOException{
        Parent AnchorParent = FXMLLoader.load(getClass().getResource("forgotPassword.fxml"));
        Scene AnchorScene = new Scene(AnchorParent);
        
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(AnchorScene);
        window.show();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    
    Stage dialogStage = new Stage();
    Scene scene;
    
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    ResultSet result = null;
    
    public HomeController() throws SQLException {
        conn = ConnectionStart();
    }
    
    public void loginAction(ActionEvent event){
        String eml = email.getText().toString();
        String pass = password.getText().toString();
   
        String sql = "SELECT password,type from users where email=?";
        
        try{
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, eml);
            result = preparedStatement.executeQuery();
            if(result.next()){
                String userType = result.getString("type");                
                String decryptedPass = result.getString("password");
                if(pass.equals(decrypt(decryptedPass))){
                Node node = (Node)event.getSource();
                dialogStage = (Stage) node.getScene().getWindow();
                dialogStage.close();
                if(userType.equals("company")){
                    scene = new Scene(FXMLLoader.load(getClass().getResource("CompanyHome.fxml")));
                    CompanyHomeController c = new CompanyHomeController(eml);
                }
                else{
                    scene = new Scene(FXMLLoader.load(getClass().getResource("ClientHome.fxml")));
                }
                dialogStage.setScene(scene);
                dialogStage.show();
                }
            }else{
                infoBox("Please enter correct Email and Password", null, "Failed");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public static void infoBox(String infoMessage, String headerText, String title){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }
    
}
