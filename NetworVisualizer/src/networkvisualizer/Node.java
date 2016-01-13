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
    private Point p;
    private double angle,distance;
    private String label;
    Color color=Color.red;
    LinkedList<Node>nodes = new LinkedList();
    private final int size;
    private boolean doFollowMouse=false;
    
    public Node(double angle, double distance, int size, String label)
    {
        p=new Point();
        this.size=size;
        this.angle=angle;
        this.distance=distance;
        this.label=label;
    }
    
    public Node(int size, String label)
    {
        p=new Point();
        this.size=size;
        this.label=label;
    }
    
    public void setId(int id)
    {
        this.id=id;
    }
    
    public void setParams(String label)
    {
        this.label=label;
    }
    
    public void setPolar(double angle, double distance)
    {
        this.angle=angle;
        this.distance=distance;
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
    
    public Point getPosition(Point centerNode, double zoom)
    {        
        if(!doFollowMouse)
        {
            p.x = centerNode.x + (int)(Math.sin(Math.toRadians(angle))*distance / zoom);
            p.y = centerNode.y + (int)(Math.cos(Math.toRadians(angle))*distance / zoom);
        }
        return p;
    }  
    
    public String getLabel() {
        return label;
    }
    
    public int getSize() {
        return size;
    }
}
