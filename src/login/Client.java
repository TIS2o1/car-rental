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
public class Client {
    private String name,email,address,contact;
    private int license_no;

    public Client(String name, String email, String address, int license_no, String contact) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.license_no = license_no;
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getLicense_no() {
        return license_no;
    }

    public void setLicense_no(int license_no) {
        this.license_no = license_no;
    }


    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
    
}
