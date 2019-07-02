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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

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
    
    
    Connection conn = null;
    
    ResultSet result = null;
    PreparedStatement ps = null;
    
    private Company comp;
    
    public CarViewController() throws SQLException{
        conn = ConnectionStart();
    }
    
    
    
    public void initialize(URL url, ResourceBundle rb) {
        
        col_id.setCellValueFactory(new PropertyValueFactory<Car,String>("id"));
        col_model.setCellValueFactory(new PropertyValueFactory<Car,String>("model"));
        col_avail.setCellValueFactory(new PropertyValueFactory<Car,String>("availability"));
        col_price.setCellValueFactory(new PropertyValueFactory<Car,Double>("price"));
        col_seats.setCellValueFactory(new PropertyValueFactory<Car,Integer>("seat"));
        
        
        table.setEditable(true);
        col_price.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        col_avail.setCellFactory(TextFieldTableCell.forTableColumn());
        col_seats.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        col_model.setCellFactory(TextFieldTableCell.forTableColumn());
        
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        
        
    }    
    
    public ObservableList<Car> getCars(){
        ObservableList<Car> car = FXCollections.observableArrayList();
        try{
            ps = conn.prepareStatement("Select id,model,no_of_seats,price,availability from cars where cname=?");
            ps.setString(1,comp.getName());
            result = ps.executeQuery();
            
            while(result.next()){
                car.add(new Car(result.getString("id"),result.getString("model"),result.getString("availability"),result.getInt("no_of_seats"),result.getDouble("price")));
            }
        }
        catch(SQLException ex){
            Logger.getLogger(CarViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return car;
    }
    
    public void editPrice(CellEditEvent edittedCell) throws SQLException{
        Car selectedCar = table.getSelectionModel().getSelectedItem();
        double carPrice = Double.parseDouble(edittedCell.getNewValue().toString());
        selectedCar.setPrice(carPrice);
        
        ps = conn.prepareStatement("UPDATE cars SET price=? WHERE id=?");
        ps.setDouble(1,carPrice);
        ps.setString(2,selectedCar.getId());
        ps.executeUpdate();
    }
    
    public void editSeats(CellEditEvent edittedCell) throws SQLException{
        Car selectedCar = table.getSelectionModel().getSelectedItem();
        int seats = Integer.parseInt(edittedCell.getNewValue().toString());
        selectedCar.setPrice(seats);
        
        ps = conn.prepareStatement("UPDATE cars SET price=? WHERE id=?");
        ps.setDouble(1,seats);
        ps.setString(2,selectedCar.getId());
        ps.executeUpdate();
    }
    
    public void editAvailability(CellEditEvent edittedCell) throws SQLException{
        Car selectedCar = table.getSelectionModel().getSelectedItem();
        selectedCar.setAvailability(edittedCell.getNewValue().toString());
        
        ps = conn.prepareStatement("UPDATE cars SET availability=? WHERE id=?");
        ps.setString(1,selectedCar.getAvailability());
        ps.setString(2,selectedCar.getId());
        ps.executeUpdate();
    }
    
    public void editModel(CellEditEvent edittedCell) throws SQLException{
        Car selectedCar = table.getSelectionModel().getSelectedItem();
        selectedCar.setAvailability(edittedCell.getNewValue().toString());
        
        ps = conn.prepareStatement("UPDATE cars SET availability=? WHERE id=?");
        ps.setString(1,selectedCar.getModel());
        ps.setString(2,selectedCar.getId());
        ps.executeUpdate();
    }
    
    public void deleteSelectedCars() throws SQLException{
        ObservableList<Car> selectedRows,allCars;
        allCars = table.getItems();
        selectedRows = table.getSelectionModel().getSelectedItems();
        
        for(Car car : selectedRows){
            allCars.remove(car);
            ps = conn.prepareStatement("DELETE from cars WHERE id=?");
            ps.setString(1,car.getId());
            ps.executeUpdate();
        }
    }
    
    public void createTable(Company comp){
        this.comp = comp;
        table.setItems(getCars());
    }
    
    
    
}
