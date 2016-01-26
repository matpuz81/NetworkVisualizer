/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkvisualizer;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Jessica
 */
public class CreateModifyCommunicationProtocol extends JDialog {
    
    private TextFieldPanel id;
    private InputFieldPanel name, description;
    private ChoserFieldPanel level;
    private JButton done;
    private CommunicationProtocol protocol;
    private static final int MIN_LEVEL = 1, MAX_LEVEL = 7;
    
    public CreateModifyCommunicationProtocol(CommunicationProtocol protocol) {
        this.protocol = protocol;
        if(protocol == null){
            this.setTitle("Create Communication Protocol");
        } else {
            this.setTitle("Modify Communication Protocol");
        }
        this.setSize(300,500);
        this.setModal(true);
        
        JPanel panel = new JPanel();
        
        if(protocol != null) {
            id = new TextFieldPanel("Communication Protocol ID:", protocol.getId());
            panel.add(id);
        }
        
        name = new InputFieldPanel("Name:");
        if(protocol != null) {
            name.setFieldText(protocol.getName());
        }
        panel.add(name);
        
        description = new InputFieldPanel("Description:");
        if(protocol != null) {
            description.setFieldText(protocol.getDescription());
        }
        panel.add(description);
        
        level = new ChoserFieldPanel("Level:", this.generateValues());
        if(protocol != null && protocol.getLevel() >= MIN_LEVEL && protocol.getLevel() <= MAX_LEVEL) {
            level.setFieldChoser(protocol.getLevel()- MIN_LEVEL);
        }
        panel.add(level);
        
        done = new JButton("Done");
        done.setPreferredSize(new Dimension(100,25));
        done.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String protocolName, protocolDescritpion;
                int protocolLevel = -1;
                boolean isValid = true;
                
                protocolName = name.getFieldText();
                if(protocolName == null || protocolName.length() == 0){
                    isValid = false;    
                }
                
                protocolDescritpion = description.getFieldText();
                if(protocolDescritpion == null || protocolDescritpion.length() == 0){
                    isValid = false;    
                }
                
                protocolLevel = level.getFieldChoser() + MIN_LEVEL;
                if(protocolLevel < MIN_LEVEL || protocolLevel > MAX_LEVEL){
                    isValid = false;    
                }
                
                if(isValid){
                    if(CreateModifyCommunicationProtocol.this.protocol != null){
                       CreateModifyCommunicationProtocol.this.protocol.setName(protocolName);
                       CreateModifyCommunicationProtocol.this.protocol.setDescription(protocolDescritpion);
                       CreateModifyCommunicationProtocol.this.protocol.setLevel(protocolLevel);
                       //Modify in DB
                    }else{
                       CommunicationProtocol newProtocol = new CommunicationProtocol(protocolName,
                               protocolLevel, protocolDescritpion);
                       NetworkVisualizer.DB.addCommunicationProtocol(newProtocol);
                    }
                    
                    CreateModifyCommunicationProtocol.this.setVisible(false);
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
    
    private String[] generateValues(){
        String[] values;
        int length = (MAX_LEVEL - MIN_LEVEL) + 1;
        values = new String[length];
        
        for (int i = 0; i < values.length; i++) {
            values[i] = String.valueOf(i + MIN_LEVEL);
        }
        
        return values;
    }
}
