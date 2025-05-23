package org.ivc.dbms.Main.classes;

public class student {
    private String permNumber;
    private String address;
    private String name;
    private Long id;
    public String getPermNumber() {
        return permNumber;
    }
    public void setPermNumber(String permNumber) {
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

    
}
