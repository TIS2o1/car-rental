/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

import static dbConn.dbConnection.ConnectionStart;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import static login.SignUpCompanyController.infoBox;

/**
 * FXML Controller class
 *
 * @author TIS_2o1
 */
public class AddCarController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            conn = ConnectionStart();
        } catch (SQLException ex) {
            Logger.getLogger(AddCarController.class.getName()).log(Level.SEVERE, null, ex);
        }

        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All files", "*.*"),
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("Text File", "*.txt")
        );
    }

    @FXML
    private TextField quantity;
    @FXML
    private TextField model;
    @FXML
    private TextField no_of_seats;
    @FXML
    private TextField price;
    private FileChooser fileChooser;
    private File file;
    @FXML
    private ImageView imageView;
    private Image image;
    private final Desktop desktop = Desktop.getDesktop();
    private FileInputStream fis;
    Stage dialogStage = new Stage();
    Scene scene;

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet result = null;

    private Company comp;

    public void AddCarAction(ActionEvent event) throws SQLException {
        if (model.getText() == null || price == null || no_of_seats == null || quantity == null || imageView.getImage()==null) {
            infoBox("Please fill in all fields", null, "Failed");
        } else {

            try {
                int qty = Integer.parseInt(quantity.getText());
                String car_model = model.getText();
                double carPrice = Double.parseDouble(price.getText());
                int seats = Integer.parseInt(no_of_seats.getText());
                String comp_name = comp.getName();
                if (qty > 0 && carPrice > 0) {
                    for (int i = 1; i <= qty; i++) {
                        String sql1 = "INSERT INTO cars(image,cname,model,price,no_of_seats,availability) values (?,?,?,?,?,'AVAILABLE')";

                        ps = conn.prepareStatement(sql1);
                        fis = new FileInputStream(file);
                        ps.setBinaryStream(1, fis, file.length());
                        ps.setString(2, comp_name);
                        ps.setString(3, car_model);
                        ps.setDouble(4, carPrice);
                        ps.setInt(5, seats);
                        ps.executeUpdate();
                    }
                    infoBox("Successfully Added", null, "Success");
                    refresh();
                }
                else{
                    infoBox("Please enter valid fields", null, "Failed");
                }
            } catch (FileNotFoundException | SQLException e ) {
                infoBox("Error: Please try Again", null, "Failed");
            } catch (NumberFormatException e){
                infoBox("Please enter valid fields", null, "Failed");
            }
        }
    }

    @FXML
    public void goBackHome(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("CompanyHome.fxml"));
        Parent parent = loader.load();

        scene = new Scene(parent);

        CompanyHomeController controller = loader.getController();
        controller.initData(comp);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setTitle("Home");
        window.setScene(scene);
        window.show();
    }

    @FXML
    public void browseImage(ActionEvent event) throws IOException {
        file = fileChooser.showOpenDialog(null);
        if (file != null) {
            System.out.println("" + file.getAbsolutePath());
            image = new Image(file.getAbsoluteFile().toURI().toString(), imageView.getFitWidth(), imageView.getFitHeight(), true, true);
            imageView.setImage(image);
            imageView.setPreserveRatio(true);
        }

    }

    private void refresh() {
        quantity.clear();
        model.clear();
        price.clear();
        no_of_seats.clear();
        imageView.setImage(null);
    }

    public void companyInformation(Company comp) {
        this.comp = comp;
    }
}
