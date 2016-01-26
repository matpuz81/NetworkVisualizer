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
public class ServiceListModel extends AbstractTableModel {

    private final String[] columnNames = {"COD",
                        "Service",
                        "Description",
                        "Permission", "Cost"};
    
    private ArrayList<Service> serviceList = new ArrayList<Service>();
    
    public ServiceListModel() {
        updateServiceList();
    }

    public ArrayList<Service> getServiceList() {
        return serviceList;
    }
    
    @Override
    public int getRowCount() {
        return serviceList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Service service = this.serviceList.get(rowIndex);
        switch(columnIndex) {
            case 0:
                return service.getCOD();
            case 1:
                return service.getService();
            case 2:
                return service.getDescription();
            case 3:
                return service.getPermission();
            case 4:
                return service.getCost();
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
    
    public void updateServiceList() {
        this.serviceList = NetworkVisualizer.DB.getAllService();
    }
}
