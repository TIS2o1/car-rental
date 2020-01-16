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
import java.time.format.DateTimeFormatter;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author TIS_2o1
 */
public class ClientHomeController implements Initializable {

    @FXML
    Label car_model;
    @FXML
    Label label;
    @FXML
    Label lease_start;
    @FXML
    Label lease_end;
    @FXML
    Label total_rent;
    @FXML
    Label driver_hire;
    @FXML
    Label model;
    @FXML
    Label start;
    @FXML
    Label end;
    @FXML
    Label rent;
    @FXML
    Label hire;

    private Client clnt;
    @FXML
    private Label ClientName;
    @FXML
    //final AnchorPane anchorPane;

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

    public void initData(Client clnt) throws SQLException {
        this.clnt = clnt;
        String str = "Welcome, " + this.clnt.getName();
        ClientName.setText(str);
        //showRentInfo();
    }

    public void showAllCars(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("allCarView.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        AllCarViewController controller = loader.getController();
        controller.initData(clnt);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setTitle("View all Cars");
        window.setScene(scene);
        window.show();
    }

    public void LogOut(ActionEvent event) throws IOException {
        SceneChanger scene = new SceneChanger();
        scene.changeScenes(event, "home.fxml", "Sign In");
    }

    /*public void showRentInfo() throws SQLException {
        ps = conn.prepareStatement("Select * from rent where client_email=? and is_done='NO'",ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        ps.setString(1, clnt.getEmail());
        result = ps.executeQuery();

        if (result.next()) {
            result.previous();
            while (result.next()) {
                anchorPane = new AnchorPane();
                String id = result.getString("car_id");
                String totalRent = Double.toString(result.getDouble("total_rent"));
                String hireDriver = result.getString("driver_hire");
                LocalDate startDate = convertDate(result.getDate("start_date"));
                LocalDate endDate = convertDate(result.getDate("end_date"));
                if (endDate.compareTo(LocalDate.now()) < 0) {
                    //if lease time is over rent is marked as done
                    ps = conn.prepareStatement("Update rent set is_done='YES' where client_email=?");
                    ps.setString(1, clnt.getEmail());
                    ps.executeUpdate();

                    //car is marked as available
                    ps = conn.prepareStatement("Update cars set availability='AVAILABLE' where id=?");
                    ps.setString(1, id);
                    ps.executeUpdate();

                    label.setVisible(true);
                    label.setText("Lease has expired");

                } else {
                    car_model.setVisible(true);
                    lease_start.setVisible(true);
                    lease_end.setVisible(true);
                    total_rent.setVisible(true);
                    driver_hire.setVisible(true);
                    model.setVisible(true);
                    start.setVisible(true);
                    end.setVisible(true);
                    rent.setVisible(true);
                    hire.setVisible(true);

                    model.setText(id);
                    start.setText(startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    end.setText(endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    rent.setText(totalRent);
                    hire.setText(hireDriver);

                }
            }
        } else {
            label.setVisible(true);
            label.setText("No car has been rented");

        }
    }*/
    
    

    public LocalDate convertDate(java.sql.Date sqlDate) {
        return sqlDate.toLocalDate();
    }
}
