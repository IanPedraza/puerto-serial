package puerto.serie;

import javax.comm.CommPortIdentifier;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import javax.comm.*;
import listeners.onConsoleListener;

public class ControladorPuertos implements SerialPortEventListener{
    
    private Enumeration ports;
    private SerialPort serialPort;
    
    //BUFFER
    private OutputStream output;
    private BufferedReader input;
    private InputStream inputStream;
    
    //ARREGLOS
    private final ArrayList<Puerto> lista_puertos;
    private final ArrayList<String> lista;
    
    //LISTENERS
    private final onConsoleListener listener;
    
    public ControladorPuertos(onConsoleListener listener) {
        this.listener = listener;
        this.lista_puertos = new ArrayList();
        this.lista = new ArrayList();
    }
    
    public ArrayList<String> obtenerLista(){
        ports = CommPortIdentifier.getPortIdentifiers();
        
        lista_puertos.removeAll(lista_puertos);
        lista.removeAll(lista);
                
        while (ports.hasMoreElements()) {
            CommPortIdentifier port = (CommPortIdentifier) ports.nextElement();
                      
            if(port.getPortType() == CommPortIdentifier.PORT_SERIAL){
                lista_puertos.add(new Puerto(port, port.getName()));
                lista.add(port.getName());                
            }
           
        }
        
        return lista;
    }
    
    public boolean abrirPuerto(int index, int numero_baudios){
        return abrirPuerto(lista_puertos.get(index).getPuertoSerie(), numero_baudios);
    }
    
    public void cerrarConexion(){
        serialPort.close();
        listener.onRecieve("CONEXIÓN CERRADA\n");
    }
    
    private boolean abrirPuerto(CommPortIdentifier portId, int numero_baudios){
        try {
            serialPort = (SerialPort) portId.open(this.getClass().getName(), 1);
            
            serialPort.setSerialPortParams(numero_baudios, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
         
            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            output = serialPort.getOutputStream();
            inputStream =serialPort.getInputStream();
            
            serialPort.addEventListener((SerialPortEventListener) this);
            serialPort.notifyOnDataAvailable(true);
         
            listener.onRecieve(portId.getName() + " ABIERTO\n**********************\n");
            return true;
        } catch (PortInUseException | UnsupportedCommOperationException | IOException | TooManyListenersException e) {
            System.err.println(e.toString());
            listener.onRecieve("Error: Ocurrió un error al abrir el puerto\n");
            return false;
        }
    }

    @Override
    public void serialEvent(SerialPortEvent spe) {
        switch (spe.getEventType()) {
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
                
            case SerialPortEvent.DATA_AVAILABLE:
                byte[] readBuffer = new byte[20];

                try {
                    while (inputStream.available() > 0) {
                        int numBytes = inputStream.read(readBuffer);
                    }
                    
                    listener.onRecieve(new String(readBuffer));                    
                } catch (Exception e) {
                    System.err.println(e.toString());
                }
                
                break;
        }
    }
    
    public void enviarMensaje(String cadena){
        try {
            cadena += "\n";
            
            for(int i = 0; i < cadena.length(); i++){
                output.write(cadena.charAt(i));
            }            
        } catch (IOException ex) {
            System.err.println(ex.toString());
        }
    }
 
}
