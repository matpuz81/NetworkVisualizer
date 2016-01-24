/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package networkvisualizer;

/**
 *
 * @author matthiaskeim
 */
public class Service {
    
    private int code;
    private String description;
    private String service;
    private String permission;
    private float cost;
    
    public Service(String desc, String serv, String perm, float co) {
        this.description = desc;
        this.service = serv;
        this.permission = perm;
        this.cost = co;
    }
    
    public void setCode(int co) {
        this.code = co;
    }
    
    public String getDescription() {
        return this.description;
    } 
    
    public String getService() {
        return this.service;
    }
    
    public String getPermission() {
        return this.permission;
    }
    
    public float getCost() {
        return this.cost;
    }
    
    public int getCode() {
        return this.code;
    }
}
