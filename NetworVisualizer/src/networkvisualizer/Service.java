/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkvisualizer;

/**
 *
 * @author Jessica
 */
public class Service {

    private int COD;
    private float cost;
    private String service, description, permission;

    public Service(){
        
    }
    
    public Service(String service, String description, String permission, float cost){
        this();
        this.service = service;
        this.description = description;
        this.permission = permission;
        this.cost = cost;
    }
    
    public Service(int COD, String service, String description, String permission, float cost){
        this(service, description, permission, cost);
        this.COD = COD;
    }

    public int getCOD() {
        return COD;
    }

    public void setCOD(int COD) {
        this.COD = COD;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}


