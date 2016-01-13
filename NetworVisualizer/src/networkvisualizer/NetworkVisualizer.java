/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkvisualizer;

import javax.swing.JFrame;

/**
 *
 * @author chef
 */
public class NetworkVisualizer {
    
    public static final GraphPanel panel = new GraphPanel();
    //This is the global DB Object. Every ineracction with the db should be done over this object.
    public static final DBCore DB = new DBCore();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        NetworkVisualizerPanel networkPanel = new NetworkVisualizerPanel();
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE); 
        frame.getContentPane().add(networkPanel); 
        frame.pack();
        frame.setVisible(true);
    }
}
