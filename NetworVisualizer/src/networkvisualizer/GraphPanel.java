/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkvisualizer;

import com.sun.java.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
/**
 *
 * @author chef
 */
public class GraphPanel extends JPanel {
    
    LinkedList<Node> nodes = new LinkedList();
    Color nodeColor = Color.white;
    Color nodeBorderColor = Color.black;
    Color possibleColor = Color.lightGray;
    Color possibleHighlighted = Color.GREEN;
    
    Node selectedNode=null;
    Node snappedNode=null;
    Point mousePos;
    
    public GraphPanel()
    {
        setPreferredSize(new Dimension(800,600));
        setSize(800,600);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent event) {
                    checkForHover(event);
            }

            @Override
            public void mouseExited(MouseEvent event) {
                    checkForHover(event);
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                checkClick(e);
            }
        });

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseMoved(MouseEvent event) {
                checkForHover(event);
            }

            @Override
            public void mouseDragged(MouseEvent event) {
                checkForHover(event);
            }
        });
        addNode("Test");
    }
    
    public void addNode(String label)
    {
        nodes.add(new Node(new Point(getSize().width/2,getSize().height/2),getNodeSize(), label));
    }
    
    public void addNode(Node parent, double angle, double distance, String label)
    {
        nodes.add(new Node(parent,angle,distance,getNodeSize(), label));
    }
    
    public int getNodeSize()
    {
        return this.getWidth()/40;
    }
    
    public int getNodeDistance()
    {
        return getNodeSize()*5;
    }
    
    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.clearRect(0,0,this.getSize().width,this.getSize().height);
        
        if(selectedNode!=null)
        {
            g2.drawLine(selectedNode.x, selectedNode.y, mousePos.x, mousePos.y);
        }
        
        for (Node n:nodes)
        {            
            Point p = n.getPosition(getNodeDistance());
            
            if(!n.nodes.isEmpty())
            {
                for(Node subnode:n.nodes)
                g2.drawLine(subnode.x, subnode.y, p.x, p.y);
            }
            
            if(n.showPossibleNodes)
            {   
                //for(int angle:n.getPossibleNodes())
                //g2.drawLine(p.x, p.y, p.x+(int)(Math.sin(Math.toRadians(angle))*getNodeDistance()), p.y+(int)(Math.cos(Math.toRadians(angle))*getNodeDistance()));
            }

        }
        
        
        for (Node n:nodes)
        {      
            Point p = n.getPosition(getNodeDistance());
            if(n==selectedNode)
                g2.setColor(Color.green);
            else if(n==snappedNode)
            {
                if(areConnected(snappedNode,selectedNode))
                    g2.setColor(Color.gray);
                else
                    g2.setColor(Color.blue);
            }
            g2.fillOval(p.x-getNodeSize()-2,p.y-getNodeSize()-2,getNodeSize()*2+4, getNodeSize()*2+4);
            
            g2.setColor(n.color);
            g2.fillOval(p.x-getNodeSize(),p.y-getNodeSize(),getNodeSize()*2, getNodeSize()*2);
            g2.setColor(nodeBorderColor);
            
            g2.drawString(n.label, n.x-g.getFontMetrics().stringWidth(n.label)/2, n.y+3);
            
            /*
            if(n.possibleChild!=null)
            {
                g2.setColor(possibleHighlighted);
                p = n.possibleChild.getPosition(getNodeDistance());
                g2.fillOval(p.x-getNodeSize(),p.y-getNodeSize(),getNodeSize()*2, getNodeSize()*2);
                g2.setColor(nodeBorderColor);
            }*/
        }
    }
    
    public boolean areConnected(Node n1, Node n2)
    {
        if(n1 == null || n2 == null)
            return false;
        return n1.nodes.contains(n2) || n2.nodes.contains(n1);
    }
    
    public void checkForHover(MouseEvent event){

        mousePos = event.getPoint();
        snappedNode=null;
        for (Node n:nodes)
        {
            Point p = n.getPosition(getNodeDistance());
            if(event.getPoint().distance(p.x, p.y) < getNodeSize()){
                mousePos = new Point(p.x,p.y);
                snappedNode=n;
            }
            
            else if(n.showPossibleNodes && event.getPoint().distance(p.x, p.y) < getNodeDistance())
            {
                /* for(int angle:n.getPossibleNodes())
                 {
                     int x = p.x+(int)(Math.sin(Math.toRadians(angle))*getNodeDistance());
                     int y = p.y+(int)(Math.cos(Math.toRadians(angle))*getNodeDistance());
                     if(event.getPoint().distance(x, y) < getNodeSize())
                     {
                         n.setPossibleChild(angle);
                     }
                 }*/
            }
            
            else if(n.possibleChild!=null && event.getPoint().distance(n.possibleChild.x,n.possibleChild.y) < n.possibleChild.size)
            {
                
            }
            
            else
            {
                n.showPossibleNodes=false;
                n.removePossibleChild();
            }
                
        }
        repaint();
    }
    
    public void checkClick(MouseEvent event){
        
        if(SwingUtilities.isRightMouseButton(event))
        {
            boolean onNode=false;
            for(Node n:nodes){
                if(event.getPoint().distance(n.x,n.y)<n.size){
                    onNode=true;
                    selectedNode=n;

                    NodeMenu menu = new NodeMenu();
                    menu.show(this, selectedNode.x, selectedNode.y);
                    /*
                    JOptionPane.showConfirmDialog(this,"Delete Node " + selectedNode.label + "?",
                    "Are you sure?",
                    JOptionPane.YES_NO_OPTION);*/
                    //selectedNode=null;
                }
            }
            
            if(!onNode){    //If the right mouse button is clicked anywhere but on a node it unselects the selected Node
                selectedNode=null;
            }
        }
        
        else
        {

            if(selectedNode==null){
                for(Node n:nodes){
                    if(event.getPoint().distance(n.x,n.y)<n.size){
                        selectedNode=n;
                    }
                }
            }
            else if(snappedNode != null)
            {
                int n = JOptionPane.showConfirmDialog(this,"Do you want to connect " + selectedNode.label + " to " + snappedNode.label + "?",
                "Connect the nodes?",
                JOptionPane.YES_NO_OPTION);
                System.out.println(n);
                if(n==0)
                {
                    selectedNode.nodes.add(snappedNode);
                    snappedNode.nodes.add(selectedNode);
                    selectedNode=null;
                }
            }
            else
            {
                double angle = 180+Math.toDegrees(Math.atan2((selectedNode.x-mousePos.x),(selectedNode.y-mousePos.y)));
                double distance = Math.sqrt(Math.pow((selectedNode.x-mousePos.x), 2)+Math.pow((selectedNode.y-mousePos.y), 2));
                addNode(selectedNode,angle,distance,"test");
                selectedNode=null;
            }
        }
        
        /*
        
        Node hasPossibleChild=null;
        for (Node n:nodes)
        {
            if(n.possibleChild!=null){
                hasPossibleChild=n;
            }
        }
        if(hasPossibleChild!=null){
            nodes.addLast(hasPossibleChild.possibleChild);
            hasPossibleChild.removePossibleChild();
        }*/
    }
}
