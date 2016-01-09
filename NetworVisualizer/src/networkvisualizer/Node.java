/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkvisualizer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
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
   // Node parent;
    boolean showPossibleNodes=false;
    Node possibleChild;
    int size;
    Color color=Color.red;
    LinkedList<Node>nodes = new LinkedList(); // nodes[0] is parent
    
    public Node(Point p, int size, String label)
    {
        this.x=p.x;
        this.y=p.y;
        this.label=label;
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
        
        if(nodes.isEmpty())
        {
            
        }
        else
        {
            x = nodes.getFirst().x + (int)(Math.sin(Math.toRadians(angle))*distance);
            y = nodes.getFirst().y + (int)(Math.cos(Math.toRadians(angle))*distance);
        }

    }
    
    public int getNodeDistance()
    {
        return size * 5;
    }
    
    public Point getPosition(int nodeDistance)
    {
        return new Point(x,y);
    }
    /*
    public LinkedList<Integer> getPossibleNodes()
    {
        LinkedList<Integer> l = new LinkedList();
        for(int a = minAngle; a <= maxAngle; a += 60)
        {
            l.add(a);
        }
        return l;
    }*/
    
    public void removePossibleChild()
    {
        possibleChild=null;
    }
    
    public void setPossibleChild(int angle)
    {
        //possibleChild = new Node(this,angle,size,"");
    }
    
    
}
