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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;



/**
 *
 * @author Jessica
 */
public class TextFieldPanel extends JPanel{
    
    private JLabel label_name, label_value;
    private int value;
    
    public TextFieldPanel(String name, int value){
        this.value = value;
        
        this.setPreferredSize(new Dimension (300, 100));
        
        Color orange_light_color = new Color(253, 189, 99);
        this.setBackground(orange_light_color);
        
        label_name = new JLabel(name, SwingConstants.RIGHT);
        label_name.setPreferredSize(new Dimension(100, 25));
        this.add(label_name);
        
        label_value = new JLabel(String.valueOf(value), SwingConstants.RIGHT);
        label_value.setPreferredSize(new Dimension(150, 25));
        this.add(label_value);
    }
    
    public int getValue(){
        return value;
    }
    
    public void setValue(int value) {
        this.value = value;
        label_value.setText(String.valueOf(value));
    }
    

}
