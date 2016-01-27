/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkvisualizer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author chef
 */
class MenuListener implements ActionListener{
    
    GraphPanel parent;
    Node node;
    MouseEvent mouse;
    public MenuListener(MouseEvent mouse, Node node)
    {
        this.parent=NetworkVisualizer.panel;
        this.mouse=mouse;
        this.node=node;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "moveNetwork":
                parent.selectedNodes.clear();
                for(Node n:node.getNetwork().getNodes()) {
                    parent.selectedNodes.add(n);
                }
            case "moveNode":
                parent.mouseStartPolar = Polar.getPolar(parent.getCenter(), mouse.getPoint(),parent.getZoom());
                parent.moveSelected=true;
                break;
            case "addNode":
                parent.createNode(null,mouse.getPoint());
                break;
            case "deleteNode":
                parent.selectedNodes.clear();
                int i= JOptionPane.showConfirmDialog(null,"Delete Node " + node.getLabel() + "?",
                        "Are you sure?",JOptionPane.YES_NO_OPTION);
                if(i==0)
                    parent.deleteNode(node);
                break;
            case "nodeParameters":
                parent.selectedNodes.clear();
                NodeParameters paramsFrame = new NodeParameters(node);
                paramsFrame.setVisible(true);
                break;
        }
    }
    
}
