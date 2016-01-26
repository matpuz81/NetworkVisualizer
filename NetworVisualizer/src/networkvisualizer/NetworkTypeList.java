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
public class NetworkTypeList extends JDialog {
    
    private JTable table;
    private JButton newType, modify, delete;
    private NetworkTypeListModel model;
    
    
    public NetworkTypeList() {
        this.setTitle("Newtork type List");
        this.setSize(500,520);
        this.setModal(true);
        
        NetworkTypeListener listener = new NetworkTypeListener();

        JPanel panel = new JPanel();
        
        model = new NetworkTypeListModel();
        table = new JTable(model);
        JScrollPane scrollpane = new JScrollPane(table);
        panel.add(scrollpane);
        
        newType = new JButton("New");
        newType.setPreferredSize(new Dimension(100,25));
        newType.addActionListener(listener);
        panel.add(newType);
        
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
        this.model.updateNetworkTypeList();
        this.model.fireTableDataChanged();
    }
    
    private class NetworkTypeListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource().equals(newType)) {
                CreateModifyNetworkType create = new CreateModifyNetworkType(null);
            } else if(e.getSource().equals(modify)) {
                int index = table.getSelectedRow();
                if(index >= 0){
                    NetworkType networkType = NetworkTypeList.this.model.getNetworkTypeList().get(index);
                    CreateModifyNetworkType modify = new CreateModifyNetworkType(networkType);    
                }
                
            } else if(e.getSource().equals(delete)) {
                int response = JOptionPane.showConfirmDialog(null, "Are you sure?", "Confirm",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(response == JOptionPane.YES_OPTION){
                    int index = table.getSelectedRow();
                    if(index >= 0){
                        NetworkType networkType = NetworkTypeList.this.model.getNetworkTypeList().get(index);
                        NetworkVisualizer.DB.deleteNetworkType(networkType);
                        
                    }
                }
            }
            updateTable();
        }
        
    }
    
}
