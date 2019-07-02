/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class Car {
    private SimpleStringProperty id,model,availability;
    private SimpleIntegerProperty seat;
    private SimpleDoubleProperty price;
    
    public Car(String id, String model, String availab, int seat, double price){
        this.id = new SimpleStringProperty(id);
        this.model = new SimpleStringProperty(model);
        this.availability = new SimpleStringProperty(availab);
        this.seat = new SimpleIntegerProperty(seat);
        this.price = new SimpleDoubleProperty(price);
        
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

    
    
    
    
}
