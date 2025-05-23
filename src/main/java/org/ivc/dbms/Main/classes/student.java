package org.ivc.dbms.Main.classes;

public class student {
    private Long permNumber;
    private Long id;
    private Long pin;
    private String name;
    public Long getPin() {
        return pin;
    }
    public void setPin(Long pin) {
        this.pin = pin;
    }
    private String address;
    private String dept;

    public Long getPermNumber() {
        return permNumber;
    }
    public void setPermNumber(Long permNumber) {
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
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return "student [permNumber=" + permNumber + ", address=" + address + ", name=" + name + ", id=" + id
                + ", getPermNumber()=" + getPermNumber() + ", getAddress()=" + getAddress() + ", getName()=" + getName()
                + ", getId()=" + getId() + "]";
    }
    public void setdept(String dept) {
        this.dept = dept;
    }
    public String getdept() {
        return dept;
    }

        
    public student(Long permNumber, Long id, Long pin, String name, String address, String dept) {
        this.permNumber = permNumber;
        this.id = id;
        this.pin = pin;
        this.name = name;
        this.address = address;
        this.dept = dept;
    }

    
}
