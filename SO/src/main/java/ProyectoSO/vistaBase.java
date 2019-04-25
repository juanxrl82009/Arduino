/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProyectoSO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author John Castro
 */
public class vistaBase extends javax.swing.JFrame {
     private Conexio con;
     
     
    Connection cn;
    Statement st;
    ResultSet rs;
    DefaultTableModel modelo;
    int id;
   
    public vistaBase() {
        initComponents();
        setVisible(true);
        setLocationRelativeTo(null);
        listar();
    }
    
    void listar(){    
        modelo=(DefaultTableModel)tabla.getModel();
        /*Se almacena la consulta sql en un string*/
        String sql="SELECT * "+"FROM dato";
        try{
            /*se establece coneccion con la base de datos y se le introduce la consulta*/
            cn=con.getConnection();
            st=cn.createStatement();
            rs=st.executeQuery(sql);   
            Object[] Datos= new Object[7]; /*Un array donde se almacenan las filas de la tabla. el tamaño del
            array debe ser el numero de columnas que tenga nuestra consulta*/
            while(rs.next()){
            Datos[0]=rs.getInt("fecha");/*deben llamarse exactamente igual a como esta en la tabla*/
            Datos[1]=rs.getString("temperatura");/*deben llamarse exactamente igual a como esta en la tabla*/
            Datos[2]=rs.getString("humedad");
            modelo.addRow(Datos);
            }    
        }catch(SQLException e){}
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new rojerusan.RSTableMetro();
        jButton1 = new javax.swing.JButton();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Decker", 0, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setText("Base de Datos");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 10, -1, -1));

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Fecha", "Temperatura", "Humedad"
            }
        ));
        tabla.setFuenteFilas(new java.awt.Font("Decker", 0, 18)); // NOI18N
        tabla.setFuenteFilasSelect(new java.awt.Font("Decker", 0, 18)); // NOI18N
        tabla.setFuenteHead(new java.awt.Font("Decker", 0, 24)); // NOI18N
        tabla.setRowHeight(25);
        tabla.setRowMargin(0);
        jScrollPane1.setViewportView(tabla);
        if (tabla.getColumnModel().getColumnCount() > 0) {
            tabla.getColumnModel().getColumn(2).setResizable(false);
        }

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 670, 550));

        jButton1.setBackground(new java.awt.Color(0, 112, 192));
        jButton1.setFont(new java.awt.Font("Decker", 0, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Actualizar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 660, 190, 40));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 670, 730));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        listar();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(vistaBase.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(vistaBase.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(vistaBase.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(vistaBase.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new vistaBase().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private rojerusan.RSTableMetro tabla;
    // End of variables declaration//GEN-END:variables
}