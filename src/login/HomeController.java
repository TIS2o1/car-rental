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
    
    public void loginAction(ActionEvent event){ //allows user to login with correct email and password
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
                        Company comp = createCompanyInstance(eml);
                        showCompanyHomeView(event,comp);
                        
                    }
                    else{
                        scene = new Scene(FXMLLoader.load(getClass().getResource("ClientHome.fxml")));
                    }
                }
                else{
                    infoBox("Please enter correct password", null, "Failed");
                }
            }else{
                infoBox("Please enter correct Email", null, "Failed");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public Company createCompanyInstance(String eml) throws SQLException{
        String sql = "Select cname,contact,location from company where email=? ";
        preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, eml);
        result = preparedStatement.executeQuery();
        result.next();
        String compName = result.getString("cname");
        String location = result.getString("location");
        int contact = result.getInt("contact");
        
        Company comp = new Company(compName,eml,location,contact);
        return comp;
    }
    
    public static void infoBox(String infoMessage, String headerText, String title){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }
    
    public void showCompanyHomeView(ActionEvent event, Company comp) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("CompanyHome.fxml"));
        Parent parent = loader.load();
        
        Scene scene = new Scene(parent);
        
        CompanyHomeController controller = loader.getController();
        controller.initData(comp);
        
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        
        window.setTitle("Home");
        window.setScene(scene);
        window.show();
    }
    
    public void changeSignUpButtonPushed(ActionEvent event) throws IOException{
        SceneChanger scene = new SceneChanger();
        scene.changeScenes(event, "signUpCompany.fxml", "Sign Up");
    }
    
    public void forgotPasswordButtonPushed(ActionEvent event) throws IOException{
        SceneChanger scene = new SceneChanger();
        scene.changeScenes(event, "forgotPassword.fxml", "Forgot Password");
    }
    
}
