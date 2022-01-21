/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uja.dae.rastreador.controladoresREST.DTO;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Pedro
 */
public class DTONotificacion implements Serializable {
    boolean positivo;
    LocalDateTime fecha;

    public DTONotificacion() {
    }

    public DTONotificacion(boolean positivo, LocalDateTime fecha) {
        this.positivo = positivo;
        this.fecha = fecha;
    }

    public boolean isPositivo() {
        return positivo;
    }

    public void setPositivo(boolean positivo) {
        this.positivo = positivo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
