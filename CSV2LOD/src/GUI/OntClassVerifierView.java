/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import com.hp.hpl.jena.vocabulary.XSD;
import csv2rdf.CSVClass;
import csv2rdf.CSVHeader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import csv2rdf.CLDataType;

import csv2rdf.CSV2RDF;
import java.awt.Cursor;
/**
 *
 * @author ksharma
 */
public class OntClassVerifierView extends javax.swing.JFrame implements PropertyDataSource {

    /**
     * Creates new form OntClassVerifierView
     */
    public CSVClass[] csvClasses;
    public CSVClass currentSelectedClass;
    CSV2RDF controller;
    
    public OntClassVerifierView(CSV2RDF con)
    {
        initComponents();   
        this.controller = con;
    }
    
    public void showGraph()
    {
        this.classCombo.removeAllItems();
        
        if(this.csvClasses.length > 0)
        {
            for(CSVClass c: this.csvClasses)
            {
                this.classCombo.addItem(c);
            }
            
            this.currentSelectedClass = (CSVClass)this.classCombo.getSelectedItem();
        }
        
        this.fillupPropertyTable();
    }

    private void fillupPropertyTable()
    {
        ArrayList<CSVHeader> ar = new ArrayList<>();
        ar.addAll(Arrays.asList(this.currentSelectedClass.loadHeaders()));
        PropertyTableModel model = new PropertyTableModel(ar);
        model.dataSource = this;
        
        List<CLDataType> lds = new ArrayList<>();
        lds.add(new CLDataType(XSD.xboolean));
        lds.add(new CLDataType(XSD.xbyte));
        lds.add(new CLDataType(XSD.xdouble));
        lds.add(new CLDataType(XSD.xfloat));
        lds.add(new CLDataType(XSD.xint));
        lds.add(new CLDataType(XSD.xlong));
        lds.add(new CLDataType(XSD.xshort));
        lds.add(new CLDataType(XSD.xstring));
        lds.add(new CLDataType(XSD.date));
        lds.add(new CLDataType(XSD.dateTime));
        lds.add(new CLDataType(XSD.decimal));
        
        this.propertyTable.setModel(model);
        this.propertyTable.setDefaultRenderer(CLDataType.class, new DataTypeCellRenderer());
        this.propertyTable.setDefaultEditor(CLDataType.class, new DataTypeCellEditor(lds));
        this.propertyTable.setRowHeight(25);
    }
    
    @Override
    public ArrayList<CSVHeader> getDataItems()
    {
        ArrayList<CSVHeader> ar = new ArrayList<>();
        ar.addAll(Arrays.asList(this.currentSelectedClass.loadHeaders()));
        
        return ar;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        classCombo = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        propertyTable = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtClassName = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Verify Models");
        setLocationByPlatform(true);
        setResizable(false);
        setType(java.awt.Window.Type.UTILITY);
        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Verify Classes");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(1, 1, 700, 30);

        classCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                classComboItemStateChanged(evt);
            }
        });
        getContentPane().add(classCombo);
        classCombo.setBounds(10, 70, 240, 30);

        jLabel2.setText("Edit Properties");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(330, 44, 360, 20);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Properties"));

        propertyTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Header Name", "Data Type", "Has Relationship"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(propertyTable);
        if (propertyTable.getColumnModel().getColumnCount() > 0) {
            propertyTable.getColumnModel().getColumn(0).setResizable(false);
            propertyTable.getColumnModel().getColumn(1).setResizable(false);
            propertyTable.getColumnModel().getColumn(2).setResizable(false);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 545, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(jPanel1);
        jPanel1.setBounds(320, 60, 570, 340);

        jLabel3.setText("Files");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(14, 50, 230, 14);

        jLabel4.setText("Class Name");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(5, 120, 70, 14);
        getContentPane().add(txtClassName);
        txtClassName.setBounds(80, 110, 170, 30);

        jButton1.setText("Cancel & Close");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(480, 400, 140, 40);

        jButton2.setText("Filter Concepts");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2);
        jButton2.setBounds(640, 400, 130, 40);

        jButton3.setText("Finish");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3);
        jButton3.setBounds(790, 400, 90, 40);

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        this.dispose();
        this.controller.UserCanceledOperation();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void classComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_classComboItemStateChanged
        
        if(this.classCombo.getItemCount()<=0)
            return;
        
        if(this.txtClassName.getText().length() > 0)
        {
            this.currentSelectedClass.uderDefinedClassName = this.txtClassName.getText();
        }
        
        CSVClass item = (CSVClass)this.classCombo.getSelectedItem();
        this.currentSelectedClass = item;
        this.txtClassName.setText(item.getClassName());
        this.fillupPropertyTable();
    }//GEN-LAST:event_classComboItemStateChanged

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       
        Cursor c = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
        this.setCursor(c);
        this.setEnabled(false);
        this.controller.FilterConcepts();
        
        this.setEnabled(true);
        c = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
        this.setCursor(c);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
       
        if(this.txtClassName.getText().length() > 0)
        {
            this.currentSelectedClass.uderDefinedClassName = this.txtClassName.getText();
        }
        
        this.dispose();
        this.controller.GenerateFinalConcept();
    }//GEN-LAST:event_jButton3ActionPerformed

    // <editor-fold>
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox classCombo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable propertyTable;
    private javax.swing.JTextField txtClassName;
    // End of variables declaration//GEN-END:variables
//</editor-fold>
}
