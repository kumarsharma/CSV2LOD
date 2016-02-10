/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import csv2rdf.CLDataType;

/**
 *
 * @author ksharma
 */
public class DataTypeCellRenderer extends DefaultTableCellRenderer
{
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column)
    {
        if (value instanceof CLDataType) 
        {
                CLDataType cd = (CLDataType) value;
                setText(cd.toString());
        }

//        if (isSelected) {
//                setBackground(table.getSelectionBackground());
//        } else {
//                setBackground(table.getSelectionForeground());
//        }

        return this;
    }

}
