/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package networkvisualizer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;



/**
 *
 * @author Jessica
 */
public class ChoserFieldPanel extends JPanel {
    
    private JLabel label;
    private JComboBox field;
    private String[] values = {"Normal", "Admin"};
    
    public ChoserFieldPanel(String textLabel){
        
        this.setPreferredSize(new Dimension (300, 100));
        
        Color orange_light_color = new Color(253, 189, 99);
        this.setBackground(orange_light_color);
        
        label = new JLabel(textLabel, SwingConstants.RIGHT);
        label.setPreferredSize(new Dimension(70, 25));
        this.add(label);
        
        field = new JComboBox(values);
        field.setPreferredSize(new Dimension(150, 25));
        this.add(field);
    }
    
    public void setFieldChoser(int index) {
        field.setSelectedIndex(index);
    }
    
    public int getFieldChoser(){
        return field.getSelectedIndex();
    }
    
    public void resetFieldChoser() {
        field.setSelectedIndex(0);
    }
}
