package main;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import javax.swing.JTextArea;

/**
 *
 * @author nicolas.fanin
 */
public class ClienteChat {

    //Referncia al socket de conexión con el servidor
    private Socket conexion = null;
    //Dirección IP del servidor de chat.
    private String ipServidor = "127.0.0.1";
    //Puerto TCP del servidor de chat (chat room)
    private int puertoServidor = 2000;

    // Flujo de Entradade caracteres desde el servidor.
    private BufferedReader flujoEntrada = null;
    // Flujo de salida de caracteres hacia el servidor.
    private PrintStream flujoSalida = null;
    
    private JTextArea taConversacion;

    public ClienteChat(String direccionIP, String puerto, JTextArea taConversacion) {
        
        this.taConversacion = taConversacion;

        if (direccionIP != null) {
            ipServidor = direccionIP;
        }

        if (puerto != null) {
            try {
                puertoServidor = Integer.parseInt(puerto);
            } catch (NumberFormatException nfe) {
            }
        }
    }
    
    public void conectar() {
        try {
            // se abre un socket a la dirección IP y puerto indicado .
            conexion = new Socket(ipServidor, puertoServidor);
            
            // se crea un lector de caracteres para todo lo que se reciba
            // desde el servidor por el socket.
            flujoEntrada = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            // se crea un flujo para enviar texto al servidor.
            flujoSalida = new PrintStream(conexion.getOutputStream());
            
            // se inicia un ciclo de lectura infinito.
            Thread t = new Thread(new LectorRemoto());
            t.start();
            
        } catch (Exception e){
            String except = "No se pudo abrir el socket " + ipServidor + ":" + puertoServidor;
            System.out.println(except);
            this.taConversacion.setText("Error en establecer conexion: \n"+except);
            e.printStackTrace();
            //System.exit(-1);
        }        
    }
    
    public void EnviarMensaje(String usuario, String mensaje) {
        flujoSalida.println(usuario + ": " + mensaje);
    }
    
    private class LectorRemoto implements Runnable {
        
        public void run() {
            // se hace un ciclo infinito leyendo todas las líneas 
            // que se vayan recibiendo del servidor.
            int chatters = 2;
            while(true){
                try{
                    String mensaje = flujoEntrada.readLine();
                    System.out.println(mensaje);
                    if (chatters>0){
                        taConversacion.append(mensaje);
                        taConversacion.append("\n");
                        chatters--;
                    }
                    else{
                        /*
                        taConversacion.append(mensaje);
                        taConversacion.append("\n");
*/
                        System.out.println("Chatter");
                        appendConColores(mensaje);
                    }
                } catch (Exception e) {
                    System.out.println("Error leyendo del servidor");
                    e.printStackTrace();
                    break;
                }
            }
        }
        public void appendConColores(String mensaje){
            char[] mensajeEnChar = mensaje.toCharArray();
            boolean username = true;
            for (char c : mensajeEnChar) {
                if (username) {
                    String caracter = "" + c;
                    taConversacion.setForeground(Color.red);
                    taConversacion.append(caracter.substring(0));
                    if (c == ':') {
                        username = false;
                        System.out.println("Falseado");
                    }
                } else {
                    String caracter = "" + c;
                    taConversacion.setForeground(Color.BLACK);
                    taConversacion.append(caracter.substring(0));

                }

                
            }
            taConversacion.append("\n");
            
            
        }
    }
    

}
