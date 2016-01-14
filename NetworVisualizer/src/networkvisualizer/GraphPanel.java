/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkvisualizer;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
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
    private final Point centerNode;
    Point mouseCenterDifference;
    
    Node selectedNode=null;
    Node snappedNode=null;
    Node movingNode=null;
    Point anchor;
    Point mousePos = new Point();
    Rectangle selection=null;
    
    String noNodeMessage ="Right click -> 'Add Node' to add Nodes";
    double zoom=1.00, maxZoom=10.0, minZoom=0.2;
    
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
                checkForHover(event);
            }
        });
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                checkClick(e);
            }
        });
        
        addMouseWheelListener( new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if((zoom > minZoom && e.getUnitsToScroll() < 0) || (zoom < maxZoom && e.getUnitsToScroll() > 0)) {
                    zoom+=(((double)e.getUnitsToScroll())/100);
                }
            }
        });

        centerNode = new Point(getSize().width/2,getSize().height/2);
        
    }
    
    public Point getCenterNode() {
        return centerNode;
    }
    
    public void createNode(Node connectedNode, Point p)     //Used to create a new node (opens the NodeCreateFrame)
    {
        double angle = getAngle(centerNode,p);
        double distance = getDistance(centerNode,p);   
        
        Network net = null;
        
        if(connectedNode != null)
        {
            
        }
        
        Node tmpNode = new Node(null,angle,distance,getNodeSize(), "192.168.1." + (nodes.size()+1));
        if(connectedNode != null) {
            tmpNode.nodes.add(connectedNode);       //If the node is connected to another node, add the other node to the list
        }
        NodeParameters createPanel = new NodeParameters(tmpNode);
        createPanel.setVisible(true);
    }
    
    
    public void addNodeToDb(Node n) { //final add node function, adds it to the database and list
        
        int id = NetworkVisualizer.DB.addNode(n);

        if(id!=-1)
        {          
            n.setId(id);
            if(!n.nodes.isEmpty())      //check if node is connected to another node, if so then add the connection
            {
                Node connectedNode = n.nodes.getFirst();
                connectedNode.nodes.add(n);
                NetworkVisualizer.DB.addNodeConnection(n, connectedNode);
            }
            nodes.add(n);
        }
        else
        {
            databaseError(0);
        }
    }
    
    public void addNodeFromDB(Network net, int id, double angle, double distance, String label) {
        Node tmpNode = new Node(net,angle,distance,getNodeSize(),label);
        tmpNode.setId(id);
        nodes.add(tmpNode);
    }
    
    public void connectNodesById(int id1, int id2)
    {
        Node n1=getNodeById(id1), n2=getNodeById(id2);
        if(n1 != null && n2 != null)
        {
            n1.nodes.add(n2);
            n2.nodes.add(n1);
        }
    }
    
    public Node getNodeById(int id)
    {
        for(Node n:nodes)
        {
            if(n.getId() == id)
                return n;
        }
        return null;
    }
    
    
    
    public void deleteNode(Node n)
    {
        if(NetworkVisualizer.DB.deleteNode(n))
        {
            for(Node nx:n.nodes){
                nx.nodes.remove(n);
            }
            nodes.remove(n);
        }
    }
    
    
    public int getNodeSize()
    {
        return (int)(this.getWidth()/40 /zoom);
    }
    
    public int getNodeDistance()
    {
        return getNodeSize()*5;
    }
    
    public double getAngle(Point p1 , Point p2)
    {
        return 180+Math.toDegrees(Math.atan2((p1.x-p2.x),(p1.y-p2.y)));
    }
    
    public double getDistance(Point p1, Point p2)
    {
        return Math.sqrt(Math.pow((p1.x-p2.x), 2)+Math.pow((p1.y-p2.y), 2))*zoom;   
    }
    
    private void databaseError(int error_id)
    {
        System.out.println("DB_ERROR");
    }
    
    
    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.clearRect(0,0,this.getSize().width,this.getSize().height);
        
        if(nodes.isEmpty()){
            g2.drawString(noNodeMessage,this.getSize().width/2-g.getFontMetrics().stringWidth(noNodeMessage)/2, getSize().height/2+3);
        }
        
        if(selectedNode!=null){
            g2.drawLine(selectedNode.getPosition(getCenterNode(),zoom).x, selectedNode.getPosition(getCenterNode(),zoom).y, mousePos.x, mousePos.y);
        }
        
        for (Node n:nodes) {
            Point p = n.getPosition(getCenterNode(),zoom);              

            for(Node subnode:n.nodes) {
                g2.drawLine(subnode.getPosition(getCenterNode(),zoom).x, subnode.getPosition(getCenterNode(),zoom).y, p.x, p.y);
            }
        }
        
        
        for (Node n:nodes)
        {     
            Point p = n.getPosition(getCenterNode(),zoom);
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

            g2.drawString(n.getLabel(), n.getPosition(getCenterNode(),zoom).x-g.getFontMetrics().stringWidth(n.getLabel())/2, n.getPosition(getCenterNode(),zoom).y+3);     
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
        
        if(SwingUtilities.isMiddleMouseButton(event)) {
            Point p = new Point(event.getX()-mouseCenterDifference.x, event.getY()-mouseCenterDifference.y);
            centerNode.setLocation(p);
        }
        
        if(selection != null) {
            selection.setBounds( (int)Math.min(anchor.x,event.getX()), (int)Math.min(anchor.y,event.getY()),(int)Math.abs(event.getX()-anchor.x), (int)Math.abs(event.getY()-anchor.y));
        }
        
        for (Node n:nodes) {
            Point p = n.getPosition(getCenterNode(),zoom);
            if(event.getPoint().distance(p) < getNodeSize()){
                snappedNode=n;
                mousePos=snappedNode.getPosition(getCenterNode(), zoom);
            }        
            if(movingNode != null && n == movingNode) {
                n.followMouse(event);
            }        
        }
        repaint();
    }
    
    public void checkClick(MouseEvent event){
        
        if(SwingUtilities.isMiddleMouseButton(event)) {
            mouseCenterDifference = new Point(event.getX()-centerNode.x, event.getY()-centerNode.y);
        }
        
        else if(SwingUtilities.isRightMouseButton(event))
        {
            Node n = isMouseOnNode(event);
            if(n==null){    //If the right mouse button is clicked anywhere but on a node it unselects the selected Node
                if(selectedNode==null)
                {
                    PanelMenu menu = new PanelMenu(event,selectedNode);
                    menu.show(this, event.getPoint().x, event.getPoint().y);
                }
                selectedNode=null;
            }
            else{       //If the right mouse button is clicked on a node, the context menu opens
                selectedNode=n;

                NodeMenu menu = new NodeMenu(event,selectedNode);
                menu.show(this, selectedNode.getPosition(getCenterNode(),zoom).x, selectedNode.getPosition(getCenterNode(),zoom).y);

                selectedNode=null;
            }
        }
        
        else
        {
            if(movingNode != null){
                double angle = getAngle(getCenterNode(),event.getPoint());
                double distance = getDistance(getCenterNode(),event.getPoint());
                movingNode.setPolar(angle, distance);
                movingNode.stopFollowMouse();
                movingNode=null;
            }
            else if(selectedNode==null){
                selectedNode = isMouseOnNode(event);
            }    
            else if(snappedNode == null)        //a node is selected and anywhere else clicked, so a new node connected to the currently selected node gets created
            {
                createNode(selectedNode,event.getPoint());
                selectedNode=null;
            }        
            else if(snappedNode != selectedNode && !areConnected(snappedNode,selectedNode))     //a node is selected and a other node clicked, asks if they should be connected
            {
                int n = JOptionPane.showConfirmDialog(this,
                "Do you want to connect " + selectedNode.getLabel() + " to " + snappedNode.getLabel() + "?",
                "Connect the nodes?",JOptionPane.YES_NO_OPTION);
                if(n==0) {
                    selectedNode.nodes.add(snappedNode);
                    snappedNode.nodes.add(selectedNode);
                    selectedNode=null;
                }
            }
            else {      //Left mousebutton clicked anywhere on the panel
                selection = new Rectangle(event.getPoint());
            }
        }
        repaint();
    }
    
    
    public Node isMouseOnNode(MouseEvent event) //returns a node if the mousepointer is over it, otherwise null
    {
        for(Node n:nodes){
            if(event.getPoint().distance(n.getPosition(getCenterNode(),zoom))<n.getSize()){
                return n;
            }
        }
        return null;
    }
}
