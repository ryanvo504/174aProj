package org.ivc.dbms.Main.classes;

public class building {
    private String buildingCode;

    public building(){
    }

    public building(String buildingCode)
    {
        this.buildingCode = buildingCode;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

}
