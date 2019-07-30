/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saladechat;

import java.util.ArrayList;


/**
 *
 * @author cetti
 */
public class GestorDeUsuario {
    private ArrayList<Usuario> users;

    public GestorDeUsuario(int usuarioActual, ArrayList users) {
        this.users = users;
    }

    public GestorDeUsuario() {
        users = new ArrayList<>();
    }

    public ArrayList<Usuario> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<Usuario> users) {
        this.users = users;
    }
    
    public void addUser(Usuario user){
        users.add(user);
    }

    
    
 
    
    
    
}
