/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package networkvisualizer;

import java.util.LinkedList;

/**
 *
 * @author chef
 */
public class Network {
    
    private int id;
    private String name, description;
    private int net_type_id=-1, net_com_protocol=-1;
    private String net_topology="";
    
    
    public Network(){
        name="Unnamed Network";
        description="";
    }
    
    public void setId(int id)
    {
         this.id=id;       
    }
    
    public void setParams(String name,String description, int net_type_id, String net_topology, int net_com_protocol)
    {
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
    
    public LinkedList<Node> getNodes()
    {
        LinkedList<Node> nodes = new LinkedList();
        for(Node n:NetworkVisualizer.panel.nodes){
            if(n.getNetwork() == this)
                nodes.add(n);
        }
        return nodes;
    }
    
    
}
