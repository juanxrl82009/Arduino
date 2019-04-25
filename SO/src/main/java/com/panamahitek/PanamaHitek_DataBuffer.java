
package com.panamahitek;

import com.panamahitek.events.DataInsertionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.panamahitek.events.DataInsertionListener;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 * Esta clase ha sido diseñada para gestionar el almacenamiento de datos en
 * hojas de cálculo de Excel de manera fácil y rápida
 *
 * @author Antony García González, de Proyecto Panama Hitek. Visita
 * http://panamahitek.com
 */
public class PanamaHitek_DataBuffer {

    private List<List<Object>> mainBuffer;
    private List<String> variableList;
    private List<Object> classList;
    private int ROW_COUNT = 0;
    private String SHEET_NAME = "Arduino_log";
    private JTable table;
    private JScrollPane scroll;
    private boolean tableFlag = false;

    private boolean listenerFlag = false;
    private boolean timeFlag = false;
    private int timeColumn = 0;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private ArrayList listeners;

    TableModelListener tableModelListener = new TableModelListener() {
        @Override
        public void tableChanged(TableModelEvent tme) {

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(() -> {
                try {
                    Thread.sleep(10);
                    table.repaint();
                    table.getTableHeader().repaint();
                    table.scrollRectToVisible(table.getCellRect(table.getRowCount(), 0, true));
                    table.repaint();
                    table.getTableHeader().repaint();
                    executor.shutdown();
                    executor.awaitTermination(100, TimeUnit.MILLISECONDS);
                } catch (InterruptedException ex) {
                    Logger.getLogger(PanamaHitek_DataBuffer.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    };

    /**
     * Constructor de la clase
     */
    public PanamaHitek_DataBuffer() {
        mainBuffer = new ArrayList<>();
        variableList = new ArrayList<>();
        classList = new ArrayList<>();
        listeners = new ArrayList();
    }

    /**
     * Permite agregar una columna al buffer de datos
     *
     * @param index El índice (posicion) de la columna
     * @param variableName El nombre que se desea asignar a la variable
     * @param dataType Tipo de datos a guardar en la columna
     */
    public void addColumn(int index, String variableName, Object dataType) {
        variableList.add(index, variableName);
        classList.add(dataType);
        List<Object> list = new ArrayList<>();
        mainBuffer.add(list);
    }

    /**
     * Permite agregar una columna de registro de tiempo al buffer de datos
     *
     * @param index El índice (posicion) de la columna de tiempo
     * @param variableName El nombre que se desea asignar a la variable
     */
    public void addTimeColumn(int index, String variableName) {
        variableList.add(index, variableName);
        classList.add(new Date());
        List<Object> list = new ArrayList<>();
        this.timeColumn = index;
        this.timeFlag = true;
        mainBuffer.add(list);
    }

    /**
     * Permite establecer el formato de la fecha. Por ejemplo, si se quiere que
     * la fecha tenga el formato de hora, minutos y segundos, se debe enviar
     * como parámetro new SimpleDateFormat("HH:mm:ss")
     *
     * @param format Formato que se desea establecer
     *
     * @see SimpleDateFormat
     */
    public void addDateFormat(SimpleDateFormat format) {
        this.dateFormat = format;
    }

    /*
     * Provoca un salto de línea en el buffer de datos. Se utiliza cuando se 
     * haya terminado de guardar la información para un instante dado
     */
    public void printRow() {

        if (timeFlag) {
            mainBuffer.get(timeColumn).add(ROW_COUNT, dateFormat.format(new Date()));
        }
        ROW_COUNT++;
        for (int i = 0; i < mainBuffer.size(); i++) {
            if (mainBuffer.get(i).size() != ROW_COUNT) {
                while (mainBuffer.get(i).size() != ROW_COUNT) {
                    Object columnValue = classList.get(i);
                    if ((columnValue instanceof String) || (columnValue.equals(String.class))) {
                        mainBuffer.get(i).add("");
                    } else if ((columnValue instanceof Boolean) || (columnValue.equals(Boolean.class))) {
                        mainBuffer.get(i).add(null);
                    } else if ((columnValue instanceof Date) || (columnValue.equals(Date.class))) {
                        mainBuffer.get(i).add(dateFormat.format(new Date()));
                    } else if ((columnValue instanceof Integer) || (columnValue.equals(Integer.class))) {
                        mainBuffer.get(i).add(0);
                    } else if ((columnValue instanceof Long) || (columnValue.equals(Long.class))) {
                        mainBuffer.get(i).add(0);
                    } else if ((columnValue instanceof Float) || (columnValue.equals(Float.class))) {
                        mainBuffer.get(i).add(0);
                    } else if ((columnValue instanceof Double) || (columnValue.equals(Double.class))) {
                        mainBuffer.get(i).add(0);
                    }
                }
            }
        }

        if (tableFlag) {
            Object[] row = new Object[variableList.size()];
            for (int i = 0; i < variableList.size(); i++) {
                row[i] = mainBuffer.get(i).get(ROW_COUNT - 1);
            }
            ((DefaultTableModel) table.getModel()).addRow(row);
        }

        if (listenerFlag) {
            triggerDataInsertionEvent();
        }

    }

    /**
     * Agrega nuevos valores al buffer de datos
     *
     * @param column Columna en la que se desea almacenar el valor
     * @param value Valor a almacenar
     * @throws Exception Se produce si se escoje una columna que no ha sido
     * definida o si se intenta guardar un valor diferente al del tipo de datos
     * establecidos para la columna
     */
    public void addValue(int column, Object value) throws Exception {
        if (column > variableList.size()) {
            throw new Exception("El parametro 'column' no puede ser mayor a la "
                    + "cantidad de columnas declaradas");
        }

        mainBuffer.get(column).add(ROW_COUNT, value);
    }

    /**
     *
     * @return Cantidad de filas almacenadas en el buffer
     */
    public int getRowCount() {
        return ROW_COUNT;
    }

    /**
     *
     * @return Cantidad de columnas del buffer
     */
    public int getColumnCount() {
        return variableList.size();
    }

    /**
     *
     * @param sheetName Nombre de la hoja en el libro de Excel
     */
    public void setSheetName(String sheetName) {
        this.SHEET_NAME = sheetName;
    }

    /**
     * Abre una ventana emergente para escoger la direccion en la cual se quiere
     * almacenar la hoja de datos de Excel
     */
    public void exportExcelFile() throws FileNotFoundException, IOException {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getDefaultDirectory());
        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            String path = selectedFile.getAbsolutePath();
            if (!path.endsWith(".xlsx")) {
                path += ".xlsx";
            }
            FileOutputStream outputStream = new FileOutputStream(path);
            XSSFWorkbook workbook = buildSpreadsheet();
            workbook.write(outputStream);
        }

    }

    /**
     * Crea y almacena la hoja de Excel con los datos del buffer en una
     * ubicacion específica
     *
     * @param path Ubicacion en la que se desea almacenar el fichero
     */
    public void exportExcelFile(String path) {
        try {
            FileOutputStream outputStream = new FileOutputStream(path);
            XSSFWorkbook workbook = buildSpreadsheet();
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Construye la hoja de Excel
     *
     * @return Instancia de la clase XSSFWorkbook con los datos almacenados en
     * el buffer de datos
     */
    private XSSFWorkbook buildSpreadsheet() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        for (int i = 0; i <= mainBuffer.get(0).size(); i++) {
            Row row = sheet.createRow(i);
            for (int j = 0; j < variableList.size(); j++) {

                Cell cell = row.createCell(j);

                if (i == 0) {
                    cell.setCellValue((String) variableList.get(j));
                } else {
                    Object value = classList.get(j);
                    if ((value instanceof String) || (value.equals(String.class))) {
                        cell.setCellValue((String) mainBuffer.get(j).get(i - 1));
                    } else if ((value instanceof Boolean) || (value.equals(Boolean.class))) {
                        cell.setCellValue((Boolean) mainBuffer.get(j).get(i - 1));
                    } else if ((value instanceof Date) || (value.equals(Date.class))) {
                        cell.setCellValue((String) mainBuffer.get(j).get(i - 1));
                    } else if ((value instanceof Integer) || (value.equals(Integer.class))) {
                        cell.setCellValue((Integer) mainBuffer.get(j).get(i - 1));
                    } else if ((value instanceof Long) || (value.equals(Long.class))) {
                        cell.setCellValue((Long) mainBuffer.get(j).get(i - 1));
                    } else if ((value instanceof Float) || (value.equals(Float.class))) {
                        cell.setCellValue((Float) mainBuffer.get(j).get(i - 1));
                    } else if ((value instanceof Double) || (value.equals(Double.class))) {
                                              cell.setCellValue((Double) mainBuffer.get(j).get(i - 1));
                    }
                }
            }
        }
        return workbook;
    }

    /**
     * Construye un JTable y la inserta dentro de un JPanel
     *
     * @param panel JPanel donde se desea insertar la tabla
     */
    public void insertToPanel(JPanel panel) {
        buildTable();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        scroll = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);;
        scroll.setViewportView(table);
        scroll.setVisible(true);
        scroll.setBounds(0, 0, panel.getWidth(), panel.getHeight() - 25);
        tableFlag = true;
        panel.add(scroll);
    }

    /**
     *
     * @return JTable creado dentro de la clase
     */
    public JTable getTable() {
        buildTable();
        return this.table;
    }

    /**
     *
     * @return JScrollPane con la tabla de datos contenida en su interior
     */
    public JScrollPane getScrollPane() {
        buildTable();
        scroll = new javax.swing.JScrollPane();
        scroll.setViewportView(table);
        scroll.setVisible(true);
        return this.scroll;
    }

    /**
     * Construye la JTable donde se almacenan los datos
     */
    private void buildTable() {
        if (!tableFlag) {
            table = new JTable();
            table.setRowHeight(30);
            String[] headerTitles = new String[variableList.size()];
            Object[][] tableContent = new Object[mainBuffer.get(0).size()][variableList.size()];
            for (int i = 0; i < variableList.size(); i++) {
                headerTitles[i] = variableList.get(i);
                for (int j = 0; j < mainBuffer.get(i).size(); j++) {
                    tableContent[j][i] = mainBuffer.get(i).get(j);
                }
            }
            table.setModel(new DefaultTableModel(tableContent, headerTitles));
            DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
            renderer.setHorizontalAlignment(0);
            table.getTableHeader().setReorderingAllowed(false);
            ((DefaultTableModel) table.getModel()).addTableModelListener(tableModelListener);
        }
    }

    /**
     * Agrega el evento DataInsertionListener
     *
     * @param listener Instancia de la clase DataInsertionListener
     */
    public void addEventListener(DataInsertionListener listener) {
        listeners.add(listener);
        listenerFlag = true;
    }

    /**
     * Elimina el eventListener
     */
    public void removeEventListener() {
        listeners.remove(listeners.size() - 1);
        listenerFlag = false;
    }

    /**
     * Disparador de evento onDataInsertion
     */
    private void triggerDataInsertionEvent() {
        ListIterator li = listeners.listIterator();
        while (li.hasNext()) {
            DataInsertionListener listener = (DataInsertionListener) li.next();
            DataInsertionEvent readerEvObj = new DataInsertionEvent(this, this);
            (listener).onDataInsertion(readerEvObj);
        }
    }

    /**
     *
     * @return Lista de clases almacenadas en las columnas del databuffer
     */
    public List<Object> getClassList() {
        return classList;
    }

    /**
     *
     * @return Lista de los nombres de las columnas declaradas
     */
    public List<String> getVariableList() {
        return variableList;
    }

    /**
     *
     * @return Buffer de datos
     */
    public List<List<Object>> getMainBuffer() {
        return mainBuffer;
    }

    /**
     *
     * @return Indice de la columna de tiwmpo
     */
    public int getTimeColumn() {
        return timeColumn;
    }    
    
    }
