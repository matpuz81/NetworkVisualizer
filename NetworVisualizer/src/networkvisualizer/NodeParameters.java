/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkvisualizer;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

/**
 *
 * @author chef
 */
public class NodeParameters extends JPanel {
    
    JList nodesList;
    GraphPanel parent;
    Node node;
    DefaultListModel lm = new DefaultListModel();
    JPanel listPanel = new JPanel();
    LinkedList<Node> nodesToRemove = new LinkedList();
    JButton cancelButton, saveButton;
    
    public NodeParameters(GraphPanel parent, Node node)
    {
        this.node=node;
        setPreferredSize(new Dimension(600,400));
        setSize(600,400);
        setLayout(new BoxLayout(this,2));
        
        listPanel.setBorder(BorderFactory.createTitledBorder("Connected Nodes:"));
        
        refreshList();
        
        nodesList = new JList(lm);
        nodesList.setPreferredSize(new Dimension(300,150));
        nodesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        nodesList.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e) {
                nodesList.setSelectedIndex(nodesList.locationToIndex(e.getPoint()));
                checkMousePress(e);
            }
        });
        listPanel.add(nodesList);
        
        cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("cancel");
        //cancelButton.addActionListener(new NodeParametersListener());
        saveButton = new JButton("Save");
        saveButton.setActionCommand("save");
        //saveButton.addActionListener(new NodeParametersListener());
        
        add(listPanel);
    }
    
    void refreshList()
    {
        lm.clear();
        for(Node n:node.nodes){
            if(!nodesToRemove.contains(n))
            lm.addElement(n.label);
        }
    }
    
    void save()
    {
        for(Node n:nodesToRemove)
        {
            node.nodes.remove(n);
            n.nodes.remove(node);
        }
    }
    
    void close()
    {
        
    }
    
    void checkMousePress(MouseEvent e)
    {    
        if(SwingUtilities.isRightMouseButton(e))
        { 
            if(nodesList.getSelectedIndex() != -1)
            {
                NodeParametersMenu paramMenu = new NodeParametersMenu(parent, node,node.nodes.get(nodesList.getSelectedIndex()));
                paramMenu.show(nodesList,e.getX(),e.getY());
            }
        }
    }
    
    class NodeParametersMenu extends JPopupMenu
    {
        GraphPanel parent;
        JMenuItem disconnectNode,nodeProperties;
        Node node, connectedNode;

        public NodeParametersMenu(GraphPanel parent, Node node, Node connectedNode){
            this.parent = parent;
            this.node = node;
            disconnectNode = new JMenuItem("Delete");
            disconnectNode.setActionCommand("disconnectNode");
            nodeProperties = new JMenuItem("Properties");
            nodeProperties.setActionCommand("nodeProperties");

            disconnectNode.addActionListener(new NodeParametersListener(parent, node,connectedNode));
            nodeProperties.addActionListener(new NodeParametersListener(parent, node, connectedNode));
            
            
            add(disconnectNode);
            add(nodeProperties);
        }
    }
    
    class NodeParametersListener implements ActionListener
    {
        //GraphPanel parent;
        Node node, connectedNode;
        private NodeParametersListener(GraphPanel parent, Node node, Node connectedNode) {
            //this.parent=parent;
            this.node=node;
            this.connectedNode=connectedNode;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            switch(e.getActionCommand())
            {
                case "disconnectNode": 
                    nodesToRemove.add(connectedNode);
                    refreshList();
                    //parent.repaint();
                    break;
            }
        }
        
    }
}


