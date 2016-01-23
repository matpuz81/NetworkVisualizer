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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
/**
 *
 * @author chef
 */
public class GraphPanel extends JPanel {
    
    LinkedList<Node> nodes = new LinkedList();
    LinkedList<NodeLink> nodeLink = new LinkedList();
    //LinkedList<Network> networks = new LinkedList();
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
                if(e.getWheelRotation() <0 && zoom>minZoom)
                {
                    zoom -= zoom/10;
                }
                else if(e.getWheelRotation() > 0 && zoom<maxZoom)
                {
                    zoom += zoom/10;
                }
                /*
                if((zoom > minZoom && e.getUnitsToScroll() < 0) || (zoom < maxZoom && e.getUnitsToScroll() > 0)) {
                    zoom+=zoom/(((double)e.getUnitsToScroll()))/4;
                }*/
            }
        });

        centerNode = new Point(getSize().width/2,getSize().height/2);
        
    }
    
    public void setZoom(double newzoom)
    {
        if(newzoom < 10 && newzoom > 0.2)
            this.zoom=newzoom;
    }
    
    public Point getCenterNode() {
        return centerNode;
    }
    
    public void createNode(Node connectedNode, Point p)     //Used to create a new node (opens the NodeCreateFrame)
    {
        double angle = getAngle(centerNode,p);
        double distance = getDistance(centerNode,p);   
        
        
        Node tmpNode = new Node(angle,distance,getNodeSize(), "192.168.1." + (nodes.size()+1));
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
                addNodeConnection(tmpNode,connectedNode,"",1);
            }
        }
        else
        {
            databaseError(0);
        }
        
        //System.out.println(tmpNode.getNetwork().getNetTopology());

    }
    
    
    public void addNodeFromDB(Network net, int id, double angle, double distance, String label) {
        Node tmpNode = new Node(angle,distance,getNodeSize(),label);
        tmpNode.setId(id);
        tmpNode.setNetwork(net);
        nodes.add(tmpNode);
    }
    
    public void addNodeConnection(Node n1, Node n2,String type, double velocity)
    {
        nodeLink.add(new NodeLink(n1,n2,type,velocity));
        NetworkVisualizer.DB.addNodeConnection(n1, n2);
        System.out.println(n1.getNetwork().getNet_topology());
    }
    
    public void connectNodesById(int id1, int id2,String type, double velocity)
    {
        Node n1=getNodeById(id1), n2=getNodeById(id2);
        if(n1 != null && n2 != null)
        {
            nodeLink.add(new NodeLink(n1,n2,type,velocity));
        }
    }
    
    public void deleteNode(Node n)
    {
        LinkedList<NodeLink> toRemove = new LinkedList();
        for(NodeLink l:nodeLink)
        {
            if(l.n1 == n || l.n2 == n)
            {
                toRemove.add(l);
                NetworkVisualizer.DB.deleteNodeConnection(l.n1, l.n2);
            }
        }
        
        NetworkVisualizer.DB.deleteNode(n);
                

        nodeLink.removeAll(toRemove);
        nodes.remove(n);
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
    
    public Network createNetwork()
    {
        Network net=null;
        try {
            net = new Network();
            int id = NetworkVisualizer.DB.addNetwork(net);
            if(id!=-1)
            {
                net.setId(id);
                //networks.add(net);
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
        
        for(NodeLink l:nodeLink)
        {
            if(l.n1 == n)
                tmp.add(l.n2);
            else if(l.n2 == n)
                tmp.add(l.n1);
        }
        return tmp;
    }
    
    public NodeLink getConnection(Node n1, Node n2)
    {
        if(n1 == null || n2 == null)
            return null;
        
        for(NodeLink l:nodeLink)
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
        NodeLink l = getConnection(n1,n2);
        NetworkVisualizer.DB.deleteNodeConnection(n1, n2);
        nodeLink.remove(l);
    }
    
    
    public int getNodeSize()
    {
        return (int)(this.getWidth()/40 /zoom);
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
        System.out.println("DB_ERROR"+error_id);
    }
    
    private boolean showNodes()
    {
        return zoom<5;
    }
    
    
    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        
        if(!showNodes())     
            g2.setColor(Color.lightGray);
        g2.clearRect(0,0,this.getSize().width,this.getSize().height);
        g2.drawLine((int)(centerNode.x-15/zoom), (int)(centerNode.y), (int)(centerNode.x+15/zoom), (int)(centerNode.y));
        g2.drawLine((int)(centerNode.x), (int)(centerNode.y-15/zoom), (int)(centerNode.x), (int)(centerNode.y+15/zoom));
        if(nodes.isEmpty()){
            g2.drawString(noNodeMessage,this.getSize().width/2-g.getFontMetrics().stringWidth(noNodeMessage)/2, getSize().height/2+3);
        }
        
        if(selectedNode!=null){
            g2.drawLine(selectedNode.getPosition(getCenterNode(),zoom).x, selectedNode.getPosition(getCenterNode(),zoom).y, mousePos.x, mousePos.y);
        }
        
        for (NodeLink l:nodeLink)
        {
            g2.drawLine(l.n1.getPosition(getCenterNode(),zoom).x, l.n1.getPosition(getCenterNode(),zoom).y, l.n2.getPosition(getCenterNode(),zoom).x, l.n2.getPosition(getCenterNode(),zoom).y);
        }
        
        for (Node n:nodes)
        {     
            int x = n.getPosition(centerNode, zoom).x;
            int y = n.getPosition(centerNode, zoom).y;
            g2.setColor(n.getNetwork().getColor().darker());
            if(n==selectedNode)
                g2.setColor(Color.green);
            else if(n==snappedNode) {
                if(areConnected(snappedNode,selectedNode))
                    g2.setColor(Color.red);
                else
                    g2.setColor(Color.blue);
            }
            g2.fillOval(x-getNodeSize()-2,y-getNodeSize()-2,getNodeSize()*2+4, getNodeSize()*2+4);
            g2.setColor(n.getNetwork().getColor());
            
            if(n==selectedNode)
                g2.setColor(n.getNetwork().getColor().brighter());
            g2.fillOval(x-getNodeSize(),y-getNodeSize(),getNodeSize()*2, getNodeSize()*2);
            g2.setColor(nodeBorderColor);

            if(showNodes())
                g2.drawString(n.getLabel(), n.getPosition(getCenterNode(),zoom).x-g.getFontMetrics().stringWidth(n.getLabel())/2, n.getPosition(getCenterNode(),zoom).y+3);     
        }
        
        if(!showNodes())
        {
            for(Network net:getNetworks())
            {
                g2.drawString(net.getName(), getMiddle(net).x-g.getFontMetrics().stringWidth(net.getName())/2, getMiddle(net).y+4);
            }
        }
        
        g2.drawString("Zoom: " + zoom, 5, getSize().height-20);
    }
    
    public void checkForHover(MouseEvent event){

        mousePos = event.getPoint();
        snappedNode=null;
        
        if(SwingUtilities.isMiddleMouseButton(event) && mouseCenterDifference != null) {
            Point p = new Point(event.getX()-mouseCenterDifference.x, event.getY()-mouseCenterDifference.y);
            centerNode.setLocation(p);
        }
        
        /*
        if(selection != null) {
            selection.setBounds( (int)Math.min(anchor.x,event.getX()), (int)Math.min(anchor.y,event.getY()),(int)Math.abs(event.getX()-anchor.x), (int)Math.abs(event.getY()-anchor.y));
        }*/
        
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
                    if(snappedNode.getNetwork() != selectedNode.getNetwork())
                    {
                        n = JOptionPane.showConfirmDialog(this,
                        "Attention!\n" + snappedNode.getLabel() + " is part of " + snappedNode.getNetwork().getName() + 
                        "\nWould you like to merge " + snappedNode.getNetwork().getName() + " into "+ selectedNode.getNetwork().getName() + "?",
                        "Merge the networks?",JOptionPane.YES_NO_OPTION);
                        if(n==0)
                        {
                            for(Node node:snappedNode.getNetwork().getNodes())
                            {
                                node.setNetwork(selectedNode.getNetwork());
                            }
                        }
                        else return;
                    }
                    addNodeConnection(selectedNode,snappedNode,"",1);
                    selectedNode=null;
                }
            }
            else if(selectedNode != null)       //Node is selected and clicked again, which opens its parameters 
            {
                if(snappedNode != selectedNode)
                {
                    return;
                }
                
                NodeParameters params = new NodeParameters(selectedNode);
                selectedNode = null;
                params.setVisible(true);
            }
            else {      //Left mousebutton clicked anywhere on the panel
                selection = new Rectangle(event.getPoint());
            }
        }
        repaint();
    }
    
    public Point getMiddle(Network net)
    {
        double middleAngle=0,middleDistance=0;
        Point p=new Point(0,0);
        int cnt=0;
        for(Node n:nodes)
        {
            if(n.getNetwork() == net)
            {
                Point np = n.getPosition(getCenterNode(), zoom);
                p.x += np.x;
                p.y += np.y;
                cnt++;
            }
                
        }
        p.x/=cnt;
        p.y/=cnt;
        return p;
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
