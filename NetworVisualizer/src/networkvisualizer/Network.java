/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package networkvisualizer;

/**
 *
 * @author chef
 */
public class Network {
    
    private final int id;
    private String name, description;
    private final int net_type_id, net_com_protocol;
    private final String net_topology;
    
    public Network(int id, String name, String description, int net_type_id, String net_topology, int net_com_protocol){
        this.id=id;
        this.name=name;
        this.description=description;
        this.net_type_id=net_type_id;
        this.net_topology=net_topology;
        this.net_com_protocol=net_com_protocol;
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getNet_type_id() {
        return net_type_id;
    }
    
    public int getNet_com_protocol() {
        return net_com_protocol;
    }
    
    public String getNet_topology() {
        return net_topology;
    }
    
    
}
