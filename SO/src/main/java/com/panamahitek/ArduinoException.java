
package com.panamahitek;


public class ArduinoException extends Exception {

    private static String portName;
    private static String methodName;
    private static String exceptionType;

    /**
     * Mensajes a desplegar ante las diferentes excepciones que se pueden dar
     * durante la ejecución del código
     */
    final public static String TYPE_PORT_ALREADY_OPENED = "El puerto que intenta abrir está siendo utilizado por otro dispositivo";
    final public static String TYPE_PORT_NOT_OPENED = "Puerto no abierto";
    final public static String TYPE_RXTX_EXCEPTION = "No se puede iniciar la conexión con Arduino 2 veces";
    final public static String TYPE_NO_ARDUINO_AT_PORT = "No se ha encontrado ningún Arduino conectado a este puerto. Verifique el puerto en el que está conectado Arduino";
    final public static String TYPE_NO_SERIAL_PORT = "No se ha encontrado ningún Arduino conectado a este ordenador. Por favor conecte Arduino a la PC mediante USB";
    final public static String TYPE_PORT_USED_BY_OTHER_APP = "Imposible conectar. Este puerto posee una conexión con otra aplicación";
    final public static String TYPE_SEND_DATA = "No es posible utilizar este método si se ha iniciado la conexión a Arduino con el método arduinoRX(), el cual es sólo para recibir datos";
    final public static String TYPE_SEND_DATA_ERROR = "Error en el envío de datos";
    final public static String TYPE_WRONG_SEND_DATA_CONNECTION = "No se puede utilizar el método sendData() si se ha iniciado la conexión Arduino con el método arduinoRX(), el cual es sólo para recibir datos";
    final public static String TYPE_NO_ARDUINO_CONNECTION = "No se ha establecido conexión con Arduino. Por favor utilice alguno de los métodos arduinoRX(), arduinoTX() o arduinoRXTX()";
    final public static String TYPE_KILL_ARDUINO_CONNECTION = "No se puede finalizar la conexión con Arduino si la misma no se ha iniciado";
    final public static String TYPE_CLOSE_PORT = "Error al finalizar la conexión con Ardino";
    final public static String TYPE_RECEIVE_DATA = "No se puede utilizar el método receiveData() si se ha iniciado la conexión Arduino con el método ArduinoTX(), el cual es sólo para recibir datos";
    final public static String TYPE_NO_EVENT_LISTENER = "No se ha agregado un EventListener a la clase PanamaHitek_Arduino";

    /**
     *
     * @param portName Nombre del puerto COM
     * @param methodName Método que dispara la excepción
     * @param exceptionType Tipo de excepción
     */
    public ArduinoException(String portName, String methodName, String exceptionType) {
        super("Nomrbe del puerto - " + portName + "; Nombre del método - " + methodName + "; Tipo de excepción - " + exceptionType + ".");
        this.portName = portName;
        this.methodName = methodName;
        this.exceptionType = exceptionType;
    }

    /**
     * Get the value of exceptionType
     *
     * @return the value of exceptionType
     */
    public static String getExceptionType() {
        return exceptionType;
    }

    /**
     * Set the value of exceptionType
     *
     * @param exceptionType new value of exceptionType
     */
    public static void setExceptionType(String exceptionType) {
        ArduinoException.exceptionType = exceptionType;
    }

    /**
     * Get the value of methodName
     *
     * @return the value of methodName
     */
    public static String getMethodName() {
        return methodName;
    }

    /**
     * Set the value of methodName
     *
     * @param methodName new value of methodName
     */
    public static void setMethodName(String methodName) {
        ArduinoException.methodName = methodName;
    }

    /**
     * Get the value of portName
     *
     * @return the value of portName
     */
    public static String getPortName() {
        return portName;
    }

    /**
     * Set the value of portName
     *
     * @param portName new value of portName
     */
    public static void setPortName(String portName) {
        ArduinoException.portName = portName;
    }

}
