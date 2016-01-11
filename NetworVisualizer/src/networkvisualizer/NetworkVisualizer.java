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
    
    //Test
    
}
