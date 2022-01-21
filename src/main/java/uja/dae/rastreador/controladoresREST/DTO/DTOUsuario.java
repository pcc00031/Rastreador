/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uja.dae.rastreador.controladoresREST.DTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import uja.dae.rastreador.entidades.Usuario;

import java.io.Serializable;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Pedro
 */
public class DTOUsuario implements Serializable {
    String telefono;
    UUID uuid;

    public DTOUsuario() {

    }

    public DTOUsuario(String telefono) {
        this.telefono = telefono;
        Random r = new Random();
        r.setSeed(Long.parseLong(telefono));
        UUID uuid = new UUID(r.nextLong(), r.nextLong());

        this.uuid = uuid;
    }

    public DTOUsuario(Usuario usuario) {
        this.telefono = usuario.getTelefono();
        this.uuid = usuario.getUuid();
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Usuario aUsuario() {
        return new Usuario(telefono);
    }

    public String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(DTORastreador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
