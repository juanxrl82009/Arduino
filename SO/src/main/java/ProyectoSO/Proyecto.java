
package ProyectoSO;
import com.panamahitek.ArduinoException;
import com.panamahitek.PanamaHitek_Arduino;
import com.panamahitek.PanamaHitek_DataBuffer;
import com.panamahitek.PanamaHitek_MultiMessage;
import java.awt.Color;
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
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
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
    
    double puntoHumedad,puntoTemperatura;
    double i=1;
    
    
    
    
      XYSeries series = new XYSeries("Humedad");
    XYDataset juegoDatos= new XYSeriesCollection(series);
    
    XYSeries series1 = new XYSeries("Temperatura");
    XYDataset juegoDatos1= new XYSeriesCollection(series1);
                        
    JFreeChart chart = ChartFactory.createXYLineChart("Gráfica de Humedad",
                       "Segundos (Cada 10 Seg)","Humedad HR",juegoDatos,PlotOrientation.VERTICAL,
                        false,
                        false,
                        true                // Show legend
                        );
    
    JFreeChart chart1 = ChartFactory.createXYLineChart("Gráfica de Temperatura",
                       "Segundos (Cada 10 Seg)","Temperatura °C",juegoDatos1,PlotOrientation.VERTICAL,
                        false,
                        false,
                        true                // Show legend
                        );
    
 
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
        jScrollPane2.getViewport().setBackground(Color.WHITE);
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
                        jLabel4.setText("Humedad Actual: "+multi.getMessage(1));
                        temp=multi.getMessage(0);
                        hum=multi.getMessage(1);
                        agregar();
                        
                        /*
                        for(double i = 0.1; i < 20.1; i= i+0.1){
                        perdida = a+b*Math.log10(i*1000)+c;
                         series.add(i,perdida);
                        }
                        
                        */
                        
                        //series.clear();
                        //for(double j=0.1;j<20.1;j=j+1){
                            puntoHumedad =Double.parseDouble(hum);
                            puntoTemperatura=Double.parseDouble(temp);
                            series.add(i,puntoHumedad);
                            series1.add(i,puntoTemperatura);
                            i=i+1;
                            
                            
                            
                        //}
                        
  
                        ChartPanel Grafica = new ChartPanel(chart);        
                        panelGrafica.setLayout(new java.awt.BorderLayout());
                        panelGrafica.add(Grafica);   
                        panelGrafica.validate();
                        
                        ChartPanel Grafica1 = new ChartPanel(chart1);        
                        panelTemp.setLayout(new java.awt.BorderLayout());
                        panelTemp.add(Grafica1);   
                        panelTemp.validate();
                       
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
        panelGrafica = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        panelTemp = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));

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
        jTable1.setFuenteHead(new java.awt.Font("Decker", 0, 18)); // NOI18N
        jTable1.setGridColor(new java.awt.Color(255, 255, 255));
        jTable1.setRowHeight(25);
        jTable1.setRowMargin(0);
        jScrollPane2.setViewportView(jTable1);

        jPanel5.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 460, 240));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 26, Short.MAX_VALUE)
        );

        jPanel5.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 700, 20, 30));

        jLabel1.setFont(new java.awt.Font("Decker", 0, 36)); // NOI18N
        jPanel5.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 410, 410, 30));

        jButton1.setBackground(new java.awt.Color(0, 112, 192));
        jButton1.setFont(new java.awt.Font("Decker", 0, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Exportar a Excel");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 550, 300, 40));

        jLabel2.setFont(new java.awt.Font("Decker", 0, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setText("Aplicación de Temperatura y Humedad");
        jPanel5.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 20, -1, -1));

        javax.swing.GroupLayout panelGraficaLayout = new javax.swing.GroupLayout(panelGrafica);
        panelGrafica.setLayout(panelGraficaLayout);
        panelGraficaLayout.setHorizontalGroup(
            panelGraficaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 570, Short.MAX_VALUE)
        );
        panelGraficaLayout.setVerticalGroup(
            panelGraficaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 260, Short.MAX_VALUE)
        );

        jPanel5.add(panelGrafica, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 140, 570, 260));

        jLabel3.setFont(new java.awt.Font("Decker", 0, 18)); // NOI18N
        jLabel3.setText("Información en tiempo real");
        jPanel5.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, -1));

        jLabel4.setFont(new java.awt.Font("Decker", 0, 36)); // NOI18N
        jPanel5.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 460, 440, 30));

        javax.swing.GroupLayout panelTempLayout = new javax.swing.GroupLayout(panelTemp);
        panelTemp.setLayout(panelTempLayout);
        panelTempLayout.setHorizontalGroup(
            panelTempLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 570, Short.MAX_VALUE)
        );
        panelTempLayout.setVerticalGroup(
            panelTempLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 260, Short.MAX_VALUE)
        );

        jPanel5.add(panelTemp, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 420, 570, 260));

        jButton2.setBackground(new java.awt.Color(0, 112, 192));
        jButton2.setFont(new java.awt.Font("Decker", 0, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Enviar datos al GMAIL");
        jPanel5.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 670, 300, 40));

        jButton3.setBackground(new java.awt.Color(0, 112, 192));
        jButton3.setFont(new java.awt.Font("Decker", 0, 18)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Ver base de datos");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 610, 300, 40));

        getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1270, 730));

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

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        vistaBase vBase= new vistaBase();
    }//GEN-LAST:event_jButton3ActionPerformed

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
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane2;
    private rojerusan.RSTableMetro jTable1;
    private javax.swing.JPanel panelGrafica;
    private javax.swing.JPanel panelTemp;
    // End of variables declaration//GEN-END:variables
}
