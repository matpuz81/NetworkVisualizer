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
    private String name = "", value = "";
    
    private TextFieldPanel(){
        this.setPreferredSize(new Dimension (300, 100));
        
        Color orange_light_color = new Color(253, 189, 99);
        this.setBackground(orange_light_color);
        
        label_name = new JLabel(name, SwingConstants.RIGHT);
        label_name.setPreferredSize(new Dimension(100, 25));
        this.add(label_name);
        
        label_value = new JLabel(value, SwingConstants.RIGHT);
        label_value.setPreferredSize(new Dimension(150, 25));
        this.add(label_value);
    }
    
    public TextFieldPanel(String name, int value){
        this();
        this.name = name;
        this.value = String.valueOf(value);
        updateDialog();
    }
    
    public TextFieldPanel(String name, String value){
        this();
        this.name = name;
        this.value = value;
        updateDialog();
    }
    
    private void updateDialog(){
        label_name.setText(name);
        label_value.setText(value);
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(int value) {
        this.value = String.valueOf(value);
        updateDialog();
    }
    
    public void setValue(String value) {
        this.value = value;
        updateDialog();
    }
}
