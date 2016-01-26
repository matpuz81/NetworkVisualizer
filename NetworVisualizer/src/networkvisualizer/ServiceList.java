/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkvisualizer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author Jessica
 */
public class ServiceList extends JDialog {
    
    private JTable table;
    private JButton newService, modify, delete;
    private ServiceListModel model;
    
    
    public ServiceList() {
        this.setTitle("Service List");
        this.setSize(500,520);
        this.setModal(true);
        
        ServiceListener listener = new ServiceListener();

        JPanel panel = new JPanel();
        
        model = new ServiceListModel();
        table = new JTable(model);
        table.setDefaultRenderer(Object.class, new NetworkVisualizerTableCellRenderer());
        JScrollPane scrollpane = new JScrollPane(table);
        panel.add(scrollpane);
        
        newService = new JButton("New");
        newService.setPreferredSize(new Dimension(100,25));
        newService.addActionListener(listener);
        panel.add(newService);
        
        modify = new JButton("Modify");
        modify.setPreferredSize(new Dimension(100,25));
        modify.addActionListener(listener);
        panel.add(modify);
        
        delete = new JButton("Delete");
        delete.setPreferredSize(new Dimension(100,25));
        delete.addActionListener(listener);
        panel.add(delete);
        
        this.add(panel);
        
        this.setVisible(true);
    }
    
    public void updateTable() {
        this.model.updateServiceList();
        this.model.fireTableDataChanged();
    }
    
    private class ServiceListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource().equals(newService)) {
                CreateModifyService create = new CreateModifyService(null);
            } else if(e.getSource().equals(modify)) {
                int index = table.getSelectedRow();
                if(index >= 0){
                    Service service = ServiceList.this.model.getServiceList().get(index);
                    CreateModifyService modify = new CreateModifyService(service);    
                }
                
            } else if(e.getSource().equals(delete)) {
                int response = JOptionPane.showConfirmDialog(null, "Are you sure?", "Confirm",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(response == JOptionPane.YES_OPTION){
                    int index = table.getSelectedRow();
                    if(index >= 0){
                        Service service = ServiceList.this.model.getServiceList().get(index);
                        //NetworkVisualizer.DB.deleteService(service);
                    }
                }
            }
            updateTable();
        }
        
    }
    
}
