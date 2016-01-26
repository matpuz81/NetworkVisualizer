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
public class UserList extends JDialog {
    
    private JTable table;
    private JButton newUser, modifyUser, deleteUser;
    private UserListModel model;
    
    
    public UserList() {
        this.setTitle("User List");
        this.setSize(500,520);
        this.setModal(true);
        
        UserListener listener = new UserListener();

        JPanel panel = new JPanel();
        
        model = new UserListModel();
        table = new JTable(model);
        JScrollPane scrollpane = new JScrollPane(table);
        panel.add(scrollpane);
        
        newUser = new JButton("New");
        newUser.setPreferredSize(new Dimension(100,25));
        newUser.addActionListener(listener);
        panel.add(newUser);
        
        modifyUser = new JButton("Modify");
        modifyUser.setPreferredSize(new Dimension(100,25));
        modifyUser.addActionListener(listener);
        panel.add(modifyUser);
        
        deleteUser = new JButton("Delete");
        deleteUser.setPreferredSize(new Dimension(100,25));
        deleteUser.addActionListener(listener);
        panel.add(deleteUser);
        
        this.add(panel);
        
        this.setVisible(true);
    }
    
    public void updateTable() {
        this.model.updateUserList();
        this.model.fireTableDataChanged();
    }
    
    private class UserListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource().equals(newUser)) {
                CreateModifyUser create = new CreateModifyUser(null);
            } else if(e.getSource().equals(modifyUser)) {
                int index = table.getSelectedRow();
                if(index >= 0){
                    User user = UserList.this.model.getUserList().get(index);
                    CreateModifyUser modify = new CreateModifyUser(user);    
                }
                
            } else if(e.getSource().equals(deleteUser)) {
                int response = JOptionPane.showConfirmDialog(null, "Are you sure?", "Confirm",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(response == JOptionPane.YES_OPTION){
                    int index = table.getSelectedRow();
                    if(index >= 0){
                        User user = UserList.this.model.getUserList().get(index);
                        NetworkVisualizer.DB.deleteUser(user);
                    }
                }
            }
            updateTable();
        }
        
    }
    
}
