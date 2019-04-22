
package ProyectoSO;
import com.panamahitek.ArduinoException;
import com.panamahitek.PanamaHitek_Arduino;
import com.panamahitek.PanamaHitek_DataBuffer;
import com.panamahitek.PanamaHitek_MultiMessage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import java.sql.SQLException;
import java.sql.Statement;
/**
 * Este ejemplo permite conectarse con Arduino y empezar a recibir datos. Estos
 * datos seran tabulados en un JTable y mostrados en pantalla. Al presionar el
 * boton disponible en la interfaz, los datos tabulados seran exportados a un
 * archivo en Excel.
 *
 * Para utilizar este ejemplo se requiere que en Arduino se haya subido el
 * ejemplo double_data_send.ino
 *
 * @author Antony García González, de Proyecto Panama Hitek. Visita
 * http://panamahitek.com
 */
public class Proyecto extends javax.swing.JFrame {
 
    DefaultTableModel modelo = new DefaultTableModel();
            
    //Objeto para la gestion de la conexion con Arduino
    PanamaHitek_Arduino ino = new PanamaHitek_Arduino();
    //Objeto para la gestion de multiples mensajes recibidos desde Arduino
    PanamaHitek_MultiMessage multi = new PanamaHitek_MultiMessage(2, ino);
    //Objeto para la gestion y almacenamiento de datos recibidos
    PanamaHitek_DataBuffer buffer = new PanamaHitek_DataBuffer();

    Conexio con = new Conexio();
    
       String temp;
        String hum;
    
        Connection cn;
        Statement st;
        ResultSet rs;
    
    public Proyecto() {
        initComponents();
        
        modelo.addColumn("Temperatura");
        modelo.addColumn("Humedad");
        //jTable1.setModel(buffer.getTable().getModel());
        String Datos[] = new String [2];
        
      

        /**
         * Se crea la tabla de datos, agregando 3 columnas. Se especifica la
         * posicion de la columna (0 es la más a la izquierda), el nombre de la
         * columna y el tipo de datos
         */
        buffer.addTimeColumn(0, "Tiempo");
        buffer.addColumn(1, "Temperatura", Double.class);
        buffer.addColumn(2, "Humedad", Double.class);
        //buffer.getTable().getModel();

        //Se inserta la tabla en un panel para poder verla en la interfaz
        buffer.insertToPanel(jPanel1);
        jPanel1.setVisible(false);

        /**
         * Con este objeto se gestiona la recepcion de datos. El evento
         * serialEvent se "disparara" cada vez que se reciba un dato desde
         * Arduino enviado a traves del puerto serie
         */
        SerialPortEventListener listener = new SerialPortEventListener() {
            @Override
            public void serialEvent(SerialPortEvent serialPortEvent) {
                try {
                    /**
                     * Este metodo indica cuando se haya terminado de recibir
                     * los datos de los DOS sensores especificados en la
                     * creacion del objeto "multi"
                     */
                    if (multi.dataReceptionCompleted()) {
                        /**
                         * Se agregan los valores al buffer, especificando el
                         * índice de la columna y el valor. Se utiliza el objeto
                         * "multi" para separar los datos recibidos desde
                         * Arduino, especificando el indice.
                         */

                        buffer.addValue(1, Double.parseDouble(multi.getMessage(0)));
                        buffer.addValue(2, Double.parseDouble(multi.getMessage(1)));
                        
                        /*multi.getMessage(0) es la temperatura y el otro la humedad, los estoy
                        guardando en un array y los estoy poniendo en otra tabla*/
                        
                        /*Datos[0]=multi.getMessage(0);
                        Datos[1]=multi.getMessage(1);
                        modelo.addRow(Datos);
                        */
                       jTable1.setModel(buffer.getTable().getModel());
                      
                        jLabel1.setText("Temperatura Actual: "+multi.getMessage(0));

                        temp=multi.getMessage(0);
                        hum=multi.getMessage(1);
                        agregar();
                       
                        //Se salta un "renglon" en el buffer de datos
                        buffer.printRow();
                        /**
                         * Se le indica al objeto multi que se ha terminado de
                         * imprimir los datos recibidos desde el Arduino y que
                         * puede prepararse para recibir un nuevo par de datos
                         * 
                         */
                
                        multi.flushBuffer();
                    }
                } catch (ArduinoException ex) {
                    Logger.getLogger(Proyecto.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SerialPortException ex) {
                    Logger.getLogger(Proyecto.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(Proyecto.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

        try {
            //Se inicia la conexion con el puerto COM21 a 9600 baudios
            ino.arduinoRX("COM8", 9600, listener);
        } catch (ArduinoException | SerialPortException ex) {
            Logger.getLogger(Proyecto.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
    public void agregar()
    {
    //Se le asigna a un string el insert en la base de datos/
         String sqlCliente="INSERT INTO dato (temperatura,humedad) VALUES"
                 +"('"+temp+"','"+hum+"');";
      
        try{
           //se establece coneccion con la base de datos y se le introduce la consulta/
            cn=con.getConnection();
            st=cn.createStatement();
            rs=st.executeQuery(sqlCliente); 
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

        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new rojerusan.RSTableMetro();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null}
            },
            new String [] {
                "Tiempo", "Temperatura", "Humedad"
            }
        ));
        jTable1.setFuenteFilas(new java.awt.Font("Decker", 0, 18)); // NOI18N
        jTable1.setFuenteFilasSelect(new java.awt.Font("Decker", 0, 18)); // NOI18N
        jTable1.setFuenteHead(new java.awt.Font("Decker", 0, 24)); // NOI18N
        jTable1.setRowHeight(25);
        jScrollPane2.setViewportView(jTable1);

        jPanel5.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 100, 600, 240));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 282, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 246, Short.MAX_VALUE)
        );

        jPanel5.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 360, -1, -1));

        jLabel1.setText("jLabel1");
        jPanel5.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 380, -1, -1));

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton1.setText("Exportar a Excel");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 420, -1, -1));

        jLabel2.setFont(new java.awt.Font("Decker", 0, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setText("Aplicacion de Temperatura y Humedad");
        jPanel5.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, -1, -1));

        getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 760, 620));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        /**
        * Este boton permite finalizar la conexion con Arduino y exportar los
        * datos a Excel
        */
        try {
            //Se exportan los datos a Excel
            buffer.exportExcelFile();
            //Se finaliza la conexion con Arduino
            ino.killArduinoConnection();
            //Se muestra un mensaje
            JOptionPane.showMessageDialog(this, "Conexión con Arduino Finalizada");
            //Se cierra el programa
            System.exit(0);
        } catch (ArduinoException | IOException ex) {
            Logger.getLogger(Proyecto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Metal".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Proyecto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Proyecto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Proyecto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Proyecto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Proyecto().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane2;
    private rojerusan.RSTableMetro jTable1;
    // End of variables declaration//GEN-END:variables
}
