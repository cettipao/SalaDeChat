package chatserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Vector;
import sun.security.util.Length;

/**
 *
 * @author nicolas.fanin
 */
public class ChatServer implements Runnable {

    public final static int MAX_CONEXIONES = 40;

    public int puerto = 2000;
    private int numConexiones = 0;
    private Vector conexiones = null;

    //Constructor que recibe el puerto por parametro.
    public ChatServer(String puerto) {
        if (puerto != null) {
            try {
                this.puerto = Integer.parseInt(puerto);
            } catch (NumberFormatException nfe) {
            }
        }
    }

    public static void main(String[] args) {
        String puerto = null;

        //Se puede recibir el puerto a escuchar.
        if (args.length > 0) {
            puerto = args[0];
        }

        ChatServer cs = new ChatServer(puerto);

        cs.iniciar();
    }

    public void iniciar() {
        this.conexiones = new Vector(MAX_CONEXIONES);

        System.out.println("El servidor escucha en el puerto " + puerto);

        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        ServerSocket socketServer = null;

        Socket socketCliente;

        //Se crea el socket que escucha el servidor
        try {
            System.out.println("Intentando inciar el servidor");
            socketServer = new ServerSocket(puerto, 5);
        } catch (IOException e) {
            System.out.println("Error: no se pudo abrir el puerto " + puerto);
            e.printStackTrace();
            System.exit(-1);
        }

        System.out.println("Servidor iniciado en puerto " + puerto);

        //Ejecutar el ciclo escuchar/aceptar por siempre
        while (true) {
            try {
                //Esperar hasta que se reciba una conexion.
                socketCliente = socketServer.accept();
                procesarConexion(socketCliente);
            } catch (Exception e) {
                System.out.println("Imposible crear socket cliente.");
                e.printStackTrace();
            }
        }
    }

    public void procesarConexion(Socket socketCliente) {
        synchronized (this) {
            //si se llego al máximo de conexiones, bloquear el
            //hilo de recepción hasta que haya un lugar.

            while (numConexiones == MAX_CONEXIONES) {
                try {
                    wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            numConexiones++;
        }
        ConexionCliente con = new ConexionCliente(socketCliente);
        Thread t = new Thread(con);
        t.start();
        conexiones.addElement(con);
    }

    public synchronized void conexionCerrada(ConexionCliente conexion) {
        conexiones.removeElement(conexion);
        numConexiones--;
        notify();
    }
    
    public void enviarMesajeClientes(String cliente, String mensaje){
        Enumeration cons = conexiones.elements();
        
        while(cons.hasMoreElements()){
            ConexionCliente c = (ConexionCliente) cons.nextElement();
            c.enviarMensaje(mensaje);
        }
        System.out.println(" ### " + cliente + " : " + mensaje);
    }
    
    /**
     * Clase interna que provee el código para manejar un cliente 
     * que se ejecutará en un hilo independiente.
     */
    class ConexionCliente implements Runnable {
        
        private Socket socketCliente = null;
        private PrintWriter flujoSalida = null;
        private BufferedReader flujoEntrada = null;
        
        public ConexionCliente(Socket s) {
            socketCliente = s;
        }
        
        public void run() {
            OutputStream socketSalida = null;
            InputStream socketEntrada = null;
            String nombreCliente = null;
            String mensajeInicio;
            String mensajeCliente;
            InetAddress direccion;
            
            try {
                socketSalida = socketCliente.getOutputStream();                
                flujoSalida = new PrintWriter(new OutputStreamWriter(socketSalida));
                
                socketEntrada = socketCliente.getInputStream();                
                flujoEntrada = new BufferedReader(new InputStreamReader(socketEntrada));
                
                direccion = socketCliente.getInetAddress();
                nombreCliente = direccion.getHostName();
                
                mensajeInicio = "Nueva Conexión desde: " + nombreCliente + "\n\tSaluden todos!";
                enviarMesajeClientes(nombreCliente, mensajeInicio);
                
                System.out.println("Nueva conexión iniciada desde " + nombreCliente);
                System.out.println("Cantidad de Conexiones: " + numConexiones);
                
                mensajeCliente = null;               
                
                while((mensajeCliente = flujoEntrada.readLine()) != null){
                    enviarMesajeClientes(nombreCliente, mensajeCliente);
                }                 
            } catch(Exception e) {
            } finally {
                try {
                    if(flujoEntrada != null){
                        flujoEntrada.close();                        
                    }
                    if(flujoSalida != null){
                        flujoSalida.close();
                    }
                    socketCliente.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                conexionCerrada(this);
                enviarMesajeClientes(nombreCliente, nombreCliente + " se retiró de la sala...");
                System.out.println("Conexión cerrada desde: " + nombreCliente); 
                System.out.println("Cantidad de Conexiones: " + numConexiones);
            }
        }
        
        public void enviarMensaje(String mensaje) {
            try{
                flujoSalida.println(mensaje);
                flujoSalida.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        
    }

}
