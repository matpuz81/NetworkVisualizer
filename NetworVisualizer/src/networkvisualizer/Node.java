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
    private Point p;
    private double angle,distance;
    String label;
    Color color=Color.red;
    LinkedList<Node>nodes = new LinkedList(); // nodes[0] is parent
    Point mousePos;
    int size;
    boolean doFollowMouse=false;
    
    public Node(Point p, int size, String label)
    {
        this.size = size;
        this.p=p;
        this.label=label;
        nodes.add(null);
    }
    
    public Node(Node parent, double angle, double distance, int size, String label)
    {
        p=new Point();
        this.size=size;
        this.angle=angle;
        this.distance=distance;
        //this.parent=parent;
        this.label=label;
        nodes.add(parent);
    }
    
    public Node(Node parent, int size, String label)
    {
        p=new Point();
        this.size=size;
        //this.parent=parent;
        this.label=label;
        nodes.add(parent);
        
    }
    /*
    public void update(double zoom)
    {
        //this.zoom=zoom;
       // this.size=size;
        /*
        if(nodes.getFirst()==null)  //is a parent 
        {
            
        }
        else
        {
            p.x = nodes.getFirst().p.x + (int)(Math.sin(Math.toRadians(angle))*distance * zoom);
            p.y = nodes.getFirst().p.y + (int)(Math.cos(Math.toRadians(angle))*distance * zoom);
        }

    }
    */
    public void setParams(String label)
    {
        this.label=label;
    }
    
    public void setPolar(double angle, double distance)
    {
        this.angle=angle;
        this.distance=distance;
    }
    
    
    public Point getPosition(Point centerNode, double zoom)
    {        
        if(!doFollowMouse)
        {
            p.x = centerNode.x + (int)(Math.sin(Math.toRadians(angle))*distance / zoom);
            p.y = centerNode.y + (int)(Math.cos(Math.toRadians(angle))*distance / zoom);
        }
       //else
            //doFollowMouse = false;
        return p;
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
    
    public int getSize()
    {
        return (int)(size);
    }
}
