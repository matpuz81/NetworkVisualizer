/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkvisualizer;

import java.awt.event.MouseEvent;
import java.util.LinkedList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author chef
 */
class NodeMenu extends JPopupMenu {
    JMenuItem moveNode, moveNetwork,deleteNode,nodeProperties;
    Node node;
    MouseEvent event;
    
    
    public NodeMenu(MouseEvent event, Node node){
        this.event=event;
        this.node = node;
        
        if(NetworkVisualizer.panel.selectedNodes.size() == 1)
        {
            moveNode = new JMenuItem("Move");
            moveNode.setActionCommand("moveNode");
            moveNetwork = new JMenuItem("Move Network");
            moveNetwork.setActionCommand("moveNetwork");
            deleteNode = new JMenuItem("Delete");
            deleteNode.setActionCommand("deleteNode");
            nodeProperties = new JMenuItem("Properties");
            nodeProperties.setActionCommand("nodeProperties");       
            moveNode.addActionListener(new MenuListener(event, node));
            moveNetwork.addActionListener(new MenuListener(event, node));
            deleteNode.addActionListener(new MenuListener(event, node));
            nodeProperties.addActionListener(new MenuListener(event, node));
            add(moveNode);
            add(moveNetwork);
            add(deleteNode);
            add(nodeProperties);
        }
        else
        {
            moveNode = new JMenuItem("Move Group");
            moveNode.setActionCommand("moveNode");
            moveNode.addActionListener(new MenuListener(event, node));
            add(moveNode);
        }
        

    }
}
