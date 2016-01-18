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
    private Point p;
    private double angle,distance;
    private String label;
    Color color=Color.red;
    LinkedList<Node>nodes = new LinkedList();
    private final int size;
    private boolean doFollowMouse=false;
    private double prevAngle=0, prevDistance=0;
    
    public Node(double angle, double distance, int size, String label)
    {
        p=new Point();
        this.size=size;
        this.angle=angle;
        this.distance=distance;
        this.label=label;
        
        p.x = (int)(Math.sin(Math.toRadians(angle))*distance);
        p.y = (int)(Math.cos(Math.toRadians(angle))*distance);
    }
    
    public void setId(int id)
    {
        this.id=id;
    }
    
    public void setNetwork(Network net)
    {
        this.network=net;
    }
    
    public void setParams(String label)
    {
        this.label=label;
        NetworkVisualizer.DB.updateNode(this);
    }
    
    public void setPolar(double angle, double distance)
    {
        this.angle=angle;
        this.distance=distance;
        NetworkVisualizer.DB.updateNode(this);
        
        
        p.x = (int)(Math.sin(Math.toRadians(angle))*distance);
        p.y = (int)(Math.cos(Math.toRadians(angle))*distance);
    }
    
    public void followMouse(MouseEvent e)
    {
        doFollowMouse = true;
        p=e.getPoint();
    }
    
    public void stopFollowMouse()
    {
        doFollowMouse=false;
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
        
        if(!doFollowMouse)
        {
            //p.x = centerNode.x + (int)(Math.sin(Math.toRadians(angle))*distance / zoom);
           // p.y = centerNode.y + (int)(Math.cos(Math.toRadians(angle))*distance / zoom);
        }
        return new Point((int)(centerNode.x + p.x/zoom), (int)(centerNode.y + p.y / zoom));
    }  
    
    public int getX(Point centerNode, double zoom)
    {
        return (int)(centerNode.x + p.x/zoom);
    }
        
    public int getY(Point centerNode, double zoom)
    {
        return (int)(centerNode.y + p.y/zoom);
    }
    
    public String getLabel() {
        return label;
    }
    
    public int getSize() {
        return size;
    }
    
    public double getAngle() {
        return this.angle;
    }
    
    public double getDistance() {
        return this.distance;
    }
}
