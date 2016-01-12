/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkvisualizer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

/**
 *
 * @author chef
 */
class NodeMenu extends JPopupMenu {
    JMenuItem moveNode,deleteNode,nodeProperties;
    GraphPanel parent;
    Node node;
    MouseEvent event;
    
    public NodeMenu(GraphPanel parent,MouseEvent event, Node node){
        this.parent = parent;
        this.event=event;
        this.node = node;
        moveNode = new JMenuItem("Move");
        moveNode.setActionCommand("moveNode");
        deleteNode = new JMenuItem("Delete");
        deleteNode.setActionCommand("deleteNode");
        nodeProperties = new JMenuItem("Properties");
        nodeProperties.setActionCommand("nodeProperties");
        
        moveNode.addActionListener(new MenuListener(parent, event, node));
        deleteNode.addActionListener(new MenuListener(parent, event, node));
        nodeProperties.addActionListener(new MenuListener(parent, event, node));
        add(moveNode);
        add(deleteNode);
        add(nodeProperties);
    }
}
