/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkvisualizer;

import java.awt.BorderLayout;
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
        Toolbar toolbar = new Toolbar();
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        this.add(toolbar,BorderLayout.NORTH);
        this.add(NetworkVisualizer.panel,BorderLayout.CENTER);
        
    }
    

}
