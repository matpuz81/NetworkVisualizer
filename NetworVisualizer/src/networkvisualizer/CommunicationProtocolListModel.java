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
public class CommunicationProtocolListModel extends AbstractTableModel {

    private final String[] columnNames = {"ID",
                        "Name",
                        "Description",
                        "Level"};
    
    private ArrayList<CommunicationProtocol> protocolList = new ArrayList<CommunicationProtocol>();
    
    public CommunicationProtocolListModel() {
        updateProtocolList();
    }
    
    public ArrayList<CommunicationProtocol> getProtocolList(){
        return protocolList;
    }
    
    @Override
    public int getRowCount() {
        return protocolList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        CommunicationProtocol protocol = this.protocolList.get(rowIndex);
        switch(columnIndex) {
            case 0:
                return protocol.getId();
            case 1:
                return protocol.getName();
            case 2:
                return protocol.getDescription();
            case 3:
                return protocol.getLevel();
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
    
    public void updateProtocolList() {
        this.protocolList = NetworkVisualizer.DB.getAllCommunicationProtocol();
    }
}
