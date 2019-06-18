/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

/**
 *
 * @author TIS_2o1
 */
public class Car {
    String id,model,availab;
    int seat;
    double price;
    
    public Car(String id, String model, String availab, int seat, double price){
        this.id = id;
        this.model = model;
        this.availab = availab;
        this.seat = seat;
        this.price = price;
        
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAvailab() {
        return availab;
    }

    public void setAvailab(String availab) {
        this.availab = availab;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    
    
}
