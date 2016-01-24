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
public class CreateModifyUser extends JDialog {
    
    private TextFieldPanel userID;
    private InputFieldPanel username;
    private ChoserFieldPanel type;
    private JButton done;
    private User user;
    private static final String[] VALUES = {"Normal", "Admin"};
    
    public CreateModifyUser(User user) {
        this.user = user;
        if(user == null){
            this.setTitle("Create User");
        } else {
            this.setTitle("Modify User");
        }
        this.setSize(300,500);
        this.setModal(true);
        
        JPanel panel = new JPanel();
        
        if(user != null) {
            userID = new TextFieldPanel("User ID:", user.getUserID());
            panel.add(userID);
        }
        
        username = new InputFieldPanel("Username:");
        if(user != null) {
            username.setFieldText(user.getUsername());
        }
        panel.add(username);
        
        type = new ChoserFieldPanel("Type:", VALUES);
        if(user != null) {
            if(user.isIsAdmin()) {
                type.setFieldChoser(1);
            } else {
                type.setFieldChoser(0);
            }
        }
        panel.add(type);
        
        done = new JButton("Done");
        done.setPreferredSize(new Dimension(100,25));
        done.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String input = username.getFieldText();
                if(input != null && input.length() > 0){
                    if(CreateModifyUser.this.user == null){
                        User user = new User();
                        user.setUsername(input);
                        if(type.getFieldChoser() == 0){
                            user.setIsAdmin(false);
                        } else {
                            user.setIsAdmin(true);
                        }
                        //Insert in DB
                    }else{
                        CreateModifyUser.this.user.setUsername(input);
                        if(type.getFieldChoser() == 0){
                            CreateModifyUser.this.user.setIsAdmin(false);
                        } else {
                            CreateModifyUser.this.user.setIsAdmin(true);
                        }
                        //Modify in DB
                    }
                    CreateModifyUser.this.setVisible(false);
                }else{
                    JOptionPane.showMessageDialog(new JFrame(), "Please, rewrite username", "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(done);
        
        this.add(panel);
        this.setVisible(true);
    }
}
