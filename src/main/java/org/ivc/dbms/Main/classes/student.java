package org.ivc.dbms.Main.classes;

public class student {
    private Integer permNumber;
    private Integer pin;
    private String name;
    private String address;
    private String dept;

    public Integer getPin() {
        return pin;
    }
    public void setPin(Integer pin) {
        this.pin = pin;
    }

    public Integer getPermNumber() {
        return permNumber;
    }
    public void setPermNumber(Integer permNumber) {
        this.permNumber = permNumber;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "student [permNumber=" + permNumber + ", address=" + address + ", name=" + name 
                + ", getPermNumber()=" + getPermNumber() + ", getAddress()=" + getAddress() + ", getName()=" + getName()
                 + "]";
    }
    public void setdept(String dept) {
        this.dept = dept;
    }
    public String getdept() {
        return dept;
    }

        
    public student(Integer permNumber, Integer pin, String name, String address, String dept) {
        this.permNumber = permNumber;
        this.pin = pin;
        this.name = name;
        this.address = address;
        this.dept = dept;
    }

    
}
