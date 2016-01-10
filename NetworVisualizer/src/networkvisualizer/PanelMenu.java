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
class PanelMenu extends JPopupMenu {
    JMenuItem addNode,properties;
    GraphPanel parent;
    Node node;
    MouseEvent event;
    
    public PanelMenu(GraphPanel parent,MouseEvent event, Node node){
        this.parent = parent;
        this.event=event;
        this.node = node;
        addNode = new JMenuItem("Add Node");
        addNode.setActionCommand("addNode");
        properties = new JMenuItem("Properties");
        properties.setActionCommand("properties");
        
        addNode.addActionListener(new MenuListener(parent, event, node));
        properties.addActionListener(new MenuListener(parent, event, node));
        add(addNode);
        add(properties);
    }
}
