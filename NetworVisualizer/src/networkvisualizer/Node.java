/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkvisualizer;

import java.awt.Color;
import java.awt.Point;
import java.util.LinkedList;

/**
 *
 * @author chef
 */
public class Node {
    int x, y;
    double angle,distance;
    double minAngle=0, maxAngle=360;
    String label;
    int size;
    Color color=Color.red;
    LinkedList<Node>nodes = new LinkedList(); // nodes[0] is parent
    
    public Node(Point p, int size, String label)
    {
        this.x=p.x;
        this.y=p.y;
        this.label=label;
        nodes.add(null);
        update(size);
    }
    
    public Node(Node parent, double angle, double distance, int size, String label)
    {
        this.angle=angle;
        this.distance=distance;
        this.minAngle = angle -120;
        this.maxAngle = angle +120;
        //this.parent=parent;
        this.label=label;
        nodes.add(parent);
        update(size);
    }
    
    public void update(int size)
    {
        this.size=size;
        
        if(nodes.getFirst()==null)  //is a parent 
        {
            
        }
        else
        {
            x = nodes.getFirst().x + (int)(Math.sin(Math.toRadians(angle))*distance);
            y = nodes.getFirst().y + (int)(Math.cos(Math.toRadians(angle))*distance);
        }

    }
    
    public void setParams(String label)
    {
        this.label=label;
    }
    
    public int getNodeDistance()
    {
        return size * 5;
    }
    
    public Point getPosition(int nodeDistance)
    {
        return new Point(x,y);
    }  
}
