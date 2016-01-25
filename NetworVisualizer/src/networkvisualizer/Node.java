/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkvisualizer;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

/**
 *
 * @author chef
 */
public class Node {
    
    private int id;
    private Network network;
    private Polar pol;
    private String label;
    private final int size;
    private String status;
    
    public Node(Polar pol, int size, String label)
    {
        this.size=size;
        this.pol=pol;
        this.label=label;
    }
    
    public void setId(int id)
    {
        this.id=id;
    }
    
    public LinkedList<Node> getConnectedNodes()
    {
        return NetworkVisualizer.panel.getConnectedNodes(this);
    }
    
    public void setNetwork(Network net)
    {
        this.network=net;
        if(NetworkVisualizer.DB != null)
            NetworkVisualizer.DB.updateNode(this);
        
    }
    
    public void setParams(String label)
    {
        this.label=label;
        NetworkVisualizer.DB.updateNode(this);
    }
    
    public void setPolar(Polar pol)
    {
        this.pol=pol;
        NetworkVisualizer.DB.updateNode(this);
    }
    
    public int getId()
    {
        return id;
    }
    
    public Network getNetwork()
    {
        return network;
    }
    
    public Point getPosition(Point centerNode, double zoom)
    {    
        return pol.getPoint(centerNode,zoom);
    }  
    
    public String getLabel() {
        return label;
    }
    
    public int getSize() {
        return size;
    }
    
    public double getAngle() {
        return this.pol.getAngle();
    }
    
    public double getDistance() {
        return this.pol.getDistance();
    }
    
    public int getNetworkId() {
        return this.network.getId();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
