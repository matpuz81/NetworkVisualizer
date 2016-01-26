/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package networkvisualizer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author chef
 */
public class Toolbar extends JPanel{
    
    JSlider zoomSlider;
    JButton zoomToNetView = new JButton("View Networks");
    JButton zoomToNodeView = new JButton("View Nodes");
    public Toolbar (){
        
        
        zoomToNetView.setActionCommand("zoomToNetView");
        zoomToNetView.addActionListener(new ToolbarListener());
        zoomToNodeView.setActionCommand("zoomToNodeView");
        zoomToNodeView.addActionListener(new ToolbarListener());
        zoomSlider = new JSlider((int)(NetworkVisualizer.panel.minZoom*10),(int)(NetworkVisualizer.panel.maxZoom*10),10);
        zoomSlider.setInverted(true);
        zoomSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                NetworkVisualizer.panel.setZoom(((double)zoomSlider.getValue())/10);
                NetworkVisualizer.panel.repaint();
            }
        });
        
        
        add(zoomToNetView);
        add(zoomToNodeView);
        add(new JLabel("-"));
        add(zoomSlider);
        add(new JLabel("+"));
    }
    
    
    
    public class ToolbarListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            switch(e.getActionCommand())
            {
                case "zoomToNetView": 
                    zoomSlider.setValue(51);
                    break;
                case "zoomToNodeView": 
                    zoomSlider.setValue(10);
                    break;
            }
        }
    
    }
}
