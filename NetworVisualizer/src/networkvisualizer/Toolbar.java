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
    public Toolbar (){
        zoomSlider = new JSlider(2,100,10);
        zoomSlider.setInverted(true);
        zoomSlider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                NetworkVisualizer.panel.setZoom(((double)zoomSlider.getValue())/10);
                NetworkVisualizer.panel.repaint();
            }
        
        });
        add(new JLabel("-"));
        add(zoomSlider);
        add(new JLabel("+"));
    }
    
    
    public class ToolbarListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            
        }
    
    }
}
