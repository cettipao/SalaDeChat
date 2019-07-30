/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saladechat;

/**
 *
 * @author cetti
 */
public class Usuario {
    private String user;
    private String pass;
    private String ip;
    private String puerto;

    public Usuario(String user, String pass, String ip, String puerto) {
        this.user = user;
        this.pass = pass;
        this.ip = ip;
        this.puerto = puerto;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPuerto() {
        return puerto;
    }

    public void setPuerto(String puerto) {
        this.puerto = puerto;
    }

    @Override
    public String toString() {
        return "Usuario{" + "user=" + user + ", pass=" + pass + ", ip=" + ip + ", puerto=" + puerto + '}';
    }

    
    
    
    
}
