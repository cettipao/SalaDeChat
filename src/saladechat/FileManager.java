/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saladechat;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author cetti
 */
public class FileManager {
    private Gson gson;
    private GestorDeUsuario gestorDeUsuario;

    public FileManager() {
        this.gson = new Gson();
    }
    
    public void CrearGestorDeUsuarioDesdeJson(String path){
        try{
        File archivo = new File(path);
        FileReader fr = new FileReader(archivo);
        BufferedReader br = new BufferedReader(fr);
        String linea = br.readLine();
        GestorDeUsuario obj = gson.fromJson(linea, GestorDeUsuario.class);
        gestorDeUsuario = obj;
        }
        catch(IOException e){
            System.out.println(e);
        } 
    }
    
    public void EscrituraDeArchivo(){
        //Escritura de Archivos
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter("src/Json/usersData.txt");
            pw = new PrintWriter(fichero);

            //Conversion de clase usuario a String tipo JSON
            String JSON = gson.toJson(this.gestorDeUsuario);
            pw.println(JSON);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Nuevamente aprovechamos el finally para 
                // asegurarnos que se cierra el fichero.
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        this.CrearGestorDeUsuarioDesdeJson("src/Json/usersData.txt");

    }
    
    
    /*
    public Object CrearObjetoDesdeJson(Object object,String path){
        try{
        File archivo = new File(path);
        FileReader fr = new FileReader(archivo);
        BufferedReader br = new BufferedReader(fr);
        String linea = br.readLine();
        Object obj = gson.fromJson(linea, object.getClass());
        return obj;
        }
        catch(IOException e){
            System.out.println(e);
            return null;
        } 
    }
*/

    public GestorDeUsuario getGestorDeUsuario() {
        return gestorDeUsuario;
    }
    
    
}
