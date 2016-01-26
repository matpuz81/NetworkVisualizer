/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package networkvisualizer;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
/**
 *
 * @author Jessica
 */
public class NetworkVisualizerTableCellRenderer extends DefaultTableCellRenderer {

    private Color currentColor = Color.white;
    
    public NetworkVisualizerTableCellRenderer() {
        setOpaque(true);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean selected, boolean focused, int row, int col) {
        if(row %2 == 0) {
            currentColor = new Color(255, 178, 102);
        } else {
            currentColor = new Color(255, 204, 153);
        }
        
        if(selected) {
            super.setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
        } else {
            super.setForeground(Color.black);
            super.setBackground(currentColor);
        }
        setFont(table.getFont());
        setValue(value);
        return this;
    }
}
