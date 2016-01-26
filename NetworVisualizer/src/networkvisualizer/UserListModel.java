/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkvisualizer;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Jessica
 */
public class UserListModel extends AbstractTableModel {

    private final String[] columnNames = {"ID",
                        "Username",
                        "Type",
                        "Service Usage"};
    
    private ArrayList<User> userList = new ArrayList<User>();
    
    public UserListModel() {
        updateUserList();
        userList.add(new User(1, "Test", true));
        userList.add(new User(1, "Test", true));
        userList.add(new User(1, "Test", true));
        userList.add(new User(1, "Test", true));
        userList.add(new User(1, "Test", true));
        userList.add(new User(1, "Test", true));
        
    }
    
    public ArrayList<User> getUserList(){
        return userList;
    }
    
    @Override
    public int getRowCount() {
        return userList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        User user = this.userList.get(rowIndex);
        switch(columnIndex) {
            case 0:
                return user.getUserID();
            case 1:
                return user.getUsername();
            case 2:
                if(user.isIsAdmin())
                    return "Admin";
                else
                    return "Normal";
            case 3:
                return user.getUsage();
            default:
                return "";
        }
    }
    
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    
    public void updateUserList() {
        this.userList = NetworkVisualizer.DB.getAllUser();
    }
}
