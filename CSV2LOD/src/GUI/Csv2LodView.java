/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.io.File;
import java.net.URL;
import javax.swing.JFileChooser;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import csv2rdf.CSV2RDF;
import csv2rdf.ConversionListener;

/**
 *
 * @author ksharma
 */
public class Csv2LodView extends javax.swing.JFrame implements ConversionListener {

    File[] inputFiles;
    URL baseUrl = null;
    boolean IsInProgress;
    
    private boolean CanDoAction()
    {
        return !this.IsInProgress;
    }
    
    public Csv2LodView() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        fileList = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtOutDirectoryName = new javax.swing.JTextField();
        checkLinkResource = new javax.swing.JCheckBox();
        checkNeedReview = new javax.swing.JCheckBox();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtBaseURL = new javax.swing.JTextField();
        txtDomainName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        checkDetectDatatype = new javax.swing.JCheckBox();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Tabular Data to RDF (Linked Data)");
        setBackground(new java.awt.Color(204, 204, 255));
        setResizable(false);
        getContentPane().setLayout(null);

        jLabel1.setText("Input Files");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 30, 60, 20);

        jScrollPane1.setViewportView(fileList);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(70, 60, 170, 150);

        jButton1.setText("Select");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(70, 30, 170, 23);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Destination Directory");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(270, 30, 150, 14);

        txtOutDirectoryName.setEditable(false);
        txtOutDirectoryName.setText("Select");
        txtOutDirectoryName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtOutDirectoryNameMouseClicked(evt);
            }
        });
        txtOutDirectoryName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOutDirectoryNameActionPerformed(evt);
            }
        });
        getContentPane().add(txtOutDirectoryName);
        txtOutDirectoryName.setBounds(430, 30, 380, 20);

        checkLinkResource.setText("Link Resources?");
        getContentPane().add(checkLinkResource);
        checkLinkResource.setBounds(430, 150, 270, 23);

        checkNeedReview.setText("Need Review?");
        getContentPane().add(checkNeedReview);
        checkNeedReview.setBounds(430, 180, 260, 23);

        jButton2.setText("Run");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2);
        jButton2.setBounds(380, 370, 110, 50);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Base URL");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(340, 70, 80, 14);

        txtBaseURL.setText("http://test.csv2lod.com");
        getContentPane().add(txtBaseURL);
        txtBaseURL.setBounds(430, 70, 380, 20);

        txtDomainName.setText("BaseBall");
        getContentPane().add(txtDomainName);
        txtDomainName.setBounds(430, 110, 380, 20);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Domain Name");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(320, 110, 100, 14);

        checkDetectDatatype.setText("Detect Datatype?");
        getContentPane().add(checkDetectDatatype);
        checkDetectDatatype.setBounds(430, 210, 260, 23);

        jMenu1.setText("File");

        jMenuItem1.setText("New Job");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Exit");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        if(!this.CanDoAction())
            return;
        
        JFileChooser _fileChooser = new JFileChooser();
        _fileChooser.setMultiSelectionEnabled(true);
        int retval = _fileChooser.showDialog(null, "Select CSV File");
        if (retval == JFileChooser.APPROVE_OPTION) 
        {
            this.inputFiles = _fileChooser.getSelectedFiles();
        }
        
        if(this.inputFiles.length > 0)
        {
            DefaultListModel m = new DefaultListModel();
            for(File f:this.inputFiles)
                m.addElement(f);
            
            this.fileList.setModel(m);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtOutDirectoryNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOutDirectoryNameActionPerformed
        
        if(!this.CanDoAction())
            return;
        
        JFileChooser _fileChooser = new JFileChooser();
        _fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int retval = _fileChooser.showDialog(null, "Select Output Directory");
        if (retval == JFileChooser.APPROVE_OPTION) 
        {
            this.txtOutDirectoryName.setText(_fileChooser.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_txtOutDirectoryNameActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       
        if(!this.CanDoAction())
            return;
        
        if(this.inputFiles == null || this.inputFiles.length <= 0)
        {
            JOptionPane.showMessageDialog(null, "No files selected!!");
            return;
        }
        
        String dom_str = this.txtDomainName.getText();
        if(dom_str.length() <= 0)
                dom_str = "DefaulDomain";
        
        String url_str = this.txtBaseURL.getText();
        try{
            this.baseUrl = new URL(url_str);
        }catch(java.net.MalformedURLException e){
        
            JOptionPane.showMessageDialog(null, "Not a valid URL. Please re-enter base URL.");
            return;
        }
        
        CSV2RDF cr = new CSV2RDF(this.inputFiles, dom_str, this.txtOutDirectoryName.getText(), this.baseUrl);
        cr.ShouldLinkResource = this.checkLinkResource.isSelected();
        cr.ShouldRefineConcepts = this.checkNeedReview.isSelected();
        cr.ShouldDetectType = this.checkDetectDatatype.isSelected();
        
        cr.actor = this;
        this.IsInProgress = true;
        cr.Start();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtOutDirectoryNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtOutDirectoryNameMouseClicked
        
        if(!this.CanDoAction())
            return;
        
        JFileChooser _fileChooser = new JFileChooser();
        _fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int retval = _fileChooser.showDialog(null, "Select Output Directory");
        if (retval == JFileChooser.APPROVE_OPTION) 
        {
            this.txtOutDirectoryName.setText(_fileChooser.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_txtOutDirectoryNameMouseClicked

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        
        if(!this.CanDoAction())
            return;
        
        System.exit(1);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
      
        if(!this.CanDoAction())
            return;
        
        DefaultListModel m = (DefaultListModel)this.fileList.getModel();
        m.removeAllElements();
        this.inputFiles = null;
        this.txtBaseURL.setText("");
        this.txtDomainName.setText("");
        this.txtOutDirectoryName.setText("");
        
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        
        /*
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Csv2LodView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Csv2LodView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Csv2LodView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Csv2LodView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>*/

        Csv2LodView verifierView = new Csv2LodView();
        verifierView.setSize(900, 520);
        verifierView.setLocation(250, 120);
        verifierView.setVisible(true);
            
        
    }
    
    
    //Conversion listener
    @Override
    public void DidFinishConversion()
    {
        this.IsInProgress = false;
        JOptionPane.showMessageDialog(null, "Conversion Successfull. Check result in destination directory.");
    }
    @Override
    public void DidFinishConversionWithError(String errDesc)
    {
        this.IsInProgress = false;
        JOptionPane.showMessageDialog(null, "Conversion was not Successfull. Reason: "+errDesc);
    }
    
    
    
    
    
    
    
    
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox checkDetectDatatype;
    private javax.swing.JCheckBox checkLinkResource;
    private javax.swing.JCheckBox checkNeedReview;
    private javax.swing.JList fileList;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtBaseURL;
    private javax.swing.JTextField txtDomainName;
    private javax.swing.JTextField txtOutDirectoryName;
    // End of variables declaration//GEN-END:variables
}
