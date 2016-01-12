/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkvisualizer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author chef
 */
class MenuListener implements ActionListener{
    
    GraphPanel parent;
    Node node;
    MouseEvent mouse;
    public MenuListener(GraphPanel parent, MouseEvent mouse, Node node)
    {
        this.parent=parent;
        this.mouse=mouse;
        this.node=node;
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "moveNode":
                parent.movingNode=node;
                break;
            case "addNode":
                parent.addParent(mouse.getPoint());
                break;
            case "deleteNode":
                int i= JOptionPane.showConfirmDialog(null,"Delete Node " + node.label + "?",
                        "Are you sure?",JOptionPane.YES_NO_OPTION);
                if(i==0)
                    parent.deleteNode(node);
                break;
            case "disconnectNode":
                
                break;
            case "nodeProperties":
                NodeParameters paramsFrame = new NodeParameters(parent, node);
                break;
        }
    }
    
}
