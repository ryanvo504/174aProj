package org.ivc.dbms.Main.classes;

public class major {
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "major [name=" + name + "]";
    }

    public major(String name) {
        this.name = name;
    }
     
}
