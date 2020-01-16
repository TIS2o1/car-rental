package login;

import static dbConn.dbConnection.ConnectionStart;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;






public class AllCarViewController implements Initializable {

    ObservableList<Car> data = FXCollections.observableArrayList();
    FilteredList <Car> filteredData = new FilteredList<> (data,e->true);
    
    @FXML TextField search;
    @FXML private TableView<Car> table;
    @FXML private TableColumn<Car,String> col_model;
    @FXML private TableColumn<Car,Integer> col_seats;
    @FXML private TableColumn<Car,Double> col_rent;
    @FXML private TableColumn<Car,String> col_avail;
    @FXML private TableColumn<Car,String> col_location;
    @FXML private TableColumn<Car,String> col_name;
    @FXML private TableColumn<Car,ImageView> col_image;
    @FXML Button rentButton;
    
    private Client clnt;
    
    
    Connection conn = null;
    
    ResultSet result = null;
    PreparedStatement ps = null;
    
    
    public void initialize(URL url, ResourceBundle rb) {
        try {
            conn = ConnectionStart();
        } catch (SQLException ex) {
            Logger.getLogger(AllCarViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        col_model.setCellValueFactory(new PropertyValueFactory<>("model"));
        col_avail.setCellValueFactory(new PropertyValueFactory<>("availability"));
        col_rent.setCellValueFactory(new PropertyValueFactory<>("price"));
        col_seats.setCellValueFactory(new PropertyValueFactory<>("seat"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("compName"));
        col_location.setCellValueFactory(new PropertyValueFactory<>("location"));
        col_image.setPrefWidth(80);
        col_image.setCellValueFactory(new PropertyValueFactory<>("image"));
        
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        
        getCars();
        table.setItems(data);
    }   
    
    
    public void getCars(){
        try{
            
            ps = conn.prepareStatement("Select id,model,no_of_seats,price,availability,company.cname,location,image from cars,company where cars.cname=company.cname and availability='AVAILABLE'");
            result = ps.executeQuery();
            
            while(result.next()){
                Blob blob = result.getBlob("image");
                byte[] dataa = blob.getBytes(1, (int) blob.length());
                Image image1 = new Image(new ByteArrayInputStream(dataa));
                ImageView imageview = new ImageView();
                imageview.setFitHeight(100);
                imageview.setFitWidth(100);
                imageview.setImage(image1);
                data.add(new Car(result.getString("id"),result.getString("model"),result.getString("availability"),result.getInt("no_of_seats"),result.getDouble("price"),result.getString("cname"),result.getString("location"),imageview));
            }
        }
        catch(SQLException ex){
            Logger.getLogger(AllCarViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void searchCar(){
        search.textProperty().addListener((ObservableValue,oldValue,newValue)->{
            filteredData.setPredicate((Predicate<? super Car>)car->{
                if(newValue==null || newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if(car.getModel().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                String convDouble = String.valueOf(car.getPrice());
                if(convDouble.contains(newValue)){
                    return true;
                }
                String convInt = String.valueOf(car.getSeat());
                if(convInt.contains(newValue)){
                    return true;
                }
                else if(car.getLocation().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return false;
            });
        });
        SortedList<Car> sortedData = new SortedList<> (filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }
    
    public void rentCarButton(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("RentCar.fxml"));
        Parent parent = loader.load();
        
        Scene scene = new Scene(parent);
        RentCarController controller = loader.getController();
        controller.initData(clnt,table.getSelectionModel().getSelectedItem());
       
        
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        
        window.setTitle("View all Cars");
        window.setScene(scene);
        window.show();
    }

    void initData(Client clnt) {
        this.clnt = clnt;
        /*try {
            checkUserRent();
        } catch (SQLException ex) {
            Logger.getLogger(AllCarViewController.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
    
    @FXML
    public void goBackHome(ActionEvent event) throws IOException, SQLException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ClientHome.fxml"));
        Parent parent = loader.load();
        
        Scene scene = new Scene(parent);
        
        ClientHomeController controller = loader.getController();
        controller.initData(clnt);
        
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        
        window.setTitle("Home");
        window.setScene(scene);
        window.show();
    }
    
    /*public void checkUserRent() throws SQLException{
        ps = conn.prepareStatement("Select * from rent where client_email=? and is_done='NO'");
        ps.setString(1, clnt.getEmail());
        result = ps.executeQuery();
        if(result.next()){
            rentButton.setDisable(true);
        }
        else{
            rentButton.setDisable(false);
        }
    }*/
}
