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
public class CreateModifyService extends JDialog {
    
    private TextFieldPanel COD;
    private InputFieldPanel service, cost, description;
    private ChoserFieldPanel permission;
    private JButton done;
    private Service serv;
    private static final String[] PERMISSION_VALUES = {"Full", "Restricted"};
    
    public CreateModifyService(Service serv) {
        this.serv = serv;
        if(serv == null){
            this.setTitle("Create Service");
        } else {
            this.setTitle("Modify Service");
        }
        this.setSize(300,500);
        this.setModal(true);
        
        JPanel panel = new JPanel();
        
        if(serv != null) {
            COD = new TextFieldPanel("Service COD:", serv.getCOD());
            panel.add(COD);
        }
        
        service = new InputFieldPanel("Service:");
        if(serv != null) {
            service.setFieldText(serv.getService());
        }
        panel.add(service);
        
        description = new InputFieldPanel("Description:");
        if(serv != null) {
            description.setFieldText(serv.getDescription());
        }
        panel.add(description);
        
        cost = new InputFieldPanel("Cost:");
        if(serv != null) {
            cost.setFieldText(String.valueOf(serv.getCost()));
        }
        panel.add(cost);
        
        permission = new ChoserFieldPanel("Permission:", PERMISSION_VALUES);
        if(serv != null && serv.getPermission() != null) {
            permission.setFieldChoser(serv.getPermission());
        }
        panel.add(permission);
        
        done = new JButton("Done");
        done.setPreferredSize(new Dimension(100,25));
        done.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String serviceName, serviceDescritpion, servicePermission, serviceCostText;
                float serviceCost = 0f;
                boolean isValid = true;
                
                serviceName = service.getFieldText();
                if(serviceName == null || serviceName.length() == 0){
                    isValid = false;    
                }
                
                serviceDescritpion = description.getFieldText();
                if(serviceDescritpion == null || serviceDescritpion.length() == 0){
                    isValid = false;    
                }
                
                serviceCostText = cost.getFieldText();
                if(serviceCostText == null || serviceCostText.length() == 0){
                    isValid = false;    
                }else{
                    try {
                        serviceCost = Float.parseFloat(serviceCostText);   
                    }catch(NumberFormatException ex){
                        isValid = false;
                    }
                }
                
                servicePermission = permission.getFieldChoserString();
                if(servicePermission == null || servicePermission.length() == 0){
                    isValid = false;    
                }
                
                if(isValid){
                    if(CreateModifyService.this.serv != null){
                       CreateModifyService.this.serv.setService(serviceName);
                       CreateModifyService.this.serv.setDescription(serviceDescritpion);
                       CreateModifyService.this.serv.setCost(serviceCost);
                       CreateModifyService.this.serv.setPermission(servicePermission);
                       //Modify in DB
                    }else{
                       Service newService = new Service();
                       newService.setService(serviceName);
                       newService.setDescription(serviceDescritpion);
                       newService.setCost(serviceCost);
                       newService.setPermission(servicePermission);
                       NetworkVisualizer.DB.addService(newService);
                    }
                    
                    CreateModifyService.this.setVisible(false);
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
