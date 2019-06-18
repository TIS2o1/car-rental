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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author TIS_2o1
 */
public class CarViewController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TableView<Car> table;
    @FXML
    private TableColumn<Car,String> col_id;
    @FXML
    private TableColumn<Car,String> col_model;
    @FXML
    private TableColumn<Car,Integer> col_seats;
    @FXML
    private TableColumn<Car,Double> col_price;
    @FXML
    private TableColumn<Car,String> col_avail;
    
    
    private String eml;
    
    Connection conn = null;
    
    ResultSet result = null;
    PreparedStatement ps = null;
    
    public CarViewController(String eml) throws SQLException{
        this.eml = eml;
        conn = ConnectionStart();
    }
    
    ObservableList<Car> list = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try{
            ps = conn.prepareStatement("Select cname from company where email=?");
            ps.setString(1,eml);
            result = ps.executeQuery();
            result.next();
            String comp_name = result.getString("cname");
            
            ps = conn.prepareStatement("Select id,model,no_of_seats,price,availability from cars where cname=?");
            ps.setString(1,comp_name);
            result = ps.executeQuery();
            
            while(result.next()){
                //list.add(new Car(result.getString("id"),result.getString("model"),result.getInt("no_of_seats"),result.getDouble("price"),result.getBoolean("availability")));
            }
        }
        catch(SQLException ex){
            
        }
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_model.setCellValueFactory(new PropertyValueFactory<>("model"));
        col_avail.setCellValueFactory(new PropertyValueFactory<>("availab"));
        col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        col_seats.setCellValueFactory(new PropertyValueFactory<>("seat"));
    }    
    
    
    
    
}
