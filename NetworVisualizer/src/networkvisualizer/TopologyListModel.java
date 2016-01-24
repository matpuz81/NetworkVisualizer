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
public class TopologyListModel extends AbstractTableModel {

    private final String[] columnNames = {"Name",
                        "Structure"};
    
    private ArrayList<NetworkTopology> topologyList = new ArrayList<NetworkTopology>();
    
    public TopologyListModel() {
        NetworkTopology topology = new NetworkTopology("Star", "Centralized...");
        topologyList.add(topology);
        updateTopologyList();
    }
    
    @Override
    public int getRowCount() {
        return topologyList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        NetworkTopology topology = this.topologyList.get(rowIndex);
        switch(columnIndex) {
            case 0:
                return topology.getName();
            case 1:
                return topology.getStructure();
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
    
    public void updateTopologyList() {
        // Update userlist from DB
    }
}
