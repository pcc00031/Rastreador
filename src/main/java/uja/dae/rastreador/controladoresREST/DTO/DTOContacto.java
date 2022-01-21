/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uja.dae.rastreador.controladoresREST.DTO;

import uja.dae.rastreador.entidades.Contacto;
import uja.dae.rastreador.entidades.Usuario;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Pedro
 */
public class DTOContacto implements Serializable {
    LocalDateTime fecha_cont;
    UUID contacto;

    public DTOContacto() {
    }

    public DTOContacto(LocalDateTime fecha_cont,
                       UUID contacto) {
        this.fecha_cont = fecha_cont;
        this.contacto = contacto;
    }

    public DTOContacto(Contacto contacto) {
        this.contacto = contacto.getUsuarioContactado().getUuid();
        this.fecha_cont = contacto.getFecha_contacto();
    }

    public LocalDateTime getFecha_cont() {
        return fecha_cont;
    }

    public void setFecha_cont(LocalDateTime fecha_cont) {
        this.fecha_cont = fecha_cont;
    }

    public UUID getContacto() {
        return contacto;
    }

    public void setContacto(UUID contacto) {
        this.contacto = contacto;
    }

    public Contacto aContacto() {
        Contacto c = new Contacto();
        c.getUsuarioContactado().setUuid(contacto);
        c.setFecha_contacto(fecha_cont);
        return c;
    }
}
