/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkvisualizer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author chef
 */
public class NetworkVisualizer {
    
    //This is the global DB Object. Every ineracction with the db should be done over this object.
    public static final DBCore DB = new DBCore();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE); 
        NetworkVisualizerPanel networkPanel = new NetworkVisualizerPanel();
        frame.getContentPane().add(networkPanel); 
        frame.pack();
        frame.setVisible(true);
    }
}
