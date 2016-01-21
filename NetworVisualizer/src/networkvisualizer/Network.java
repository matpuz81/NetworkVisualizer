/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package networkvisualizer;

import java.awt.Color;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author chef´
 */
public class Network {
    
    private int id;
    private String name, description;
    private int net_com_protocol=1;
    private String net_topology="star",net_type_id="LAN";
    private Color color;
    
    
    
    public Network(){
        name="Unnamed Network";
        description="";
        Random random = new Random();
        final float hue = random.nextFloat();
        final float saturation = (random.nextInt(2000) + 5000) / 10000f;
        final float luminance = 0.9f;
        color = Color.getHSBColor(hue, saturation, luminance);
    }
    
    public void setId(int id)
    {
         this.id=id;       
    }
    
    public void setParams(String name,String description, String net_type_id, String net_topology, int net_com_protocol)
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
    
    public String getNet_type_id() {
        return net_type_id;
    }
    
    public int getNet_com_protocol() {
        return net_com_protocol;
    }
    
    public String getNet_topology() {
        return net_topology;
    }
    
    public Color getColor()
    {
        return color;
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
