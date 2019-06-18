/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

import static dbConn.dbConnection.ConnectionStart;
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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import static security.SecurePassword.decrypt;

/**
 * FXML Controller class
 *
 * @author TIS_2o1
 */
public class forgotPasswordController implements Initializable {

    /**
     * Initializes the controller class.
     */
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
    
    public forgotPasswordController() throws SQLException {
        conn = ConnectionStart();
    }
    
    @FXML
    public void forgotPasswordAction(ActionEvent event){
        String eml = email.getText().toString();
        String pass = password.getText().toString();
   
        String sql = "SELECT password,type from users where email=?";
        String sql2 = "Insert into users values (?,?,?)";
        
        String userType = "";
   
        try{
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, eml);
            result = preparedStatement.executeQuery();
            if(!result.next()){
                infoBox("Please enter correct Email", null, "Failed");
            }else{
                boolean flag = false;
                while(result.next()){
                    userType = result.getString("type");
                    String decryptedPass = result.getString("password");
                    if(pass.equals(decrypt(decryptedPass))){
                        flag = true;
                    }
                    break;
                }
                if(flag==true){
                    infoBox("Please enter another password. This password has been used once", null, "Failed");
                }else{
                    preparedStatement = conn.prepareStatement(sql2);
                    preparedStatement.setString(1, eml);
                    preparedStatement.setString(2, pass);
                    preparedStatement.setString(3, userType);
                        
                    preparedStatement.executeUpdate();
                        
                    infoBox("Password has been succesfully saved",null,"Success" );
                    Node node = (Node)event.getSource();
                    dialogStage = (Stage) node.getScene().getWindow();
                    dialogStage.close();
                    scene = new Scene(FXMLLoader.load(getClass().getResource("Home.fxml")));
                    dialogStage.setScene(scene);
                    dialogStage.show();     
                    
                }
            }
                

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
    
    
}
