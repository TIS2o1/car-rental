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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import static security.SecurePassword.decrypt;
import static security.SecurePassword.encrypt;


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
   
        //Checking old passwords with new password
        String sql = "SELECT password from user_password where email=?";
        //Inserting new password in tables
        String sql2 = "Insert into user_password values (?,?)";
        String sql3 = "Update users set password=? where email=?";
   
        try{
            preparedStatement = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, eml);
            result = preparedStatement.executeQuery();
            if(result.next()){
                result.previous();
                boolean flag = false;
                String encryptedPass = null;
                while(result.next()){
                    encryptedPass = result.getString("password");
                    if(pass.equals(decrypt(encryptedPass))){
                        flag = true;
                        break;
                    }
                }
                if(flag==true){
                    infoBox("Please enter another password. This password has been used once", null, "Failed");
                }else if(validatePassword(pass)==false){
                    infoBox("Password must be greater than 7 characters", null, "Failed");
                    password.clear();
                }else{
                    //Inserting new password in users password table
                    pass = encrypt(pass);
                    preparedStatement = conn.prepareStatement(sql2);
                    preparedStatement.setString(1, eml);
                    preparedStatement.setString(2, pass);
                        
                    preparedStatement.executeUpdate();
                    
                    //updating user's password in user login table
                    preparedStatement = conn.prepareStatement(sql3);
                    preparedStatement.setString(1, pass);
                    preparedStatement.setString(2, eml);
                        
                    preparedStatement.executeUpdate();
                    
                    
                        
                    infoBox("Password has been succesfully saved",null,"Success" );
                    Node node = (Node)event.getSource();
                    dialogStage = (Stage) node.getScene().getWindow();
                    dialogStage.close();
                    scene = new Scene(FXMLLoader.load(getClass().getResource("home.fxml")));
                    dialogStage.setScene(scene);
                    dialogStage.show();     
                    
                }
            }else{
                infoBox("Please enter correct Email", null, "Failed");
                email.clear();
                password.clear();
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
    
    private boolean validatePassword(String password){
        if(password.length()>7){
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
