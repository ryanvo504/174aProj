package org.ivc.dbms.Main.classes;

public class major {
    private Long deptId;
    private String name;

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("major{");
        sb.append("deptId=").append(deptId);
        sb.append(", name=").append(name);
        sb.append('}');
        return sb.toString();
    }
     
}
