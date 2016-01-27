/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package networkvisualizer;

import java.awt.Color;
import java.awt.Point;
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
    
    public void setParams(String name,String description, String net_type_id, int net_com_protocol)
    {
        this.name=name;
        this.description=description;
        this.net_type_id=net_type_id;
        this.net_com_protocol=net_com_protocol;
    }
    
    public void setColor(Color c) {
        this.color = c;
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
        
        switch(getNetTopology())
        {
            case 1: return "Hybrid";
            case 2: return "Line";
            case 3: return "Ring";
            case 4: return "Star";
            case 5: return "Mesh";
            case 6: return "Fully connected mesh";
            case 7: return "Tree";
        }
        return net_topology;
    }
    
    private int getNetTopology() {   // 1 - Hybrid , 2 - Line, 3 - Ring, 4 - Star, 5 - Mesh, 6 - Fully connected mesh, 7 - Tree
        
        
        if(getNodes().size() == (getNodeLinks().size() + 1))
        {            
            boolean isLine=true;
            for(Node n:getNodes())
            {
                if(n.getConnectedNodes().size() > 2)
                    isLine = false;
                
                
                if(n.getConnectedNodes().size() == getNodeLinks().size() && getNodeLinks().size() > 2)   // Star
                    return 4;
                
                if(n.getConnectedNodes().size() == 3)
                    return 7;
            }
            if(isLine) // Line
                return 2;  
        }
        
        if(getNodes().size() == getNodeLinks().size())
        {
            for(Node n:getNodes())
            {
                if(n.getConnectedNodes().size() != 2)       // Mesh
                    return 5;
            }
            return 3;       // Ring
        }
        
        if(getNodeLinks().size() == ((getNodes().size() -1) * getNodes().size() /2))
            return 6;       // Fully connected mesh
        
        if(getNodes().size() < getNodeLinks().size())
        {
            return 5;
        }
        
        return 1;
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
    
        public LinkedList<NodeConnection> getNodeLinks()
    {
        LinkedList<NodeConnection> nodeLink = new LinkedList();
        for(NodeConnection n:NetworkVisualizer.panel.nodeConnection){
            if(n.n1.getNetwork() == this)
                nodeLink.add(n);
        }
        return nodeLink;
    }
        
    public Point getMiddle()
    {
        Point p=new Point(0,0);
        int cnt=0;
        for(Node n:getNodes())
        {
            Point np = NetworkVisualizer.panel.getNodePosition(n);
            p.x += np.x;
            p.y += np.y;
            cnt++;
        }
        p.x/=cnt;
        p.y/=cnt;
        return p;
    }
    
    
}
