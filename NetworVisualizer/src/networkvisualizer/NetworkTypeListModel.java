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
public class NetworkTypeListModel extends AbstractTableModel {

    private final String[] columnNames = {"ID",
                        "Description"};
    
    private ArrayList<NetworkType> networkTypeList = new ArrayList<NetworkType>();
    
    public NetworkTypeListModel() {
        updateNetworkTypeList();
    }
    
    @Override
    public int getRowCount() {
        return networkTypeList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        NetworkType type = this.networkTypeList.get(rowIndex);
        switch(columnIndex) {
            case 0:
                return type.getId();
            case 1:
                return type.getDescription();
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
    
    public void updateNetworkTypeList() {
        // Update userlist from DB
    }
}
