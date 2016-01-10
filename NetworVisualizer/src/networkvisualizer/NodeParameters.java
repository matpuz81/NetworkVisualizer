/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkvisualizer;

import java.awt.Dimension;
import java.util.LinkedList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 *
 * @author chef
 */
public class NodeParameters extends JPanel {
    
    JList nodesList;
    DefaultListModel lm = new DefaultListModel();
    JPanel listPanel = new JPanel();
    
    public NodeParameters(Node node)
    {
        setPreferredSize(new Dimension(600,400));
        setSize(600,400);
        setLayout(new BoxLayout(this,2));
        
        listPanel.setBorder(BorderFactory.createTitledBorder("Connected Nodes:"));
        
        for(Node n:node.nodes){
            lm.addElement(n.label);
        }
        
        nodesList = new JList(lm);
        nodesList.setSize(300,150);
        listPanel.add(nodesList);
        
        add(listPanel);
    }
}
