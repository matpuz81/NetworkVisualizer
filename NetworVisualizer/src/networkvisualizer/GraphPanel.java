/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkvisualizer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
/**
 *
 * @author chef
 */
public class GraphPanel extends JPanel {
    
    LinkedList<Node> nodes = new LinkedList();
    LinkedList<NodeConnection> nodeConnection = new LinkedList();
    //LinkedList<Network> networks = new LinkedList();
    Color nodeColor = Color.white;
    Color nodeBorderColor = Color.black;
    Color possibleColor = Color.lightGray;
    Color possibleHighlighted = Color.GREEN;
    private final Point centerPoint;
    Point mouseCenterDifference;
    
    LinkedList<Node> selectedNodes = new LinkedList();
    Node snappedNode=null;
    Polar mouseStartPolar;
    
    boolean moveSelected=false;
    Point anchor;
    Point mousePos = new Point();
    Rectangle selection=null;
    
    String noNodeMessage ="Right click -> 'Add Node' to add Nodes";
    private double goalZoom=1.00, zoom=1.00;
    public final double maxZoom=10.0, minZoom=0.2, networkViewZoom=5.0;
    
    
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
                checkForDragging(event);
            }
            
        });
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                checkClick(e);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                checkRelease(e);
            }
        });
        
        addMouseWheelListener( new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                
                if((goalZoom > minZoom && e.getUnitsToScroll() < 0) || (goalZoom < maxZoom && e.getUnitsToScroll() > 0)) {
                    if(e.getWheelRotation() > 0)
                        goalZoom+= goalZoom/(goalZoom*10);
                    else 
                        goalZoom -= goalZoom/(goalZoom*10);
                    repaint();
                    NetworkVisualizer.toolbar.zoomSlider.setValue((int)(goalZoom*10));
                }
            }
        });
        

        centerPoint = new Point(getSize().width/2,getSize().height/2);
        
    }
    
    public double getZoom()
    {
        return zoom;
    }
    
    public void setZoom(double newzoom)
    {
        if(newzoom < maxZoom && newzoom > minZoom)
            this.goalZoom=newzoom;
        repaint();
    }
    
    public Point getCenter() {
        return centerPoint;
    }
    
    public void createNode(Node connectedNode, Point p)     //Used to create a new node (opens the NodeCreateFrame)
    {
        Node tmpNode = new Node(Polar.getPolar(centerPoint, p, zoom), getNodeSize(), "192.168.1." + (nodes.size()+1));
        if(connectedNode != null) {
            tmpNode.setNetwork(connectedNode.getNetwork());
        }
        else {
            tmpNode.setNetwork(createNetwork());
        }
        
        int id = NetworkVisualizer.DB.addNode(tmpNode);

        if(id!=-1)
        {          
            tmpNode.setId(id);
            nodes.add(tmpNode);
            if(connectedNode != null) {
                addNodeConnection(tmpNode,connectedNode,"Wired",1);
            }
        }
        else {
            databaseError(0);
        }
        
        repaint();
    }
    
    
    public void addNodeFromDB(Network net, int id, double angle, double distance, String label) {
        Node tmpNode = new Node(new Polar(angle,distance),getNodeSize(),label);
        tmpNode.setId(id);
        tmpNode.setNetwork(net);
        nodes.add(tmpNode);
    }
    
    public void addNodeConnection(Node n1, Node n2,String type, double velocity)
    {
        nodeConnection.add(new NodeConnection(n1,n2,type,velocity));
        NetworkVisualizer.DB.addNodeConnection(n1, n2);
        System.out.println(n1.getNetwork().getNet_topology());
    }
    
    public void connectNodesById(int id1, int id2,String type, double velocity)
    {
        Node n1=getNodeById(id1), n2=getNodeById(id2);
        if(n1 != null && n2 != null) {
            nodeConnection.add(new NodeConnection(n1,n2,type,velocity));
        }
    }
    
    public void deleteNode(Node n)
    {
        Network net = n.getNetwork();
        if(net.getNodes().size() == 1)
        {
            int x = JOptionPane.showConfirmDialog(this,
            "Attention!\n" + n.getLabel() + " is the Last node of " + net.getName() + 
            "\nBy deleting this Node, " + net.getName() + " will be deleted too.\nDo you want to proceed?",
            "Delete network?",JOptionPane.YES_NO_OPTION);
            if(x==1)
                return;
        }
        
        LinkedList<NodeConnection> toRemove = new LinkedList();
        for(NodeConnection l:nodeConnection)
        {
            if(l.n1 == n || l.n2 == n)
            {
                toRemove.add(l);
                NetworkVisualizer.DB.deleteNodeConnection(l.n1, l.n2);
            }
        }
        

        nodeConnection.removeAll(toRemove);
        nodes.remove(n);
        
        try {
            NetworkVisualizer.DB.deleteNode(n);
            if(net.getNodes().isEmpty()) {
                NetworkVisualizer.DB.deleteNetwork(net);
            }
        } catch (Exception ex) {
        }
                
    }
    
    public Node getNodeById(int id)
    {
        for(Node n:nodes) {
            if(n.getId() == id)
                return n;
        }
        return null;
    }
    
    public Network createNetwork()
    {
        Network net=null;
        try {
            net = new Network();
            int id = NetworkVisualizer.DB.addNetwork(net);
            if(id!=-1){
                net.setId(id);
            }
        } catch (Exception ex) {
            Logger.getLogger(GraphPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return net;
    }
    
    public Network getNetworkById(int id)
    {
        for(Network n:getNetworks()) {
            if(n.getId() == id )
                return n;
        }
        return null;
    }
    
    public LinkedList<Network> getNetworks()
    {
        LinkedList<Network> networks = new LinkedList();
        for(Node n:nodes)
        {
            if(!networks.contains(n.getNetwork()) && n.getNetwork() != null)
                networks.add(n.getNetwork());
        }
        return networks;
    }
    
    
    public boolean areConnected(Node n1, Node n2)
    {
        return getConnection(n1,n2) != null;
    }
    
    public LinkedList<Node> getConnectedNodes(Node n)
    {
        if(n == null) return null;
        LinkedList<Node> tmp = new LinkedList();
        
        for(NodeConnection l:nodeConnection)
        {
            if(l.n1 == n)
                tmp.add(l.n2);
            else if(l.n2 == n)
                tmp.add(l.n1);
        }
        return tmp;
    }
    
    public NodeConnection getConnection(Node n1, Node n2)
    {
        if(n1 == null || n2 == null)
            return null;
        
        for(NodeConnection l:nodeConnection)
        {
            if(l.n1 == n1 && l.n2 == n2 || l.n1 == n2 && l.n2 == n1)
            {
                return l;
            }
        }
        return null;
    }
    
    public void removeConnection(Node n1, Node n2)
    {
        NodeConnection l = getConnection(n1,n2);
        NetworkVisualizer.DB.deleteNodeConnection(n1, n2);
        nodeConnection.remove(l);
    }
    
    public int getNodeSize()
    {
        return (int)(20 /zoom);
    }
    
    private void databaseError(int error_id)
    {
        System.out.println("DB_ERROR"+error_id);
    }
    
    private boolean showNodes()
    {
        return zoom<networkViewZoom;
    }
    
    public Point getNodePosition(Node n)
    {
        if(moveSelected && selectedNodes.contains(n))
        {
            Point p = n.getPosition(getCenter(), zoom);
            Point mouseStart = Polar.getPoint(mouseStartPolar.getAngle(),mouseStartPolar.getDistance()/zoom);
            Polar curr = Polar.getPolar(getCenter(),mousePos,zoom);            
            Point currentMouse = Polar.getPoint(curr.getAngle(),curr.getDistance()/zoom);
            return new Point(p.x+currentMouse.x-mouseStart.x, p.y+currentMouse.y - mouseStart.y);
        }
        return n.getPosition(getCenter(), zoom);
    }
    
    
    public Node isMouseOnNode(MouseEvent event) //returns a node if the mousepointer is over it, otherwise null
    {
        for(Node n:nodes){
            if(event.getPoint().distance(getNodePosition(n))<n.getSize()){
                return n;
            }
        }
        return null;
    }
    
    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        
        if(zoom < goalZoom)
            zoom += (goalZoom-zoom)/70;
        if(zoom > goalZoom)
            zoom -= (zoom-goalZoom)/70;
        
        if(Math.abs(zoom - goalZoom) < 0.01)
            zoom = goalZoom;
        
        if(!showNodes())     
            g2.setColor(Color.lightGray);
        g2.clearRect(0,0,this.getSize().width,this.getSize().height);
        g2.drawLine((int)(centerPoint.x-15/zoom), (int)(centerPoint.y), (int)(centerPoint.x+15/zoom), (int)(centerPoint.y));
        g2.drawLine((int)(centerPoint.x), (int)(centerPoint.y-15/zoom), (int)(centerPoint.x), (int)(centerPoint.y+15/zoom));
        if(nodes.isEmpty()){
            g2.drawString(noNodeMessage,this.getSize().width/2-g.getFontMetrics().stringWidth(noNodeMessage)/2, getSize().height/2+3);
        }
        
        if(selectedNodes.size()==1){
            if(selection == null)
            g2.drawLine(getNodePosition(selectedNodes.getFirst()).x, getNodePosition(selectedNodes.getFirst()).y, mousePos.x, mousePos.y);
        }
        
        for (NodeConnection l:nodeConnection)
        {
            g2.drawLine(getNodePosition(l.n1).x, getNodePosition(l.n1).y, getNodePosition(l.n2).x, getNodePosition(l.n2).y);
        }
        
        for (Node n:nodes)
        {     
            int x = getNodePosition(n).x;
            int y = getNodePosition(n).y;
            g2.setColor(n.getNetwork().getColor().darker());
            if(selectedNodes.contains(n))
                g2.setColor(Color.green);
            else if(n==snappedNode) {
                if(selectedNodes.size() == 1 && areConnected(snappedNode,selectedNodes.getFirst()))
                    g2.setColor(Color.red);
                else
                    g2.setColor(Color.blue);
            }
            g2.fillOval(x-getNodeSize()-2,y-getNodeSize()-2,getNodeSize()*2+4, getNodeSize()*2+4);
            g2.setColor(n.getNetwork().getColor());
            
            if(!selectedNodes.isEmpty() && selectedNodes.contains(n))
                g2.setColor(n.getNetwork().getColor().brighter());
            g2.fillOval(x-getNodeSize(),y-getNodeSize(),getNodeSize()*2, getNodeSize()*2);
            g2.setColor(nodeBorderColor);

            if(showNodes())
                g2.drawString(n.getLabel(), getNodePosition(n).x-g.getFontMetrics().stringWidth(n.getLabel())/2, getNodePosition(n).y+3);     
        }
        
        if(!showNodes())
        {
            for(Network net:getNetworks()){
                g2.drawString(net.getName(), net.getMiddle().x-g.getFontMetrics().stringWidth(net.getName())/2, net.getMiddle().y+4);
            }
        }
        if(selection != null)
        {
            float dash1[] = {10.0f};
            g2.setStroke(new BasicStroke(1.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,10.0f, dash1, 0.0f));
            g2.draw(selection);
        }
        
            g2.drawString("Zoom: " + Math.round(100/goalZoom) + "%", 15, getSize().height-20);
        
        if(zoom != goalZoom)
        {
            repaint();
        }
        
    }
    
    public void checkForDragging(MouseEvent event) {
        if(SwingUtilities.isMiddleMouseButton(event) && mouseCenterDifference != null) {
            Point p = new Point(event.getX()-mouseCenterDifference.x, event.getY()-mouseCenterDifference.y);
            centerPoint.setLocation(p);
        }
        checkForHover(event);
    }
    
    public void checkForHover(MouseEvent event){

        mousePos = event.getPoint();
        snappedNode=null;


        
        if(selection != null) {
            selection.setBounds( (int)Math.min(anchor.x,event.getX()), (int)Math.min(anchor.y,event.getY()),(int)Math.abs(event.getX()-anchor.x), (int)Math.abs(event.getY()-anchor.y));
            for(Node n:nodes)
            {
                if(selection.contains(getNodePosition(n))) {
                    if(!selectedNodes.contains(n))
                        selectedNodes.add(n);
                }
                else {                    
                    if(selectedNodes.contains(n))
                        selectedNodes.remove(n);
                }
            }
        }
        
        for (Node n:nodes) {
            Point p = getNodePosition(n);
            if(event.getPoint().distance(p) < getNodeSize()){
                snappedNode=n;
                mousePos=getNodePosition(snappedNode);
            }             
        }
        repaint();
    }
    
    public void checkRelease(MouseEvent event)
    {
        if(selection != null)
            selection = null;
        
        repaint();
    }
    
    public void checkClick(MouseEvent event){
        
        if(SwingUtilities.isMiddleMouseButton(event)) {
            mouseCenterDifference = new Point(event.getX()-centerPoint.x, event.getY()-centerPoint.y);
        }
        
        else if(SwingUtilities.isRightMouseButton(event))
        {
            Node n = isMouseOnNode(event);
            if(n==null){    //If the right mouse button is clicked anywhere but on a node it unselects the selected Node
                if(selectedNodes.size()==0)
                {
                    PanelMenu menu = new PanelMenu(event,null);
                    menu.show(this, event.getPoint().x, event.getPoint().y);
                }
                selectedNodes.clear();
            }
            else {     //If the right mouse button is clicked on a node, the context menu opens

                
                if(selectedNodes.isEmpty())     // If no element is selected
                    selectedNodes.addFirst(n);      // add the clicked element as first element
                
                NodeMenu menu = new NodeMenu(event,selectedNodes.getFirst());
                menu.show(this, event.getPoint().x, event.getPoint().y);
                
                
            }
        }
        
        else
        {
            if(moveSelected){
                for(Node n:selectedNodes)
                {
                    n.setPolar(new Polar(getCenter(), getNodePosition(n), zoom));
                    
                }
                    moveSelected=false;
                selectedNodes.clear();
                
            }
            else if(selectedNodes.isEmpty() && isMouseOnNode(event) != null){
                selectedNodes.add(isMouseOnNode(event));
            }    
            else if(selectedNodes.size() == 1 && snappedNode == null)        //a node is selected and anywhere else clicked, so a new node connected to the currently selected node gets created
            {
                createNode(selectedNodes.getFirst(),event.getPoint());
                selectedNodes.clear();
            }        
            else if(selectedNodes.size() == 1 && snappedNode != selectedNodes.getFirst() && !areConnected(snappedNode,selectedNodes.getFirst()))     //a node is selected and a other node clicked, asks if they should be connected
            {
                int n = JOptionPane.showConfirmDialog(this,
                "Do you want to connect " + selectedNodes.getFirst().getLabel() + " to " + snappedNode.getLabel() + "?",
                "Connect the nodes?",JOptionPane.YES_NO_OPTION);
                if(n==0) {
                    if(snappedNode.getNetwork() != selectedNodes.getFirst().getNetwork())
                    {
                        n = JOptionPane.showConfirmDialog(this,
                        "Attention!\n" + snappedNode.getLabel() + " is part of " + snappedNode.getNetwork().getName() + 
                        "\nWould you like to merge " + snappedNode.getNetwork().getName() + " into "+ selectedNodes.getFirst().getNetwork().getName() + "?",
                        "Merge the networks?",JOptionPane.YES_NO_OPTION);
                        if(n==0)
                        {
                            Network net = snappedNode.getNetwork();
                            for(Node node:net.getNodes())
                            {
                                System.out.println(node.getNetwork().getId());
                                node.setNetwork(selectedNodes.getFirst().getNetwork());
                                System.out.println(node.getNetwork().getId());
                                NetworkVisualizer.DB.updateNode(node);
                            }
                            try {
                                boolean a = NetworkVisualizer.DB.deleteNetwork(net);
                                
                                System.out.println(a);
                            } catch (Exception ex) {
                                System.out.println(ex.getMessage());
                            }
                        }
                        else return;
                    }
                    addNodeConnection(selectedNodes.getFirst(),snappedNode,"Wired",1);
                    selectedNodes.clear();
                }
            }
            else if(selectedNodes.size() == 1)       //Node is selected and clicked again, which opens its parameters 
            {
                if(snappedNode != selectedNodes.getFirst())
                {
                    return;
                }
                
                NodeParameters params = new NodeParameters(selectedNodes.getFirst());
                selectedNodes.clear();
                params.setVisible(true);
            }
            else {      //Left mousebutton clicked anywhere on the panel
                if(selection != null)
                    selection = null;
                else
                {
                    selection = new Rectangle(event.getPoint());
                    anchor = event.getPoint();
                }
            }
        }
        repaint();
    }
    
    
}
