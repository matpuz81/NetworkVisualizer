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
public class CreateModifyTopology extends JDialog {
    
    private TextFieldPanel nameModify;
    private InputFieldPanel nameCreate, structure;
    private JButton done;
    private NetworkTopology topology;
    
    public CreateModifyTopology(NetworkTopology topology) {
        this.topology = topology;
        if(topology == null){
            this.setTitle("Create Topology");
        } else {
            this.setTitle("Modify Topology");
        }
        this.setSize(300,500);
        this.setModal(true);
        
        JPanel panel = new JPanel();
        
        if(topology != null) {
            nameModify = new TextFieldPanel("Name:", topology.getName());
            panel.add(nameModify);
        }else{
            nameCreate = new InputFieldPanel("Name:");
            panel.add(nameCreate);
        }
        
        structure = new InputFieldPanel("Structure:");
        if(topology != null) {
            structure.setFieldText(topology.getStructure());
        }
        panel.add(structure);
        
        done = new JButton("Done");
        done.setPreferredSize(new Dimension(100,25));
        done.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String name, structure;
                boolean isValid = true;
                
                if(CreateModifyTopology.this.topology == null){
                    name = nameCreate.getFieldText();
                }else{
                    name = nameModify.getValue();
                }
                structure = CreateModifyTopology.this.structure.getFieldText();
                
                if(name == null || name.length() == 0){
                    isValid = false;    
                }
                
                if(structure == null || structure.length() == 0){
                    isValid = false;
                }
                
                if(isValid == true){
                    if(CreateModifyTopology.this.topology == null){
                        NetworkTopology topology = new NetworkTopology(name, structure);
                        //Insert in DB
                    }else{
                        CreateModifyTopology.this.topology.setStructure(structure);
                        //Modify in DB
                    }
                    CreateModifyTopology.this.setVisible(false);
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
