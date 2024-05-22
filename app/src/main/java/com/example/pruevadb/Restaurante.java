package com.example.pruevadb;

import java.io.Serializable;
import java.util.ArrayList;

public class Restaurante implements Serializable {

    private String id;
    private String nombre;
    private String tipo;
    private String ciudad;
    private String dniUsuario;
    private String imagen;
    private int Comensales;
    private ArrayList<Reserva>reservas;
    private ArrayList<String>horastdesayuno;
    private ArrayList<String>horastcomida;
    private ArrayList<String>horastcena;
    private ArrayList<String>turnos;



    public Restaurante() {
    }

    public Restaurante(String id, String nombre, String tipo, String ciudad, String dniUsuario, String imagen, int comensales, ArrayList<Reserva> reservas) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.ciudad = ciudad;
        this.dniUsuario = dniUsuario;
        this.imagen = imagen;
        Comensales = comensales;
        this.reservas = reservas;
    }

    public Restaurante(ArrayList<Reserva> reservas) {
    }


    public  String getId(){return id;}

    public void setId(String id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDniUsuario() {
        return dniUsuario;
    }

    public void setDniUsuario(String dniUsuario) {
        this.dniUsuario = dniUsuario;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getComensales() {
        return Comensales;
    }

    public void setComensales(int comensales) {
        Comensales = comensales;
    }

    public ArrayList<String> getHorastdesayuno() {
        return horastdesayuno;
    }

    public void setHorastdesayuno(ArrayList<String> horastdesayuno) {
        this.horastdesayuno = horastdesayuno;
    }

    public ArrayList<String> getHorastcomida() {
        return horastcomida;
    }

    public void setHorastcomida(ArrayList<String> horastcomida) {
        this.horastcomida = horastcomida;
    }

    public ArrayList<String> getHorastcena() {
        return horastcena;
    }

    public void setHorastcena(ArrayList<String> horastcena) {
        this.horastcena = horastcena;
    }

    public ArrayList<String> getTurnos() {
        return turnos;
    }

    public void setTurnos(ArrayList<String> turnos) {
        this.turnos = turnos;
    }

    public ArrayList<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(ArrayList<Reserva> reservas) {
        this.reservas = reservas;
    }

}
