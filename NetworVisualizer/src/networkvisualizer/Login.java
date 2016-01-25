/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkvisualizer;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Jessica
 */
public class Login extends JDialog {
    
    private InputFieldPanel username; 
    private PasswordFieldPanel password;
    private JButton login;
   
    
    public Login() {
        this.setTitle("Login");

        this.setSize(300,500);
        this.setModal(true);
        
        JPanel panel = new JPanel();
        
        username = new InputFieldPanel("Username:");
        panel.add(username);
        
        password = new PasswordFieldPanel("Password:");
        panel.add(password);
        
        login = new JButton("Login");
        login.setPreferredSize(new Dimension(100,25));
        login.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String username, password;
                boolean isValid = true;
                
                username = Login.this.username.getFieldText();
                password = Login.this.password.getFieldText();
                
                if(username == null || username.length() == 0){
                    isValid = false;
                }
                
                if(password == null || password.length() == 0){
                    isValid = false;
                }
                
                if(isValid){
                    //Check if user exists
                    if(true) { //user exists
                        //set as current user and go to next window
                    } else {
                        JOptionPane.showMessageDialog(new JFrame(), "Please, rewrite access data", "Error",
                        JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    JOptionPane.showMessageDialog(new JFrame(), "Please, rewrite access data", "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(login);
        
        this.add(panel);
        this.setVisible(true);
    }
}
