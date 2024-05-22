package com.example.pruevadb;

import java.io.Serializable;
import java.util.ArrayList;

public class Usuario implements Serializable {
    private String nombreUsuario;
    private String correo;
    private String contraseña;
    private String dni;
    private String tipUser;
    private ArrayList<String> mensajes;

    private ArrayList<String> notificaciones;

    public Usuario() {
    }

    public Usuario(String nombreUsuario, String correo, String contraseña,String dni, String tipUser) {
        this.nombreUsuario = nombreUsuario;
        this.correo = correo;
        this.contraseña = contraseña;
        this.dni=dni;
        this.tipUser = tipUser;
    }




    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTipUser() {
        return tipUser;
    }

    public void setTipUser(String tipUser) {
        this.tipUser = tipUser;
    }

    public ArrayList<String> getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(ArrayList<String> notificaciones) {
        this.notificaciones = notificaciones;
    }

    public ArrayList<String> getMensajes() {
        return mensajes;
    }

    public void setMensajes(ArrayList<String> mensajes) {
        this.mensajes = mensajes;
    }
}