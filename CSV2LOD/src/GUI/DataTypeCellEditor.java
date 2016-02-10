/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import csv2rdf.CLDataType;

/**
 *
 * @author ksharma
 */
public class DataTypeCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener
{
    public CLDataType dataType;
    private final List<CLDataType> listDataType;
    
    public DataTypeCellEditor(List<CLDataType> ld) 
    {
        this.listDataType = ld;
    }
	
    @Override
    public Object getCellEditorValue() 
    {
        return this.dataType;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) 
    {
        if (value instanceof CLDataType) 
        {
            this.dataType = (CLDataType) value;
        }
        JComboBox<CLDataType> comboDT = new JComboBox<>();

        this.listDataType.stream().forEach((aCLDataType) -> {
            comboDT.addItem(aCLDataType);
        });

        comboDT.setSelectedItem(this.dataType);
        comboDT.addActionListener(this);
        return comboDT;
    }

    @Override
    public void actionPerformed(ActionEvent event)
    {
        JComboBox<CLDataType> comboCLDataType = (JComboBox<CLDataType>) event.getSource();
        this.dataType = (CLDataType) comboCLDataType.getSelectedItem();
    }
}
