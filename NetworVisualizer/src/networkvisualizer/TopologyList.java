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
public class TopologyList extends JDialog {
    
    private JTable table;
    private JButton newTopology, modifyTopology, deleteTopology;
    private TopologyListModel model;
    
    
    public TopologyList() {
        this.setTitle("Topology List");
        this.setSize(500,520);
        this.setModal(true);
        
        TopologyListener listener = new TopologyListener();

        JPanel panel = new JPanel();
        
        model = new TopologyListModel();
        table = new JTable(model);
        JScrollPane scrollpane = new JScrollPane(table);
        panel.add(scrollpane);
        
        newTopology = new JButton("New");
        newTopology.setPreferredSize(new Dimension(100,25));
        newTopology.addActionListener(listener);
        panel.add(newTopology);
        
        modifyTopology = new JButton("Modify");
        modifyTopology.setPreferredSize(new Dimension(100,25));
        modifyTopology.addActionListener(listener);
        panel.add(modifyTopology);
        
        deleteTopology = new JButton("Delete");
        deleteTopology.setPreferredSize(new Dimension(100,25));
        deleteTopology.addActionListener(listener);
        panel.add(deleteTopology);
        
        this.add(panel);
        
        this.setVisible(true);
    }
    
    public void updateTable() {
        this.model.updateTopologyList();
        this.model.fireTableDataChanged();
    }
    
    private class TopologyListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource().equals(newTopology)) {
                CreateModifyTopology create = new CreateModifyTopology(null);
            } else if(e.getSource().equals(modifyTopology)) {
                int index = table.getSelectedRow();
                if(index >= 0){
                    //GetTopology from DB
                    //CreateModifyTopology modify = new CreateModifyTopology(//TOPOLOGY);    
                }
                
            } else if(e.getSource().equals(deleteTopology)) {
                int response = JOptionPane.showConfirmDialog(null, "Are you sure?", "Confirm",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(response == JOptionPane.YES_OPTION){
                    int index = table.getSelectedRow();
                    if(index >= 0){
                        //delete from DB
                        
                    }
                }
            }
            updateTable();
        }
        
    }
    
}
