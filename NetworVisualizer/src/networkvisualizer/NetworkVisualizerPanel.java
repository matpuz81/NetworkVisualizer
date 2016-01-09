/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkvisualizer;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author chef
 */
public class NetworkVisualizerPanel extends JPanel{
        
    JLabel label;
    JTextField field; 
    
    public NetworkVisualizerPanel()
    {
        GraphPanel graph = new GraphPanel();
        label = new JLabel();
        label.setText("ad");
       
        field = new JTextField("", 10);
        
        this.setBackground(Color.WHITE);
        //this.add(label);
        //this.add(field);
        this.add(graph);
        
    }
    

}
