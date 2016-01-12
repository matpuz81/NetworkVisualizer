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
import javax.swing.JFrame;
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
    private Node centerNode;
    
    Node selectedNode=null;
    Node snappedNode=null;
    Node movingNode=null;
    Point mousePos;
    
    int counter = 0;
    String noNodeMessage ="Right click -> 'Add Node' to add Nodes";
    
    public GraphPanel()
    {
        setPreferredSize(new Dimension(800,600));
        setSize(800,600);
        
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseMoved(MouseEvent event) {
                checkForHover(event);
            }

            @Override
            public void mouseDragged(MouseEvent event) {
                //checkDragging(event);
            }
        });
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                checkClick(e);
            }
        });

        centerNode = new Node(new Point(getSize().width/2,getSize().height/2),getNodeSize(), null); //this is used as reference
    }
    
    public void addParent(Point p)
    {
        double angle = 180+Math.toDegrees(Math.atan2((centerNode.getPosition().x-p.x),(centerNode.getPosition().y-p.y)));
        double distance = Math.sqrt(Math.pow((centerNode.getPosition().x-p.x), 2)+Math.pow((centerNode.getPosition().y-p.y), 2));   
        addNode(centerNode,angle,distance);
    }
    
    public void addNode(Node parent, double angle, double distance)
    {
        Node tmpNode = new Node(parent,angle,distance,getNodeSize(), "Node " + (nodes.size()+1));
        NodeCreateFrame createPanel = new NodeCreateFrame(this,tmpNode);
        createPanel.setVisible(true);
    }
    
    public void addNodeToList(Node n, String label)
    {
        n.setParams(label);
        nodes.add(n);
        n.nodes.getFirst().nodes.add(n);
    }
    
    public void deleteNode(Node n)
    {
        for(Node nx:n.nodes){
            nx.nodes.remove(n);
        }
        nodes.remove(n);
    }
    
    public void moveNode(Node n, MouseEvent event)
    {
        selectedNode = n;
        double angle = 180+Math.toDegrees(Math.atan2((selectedNode.getPosition().x-mousePos.x),(selectedNode.getPosition().y-mousePos.y)));
        double distance = Math.sqrt(Math.pow((selectedNode.getPosition().x-mousePos.x), 2)+Math.pow((selectedNode.getPosition().y-mousePos.y), 2));
        selectedNode.setPolar(angle, distance);
        selectedNode.update(getNodeSize());
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
        
        if(nodes.isEmpty()){
            g2.drawString(noNodeMessage,this.getSize().width/2-g.getFontMetrics().stringWidth(noNodeMessage)/2, getSize().height/2+3);
        }
        
        if(selectedNode!=null){
            g2.drawLine(selectedNode.getPosition().x, selectedNode.getPosition().y, mousePos.x, mousePos.y);
        }
        
        for (Node n:nodes) {     
            Point p = n.getPosition();
                

            if(!n.nodes.isEmpty()) {
                for(Node subnode:n.nodes) {
                    if(subnode!=(getCenterNode())) {  
                        g2.drawLine(subnode.getPosition().x, subnode.getPosition().y, p.x, p.y);
                    }
                }
            }
        }
        
        
        for (Node n:nodes)
        {      
            if(!n.equals(getCenterNode()))
            {
                Point p = n.getPosition();
                if(n==selectedNode)
                    g2.setColor(Color.green);
                else if(n==snappedNode) {
                    if(areConnected(snappedNode,selectedNode))
                        g2.setColor(Color.gray);
                    else
                        g2.setColor(Color.blue);
                }
                g2.fillOval(p.x-getNodeSize()-2,p.y-getNodeSize()-2,getNodeSize()*2+4, getNodeSize()*2+4);
                g2.setColor(n.color);
                g2.fillOval(p.x-getNodeSize(),p.y-getNodeSize(),getNodeSize()*2, getNodeSize()*2);
                g2.setColor(nodeBorderColor);

                g2.drawString(n.label, n.getPosition().x-g.getFontMetrics().stringWidth(n.label)/2, n.getPosition().y+3);     
            }
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
            Point p = n.getPosition();
            if(event.getPoint().distance(p) < getNodeSize()){
                mousePos = p;
                snappedNode=n;
            }        
            if(movingNode != null && n == movingNode)
            {
                n.followMouse(event);
            }        
        }
        repaint();
    }
    
    public void checkClick(MouseEvent event){
        
        if(SwingUtilities.isRightMouseButton(event))
        {
            Node n = isMouseOnNode(event);
            if(n==null){    //If the right mouse button is clicked anywhere but on a node it unselects the selected Node
                if(selectedNode==null)
                {
                    PanelMenu menu = new PanelMenu(this,event,selectedNode);
                    menu.show(this, event.getPoint().x, event.getPoint().y);
                }
                selectedNode=null;
            }
            else{       //If the right mouse button is clicked on a node, the context menu opens
                selectedNode=n;

                NodeMenu menu = new NodeMenu(this,event,selectedNode);
                menu.show(this, selectedNode.getPosition().x, selectedNode.getPosition().y);

                selectedNode=null;
            }
        }
        
        else
        {
            if(movingNode != null){
                movingNode=null;
            }
            else if(selectedNode==null){
                for(Node n:nodes){
                    if(event.getPoint().distance(n.getPosition())<n.size){
                        selectedNode=n;
                    }
                }
            }    
            else if(snappedNode == null)
            {
                double angle = 180+Math.toDegrees(Math.atan2((selectedNode.getPosition().x-mousePos.x),(selectedNode.getPosition().y-mousePos.y)));
                double distance = Math.sqrt(Math.pow((selectedNode.getPosition().x-mousePos.x), 2)+Math.pow((selectedNode.getPosition().y-mousePos.y), 2));
                addNode(selectedNode,angle,distance);
                selectedNode=null;
            }        
            else if(snappedNode != selectedNode && !areConnected(snappedNode,selectedNode))
            {
                int n = JOptionPane.showConfirmDialog(this,
                "Do you want to connect " + selectedNode.label + " to " + snappedNode.label + "?",
                "Connect the nodes?",JOptionPane.YES_NO_OPTION);
                if(n==0)
                {
                    selectedNode.nodes.add(snappedNode);
                    snappedNode.nodes.add(selectedNode);
                    selectedNode=null;
                }
            }
        }
        repaint();
    }
    
    public Node getCenterNode()
    {
        return centerNode;
    }
    
    public Node isMouseOnNode(MouseEvent event) //returns a node if the mousepointer is over it, otherwise null
    {
        for(Node n:nodes){
            if(event.getPoint().distance(n.getPosition())<n.size){
                return n;
            }
        }
        return null;
    }
}
