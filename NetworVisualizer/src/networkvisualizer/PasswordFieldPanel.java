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
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;



/**
 *
 * @author Jessica
 */
public class PasswordFieldPanel extends JPanel{
    
    private JLabel label;
    private JPasswordField field;
    
    public PasswordFieldPanel(String textLabel){
        
        this.setPreferredSize(new Dimension (300, 100));
        
        Color orange_light_color = new Color(253, 189, 99);
        this.setBackground(orange_light_color);
        
        label = new JLabel(textLabel, SwingConstants.RIGHT);
        label.setPreferredSize(new Dimension(100, 25));
        this.add(label);
        
        field = new JPasswordField();
        field.setPreferredSize(new Dimension(150, 25));
        this.add(field);
    }
    
    public String getFieldText(){
        String ret = "";
        char[] password = field.getPassword();
        if(password != null && password.length > 0){
            ret = String.valueOf(password);
        }
        return ret;
    }
    
    public void setFieldText(String text) {
        field.setText(text);
    }
    
    public void resetFieldText() {
        field.setText("");
    }
}
