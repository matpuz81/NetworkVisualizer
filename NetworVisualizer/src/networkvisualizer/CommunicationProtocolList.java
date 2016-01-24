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
public class CommunicationProtocolList extends JDialog {
    
    private JTable table;
    private JButton newProtocol, modify, delete;
    private CommunicationProtocolListModel model;
    
    
    public CommunicationProtocolList() {
        this.setTitle("Communication Protocol List");
        this.setSize(500,520);
        this.setModal(true);
        
        ProtocolListener listener = new ProtocolListener();

        JPanel panel = new JPanel();
        
        model = new CommunicationProtocolListModel();
        table = new JTable(model);
        JScrollPane scrollpane = new JScrollPane(table);
        panel.add(scrollpane);
        
        newProtocol = new JButton("New Protocol");
        newProtocol.setPreferredSize(new Dimension(100,25));
        newProtocol.addActionListener(listener);
        panel.add(newProtocol);
        
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
        this.model.updateProtocolList();
        this.model.fireTableDataChanged();
    }
    
    private class ProtocolListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource().equals(newProtocol)) {
                CreateModifyCommunicationProtocol create = new CreateModifyCommunicationProtocol(null);
            } else if(e.getSource().equals(modify)) {
                int index = table.getSelectedRow();
                if(index >= 0){
                    //GetProtocol from DB
                    //CreateModifyCommunicationProtocol modify = new CreateModifyCommunicationProtocol(//PROTOCOL);    
                }
                
            } else if(e.getSource().equals(delete)) {
                int response = JOptionPane.showConfirmDialog(null, "Are you sure?", "Confirm",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(response == JOptionPane.YES_OPTION){
                    int index = table.getSelectedRow();
                    if(index >= 0){
                        //delete from DB
                        updateTable();
                    }
                }
            }
        }
        
    }
    
}
