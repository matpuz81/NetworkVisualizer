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
public class CreateModifyNetworkType extends JDialog {
    
    private TextFieldPanel idModify;
    private InputFieldPanel idCreate, description;
    private JButton done;
    private NetworkType type;
    
    public CreateModifyNetworkType(NetworkType type) {
        this.type = type;
        if(type == null){
            this.setTitle("Create Network Type");
        } else {
            this.setTitle("Modify Network Type");
        }
        this.setSize(300,500);
        this.setModal(true);
        
        JPanel panel = new JPanel();
        
        if(type != null) {
            idModify = new TextFieldPanel("ID:", type.getId());
            panel.add(idModify);
        }else{
            idCreate = new InputFieldPanel("ID:");
            panel.add(idCreate);
        }
        
        description = new InputFieldPanel("Description:");
        if(type != null) {
            description.setFieldText(type.getDescription());
        }
        panel.add(description);
        
        done = new JButton("Done");
        done.setPreferredSize(new Dimension(100,25));
        done.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String id, descr;
                boolean isValid = true;
                
                if(CreateModifyNetworkType.this.type == null){
                    id = idCreate.getFieldText();
                }else{
                    id = idModify.getValue();
                }
                descr = CreateModifyNetworkType.this.description.getFieldText();
                
                if(id == null || id.length() == 0){
                    isValid = false;    
                }
                
                if(descr == null || descr.length() == 0){
                    isValid = false;
                }
                
                if(isValid == true){
                    if(CreateModifyNetworkType.this.type == null){
                        NetworkType type = new NetworkType(id, descr);
                        //Insert in DB
                    }else{
                        CreateModifyNetworkType.this.type.setDescription(descr);
                        //Modify in DB
                    }
                    CreateModifyNetworkType.this.setVisible(false);
                }else{
                    JOptionPane.showMessageDialog(new JFrame(), "The inserted values are incorrect", "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(done);
        
        this.add(panel);
        this.setVisible(true);
    }
}
