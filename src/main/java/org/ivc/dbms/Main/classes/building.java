package org.ivc.dbms.Main.classes;

public class building {
    private Long capacity;
    private Long buildingCode;
    public Long getCapacity() {
        return capacity;
    }
    public void setCapacity(Long capacity) {
        this.capacity = capacity;
    }
    public Long getBuildingCode() {
        return buildingCode;
    }
    public void setBuildingCode(Long buildingCode) {
        this.buildingCode = buildingCode;
    }
    @Override
    public String toString() {
        return "building [capacity=" + capacity + ", buildingCode=" + buildingCode + "]";
    }

    
}
