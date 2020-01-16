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
import java.time.Duration;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import static login.HomeController.infoBox;

/**
 * FXML Controller class
 *
 * @author TIS_2o1
 */
public class RentCarController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    Label model;
    @FXML
    Label rent;
    @FXML
    Label total_rent;
    @FXML
    CheckBox checkBox;
    @FXML
    DatePicker pick;
    @FXML
    DatePicker drop;

    private long duration = -1;

    private Client clnt;
    private Car selectedCar;

    private double totalRent;

    Connection conn = null;

    ResultSet result = null;
    PreparedStatement ps = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            conn = ConnectionStart();
        } catch (SQLException ex) {
            Logger.getLogger(RentCarController.class.getName()).log(Level.SEVERE, null, ex);
        }
        pick.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0);
            }
        });
        drop.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0);
            }
        });
    }

    public void initData(Client clnt, Car car) {
        this.clnt = clnt;
        selectedCar = car;
        model.setText(selectedCar.getModel());

    }

    public void calculateRent() {
        LocalDate begin = pick.getValue();
        LocalDate end = drop.getValue();

        if (begin != null && end != null) {
            duration = Duration.between(begin.atStartOfDay(), end.atStartOfDay()).toDays();
            totalRent = selectedCar.getPrice() * duration;
            if (checkBox.isSelected()) {
                totalRent = totalRent + 1000;
                rent.setText(selectedCar.getPrice() + "+1000(driver hire)");
            } else {
                rent.setText(Double.toString((selectedCar.getPrice())));
            }
            total_rent.setText(Double.toString(totalRent));
        } else {
            infoBox("Please Enter Valid Duration", null, "Failed");
        }

    }

    public void confirmCar(ActionEvent event) throws SQLException {
        if (totalRent != 0.0) {
            try {
                ps = conn.prepareStatement("insert into rent (client_email,car_id,start_date,end_date,total_rent,driver_hire) values(?,?,?,?,?,?)");
                ps.setString(1, clnt.getEmail());
                ps.setString(2, selectedCar.getId());
                ps.setDate(3, java.sql.Date.valueOf(pick.getValue()));
                ps.setDate(4, java.sql.Date.valueOf(drop.getValue()));
                ps.setDouble(5, totalRent);
                if (checkBox.isSelected()) {
                    ps.setString(6, "YES");
                } else {
                    ps.setString(6, "NO");
                }
                ps.executeUpdate();

                //marking car as unavailable if rent is successful
                ps = conn.prepareStatement("Update cars set availability='NOT AVAILABLE' where id=? ");
                ps.setString(1, selectedCar.getId());
                ps.executeUpdate();

                infoBox("Successfully Rented", null, "Success");
                Node node = (Node) event.getSource();
                Stage dialogStage = (Stage) node.getScene().getWindow();
            } catch (SQLException x) {
                infoBox("Please fill in all information", null, "Failed");
            } catch (NullPointerException e) {
                infoBox("Please fill in all information", null, "Failed");
            }
        }
        else{
            infoBox("Please fill in all information", null, "Failed");
        }

    }

    @FXML
    public void goBackHome(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ClientHome.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        ClientHomeController controller = loader.getController();
        controller.initData(clnt);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setTitle("Home");
        window.setScene(scene);
        window.show();
    }

    public void goBackButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("allCarView.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        AllCarViewController controller = loader.getController();
        controller.initData(clnt);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setTitle("All Cars");
        window.setScene(scene);
        window.show();
    }

    public void refresh() {
        rent.setText("");
        total_rent.setText("");
        model.setText("");
        pick.setValue(null);
        drop.setValue(null);
    }
    
    public void changeDate(){
        drop.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = pick.getValue();
                setDisable(empty || date.compareTo(today) <= 0);
            }
        });
    }
}
