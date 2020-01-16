/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;
//import javafx.scene.image.Image;



public class Car {
    private SimpleStringProperty id,model,availability,compName,location;
    private SimpleIntegerProperty seat;
    private SimpleDoubleProperty price;
    private ImageView image;
    
    public Car(){
    }
    
    public Car(String id, String model, String availability, int seat, double price, ImageView image){
        this.id = new SimpleStringProperty(id);
        this.model = new SimpleStringProperty(model);
        this.availability = new SimpleStringProperty(availability);
        this.seat = new SimpleIntegerProperty(seat);
        this.price = new SimpleDoubleProperty(price);
        this.image = image;
        
    }
    
    public Car(String id, String model, String availability, int seat, double price, String compName,String location,ImageView image){
        this.id = new SimpleStringProperty(id);
        this.model = new SimpleStringProperty(model);
        this.availability = new SimpleStringProperty(availability);
        this.seat = new SimpleIntegerProperty(seat);
        this.price = new SimpleDoubleProperty(price);
        this.compName = new SimpleStringProperty(compName);
        this.location = new SimpleStringProperty(location);
        this.image = image;
        
    }
    
    

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id = new SimpleStringProperty(id);
    }

    public String getModel() {
        return model.get();
    }

    public void setModel(String model) {
        this.model = new SimpleStringProperty(model);
    }

    public String getAvailability() {
        return availability.get();
    }

    public void setAvailability(String availab) {
        this.availability = new SimpleStringProperty(availab);
    }

    public int getSeat() {
        return seat.get();
    }

    public void setSeat(int seat) {
        this.seat = new SimpleIntegerProperty(seat);
    }

    public double getPrice() {
        return price.get();
    }

    public void setPrice(double price) {
        this.price = new SimpleDoubleProperty(price);
    }

    public void setCompName(String compName) {
        this.compName = new SimpleStringProperty(compName);
    }

    public String getCompName() {
        return compName.get();
    }
    
    public String getLocation() {
        return location.get();
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }
    
}
