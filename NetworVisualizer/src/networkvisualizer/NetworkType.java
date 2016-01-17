/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkvisualizer;

/**
 *
 * @author matthiaskeim
 */
public class NetworkType {
    
    private String id_net_type;
    private String description;
    
    public NetworkType(String id, String desc) {
        this.id_net_type = id;
        this.description = desc;
    }
    
    public String getId() {
        return this.id_net_type;
    }
    
    public String getDescription() {
        return this.description;
    }    
    
}
