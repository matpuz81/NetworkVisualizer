/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkvisualizer;

import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author chef
 */
class NodeMenu extends JPopupMenu {
    JMenuItem moveNode,deleteNode,nodeProperties;
    Node node;
    MouseEvent event;
    
    public NodeMenu(MouseEvent event, Node node){
        this.event=event;
        this.node = node;
        moveNode = new JMenuItem("Move");
        moveNode.setActionCommand("moveNode");
        deleteNode = new JMenuItem("Delete");
        deleteNode.setActionCommand("deleteNode");
        nodeProperties = new JMenuItem("Properties");
        nodeProperties.setActionCommand("nodeProperties");
        
        moveNode.addActionListener(new MenuListener(event, node));
        deleteNode.addActionListener(new MenuListener(event, node));
        nodeProperties.addActionListener(new MenuListener(event, node));
        add(moveNode);
        add(deleteNode);
        add(nodeProperties);
    }
}
