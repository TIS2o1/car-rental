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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import static security.SecurePassword.encrypt;

/**
 * FXML Controller class
 *
 * @author TIS_2o1
 */
public class SignUpClientController implements Initializable {

    /**
     * Initializes the controller class.
     */
     @FXML
    private TextField email;
    @FXML
    private TextField name;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField conf_pass;
    @FXML
    private TextField contact;
    @FXML
    private TextArea address;
    
    Stage dialogStage = new Stage();
    Scene scene;
    
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    ResultSet result = null;
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         try {
             conn = ConnectionStart();
         } catch (SQLException ex) {
             Logger.getLogger(SignUpClientController.class.getName()).log(Level.SEVERE, null, ex);
         }
    }    
    public void SignUpAction(ActionEvent event){
        String eml = email.getText().toString();
        String cl_name = name.getText().toString();
        String pass = password.getText().toString();
        String confPass = conf_pass.getText().toString();
        String cont = contact.getText().toString();
        String addr = address.getText().toString();
    
        String sql1 = "INSERT INTO USERS values (?,?,'client')";
        String sql2 = "INSERT INTO CLIENT (cl_name,email,address,contact) values (?,?,?,?)";
        String sql3 = "SELECT * FROM CLIENT WHERE email=?";
        String sql4 ="INSERT INTO USER_PASSWORD values(?,?)";
        
        try{
            preparedStatement = conn.prepareStatement(sql3);
            preparedStatement.setString(1, eml);
            result = preparedStatement.executeQuery();
            if(result.next()){
                infoBox("Email taken by another account", null, "Failed");
                email.clear();
            }else{
                  if(!(pass.equals(confPass))){
                      infoBox("Password Mismatch", null, "Failed");
                      password.clear();
                      conf_pass.clear();
                  }else if(validateEmail(eml)==false){
                      infoBox("Invalid Email Address", null, "Failed");
                      email.clear();
                  }else if(validatePassword(pass)==false){
                      infoBox("Password must be greater than 7 characters", null, "Failed");
                      password.clear();
                      conf_pass.clear();
                  }else if(validateMobile(cont)==false){
                      infoBox("Invalid Mobile Number", null, "Failed");
                      contact.clear();
                  }else{
                      pass = encrypt(pass);
                      preparedStatement = conn.prepareStatement(sql1);
                      preparedStatement.setString(1, eml);
                      preparedStatement.setString(2, pass);
                      preparedStatement.executeUpdate();
                      
                      preparedStatement = conn.prepareStatement(sql4);
                      preparedStatement.setString(1, eml);
                      preparedStatement.setString(2, pass); 
                      preparedStatement.executeUpdate();
                      
                      
                      preparedStatement = conn.prepareStatement(sql2);
                      preparedStatement.setString(1, cl_name);
                      preparedStatement.setString(2, eml);
                      preparedStatement.setString(3, addr);
                      preparedStatement.setString(4, cont);
                      preparedStatement.executeUpdate();
                      
                      infoBox("Sign Up Successfull",null,"Success" );
                      Node node = (Node)event.getSource();
                      dialogStage = (Stage) node.getScene().getWindow();
                      dialogStage.close();
                      scene = new Scene(FXMLLoader.load(getClass().getResource("home.fxml")));
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
    
    private boolean validateEmail(String email){
        Pattern pattern = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+");
        Matcher match = pattern.matcher(email);
        if(match.find() && match.group().equals(email)){
            return true;
        }
        else{
            return false;
        }
    }
    
    private boolean validatePassword(String password){
        if(password.length()>7){
            return true;
        }
        else{
            return false;
        }
    }
    
    private boolean validateMobile(String mobile){
        Pattern pattern = Pattern.compile("^(?:\\+?88)?01[15-9]\\d{8}$");
        Matcher match = pattern.matcher(mobile);
        if(match.matches()){
            return true;
        }
        else{
            return false;
        }
    }
    
    public void signInButton(ActionEvent event) throws IOException{
        SceneChanger scene = new SceneChanger();
        scene.changeScenes(event, "home.fxml", "Sign In");
    }
    
}
