package com.rogergcc.conductortaxi.model;

/**
 * Created by rogergcc on 16/02/2020.
 * Copyright Ⓒ 2020 . All rights reserved.
 */
public class Carrera {

    private String _id;
    private String socket_id_conductor;
    private String socket_id_cliente;
    private String nombre_conductor;
    private String hora_finalizacion;
    private String fecha_registro;

    public Carrera(String socket_id_conductor, String socket_id_cliente, String nombre_conductor, String hora_finalizacion, String fecha_registro) {
        this.socket_id_conductor = socket_id_conductor;
        this.socket_id_cliente = socket_id_cliente;
        this.nombre_conductor = nombre_conductor;
        this.hora_finalizacion = hora_finalizacion;
        this.fecha_registro = fecha_registro;
    }

    public String getHora_finalizacion() {
        return hora_finalizacion;
    }

    public void setHora_finalizacion(String hora_finalizacion) {
        this.hora_finalizacion = hora_finalizacion;
    }

    public String getNombre_conductor() {
        return nombre_conductor;
    }

    public void setNombre_conductor(String nombre_conductor) {
        this.nombre_conductor = nombre_conductor;
    }

    public String getSocket_id_conductor() {
        return socket_id_conductor;
    }

    public void setSocket_id_conductor(String socket_id_conductor) {
        this.socket_id_conductor = socket_id_conductor;
    }

    public String getSocket_id_cliente() {
        return socket_id_cliente;
    }

    public void setSocket_id_cliente(String socket_id_cliente) {
        this.socket_id_cliente = socket_id_cliente;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(String fecha_registro) {
        this.fecha_registro = fecha_registro;
    }
}
