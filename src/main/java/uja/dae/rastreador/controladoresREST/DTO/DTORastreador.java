package uja.dae.rastreador.controladoresREST.DTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.logging.Level;
import java.util.logging.Logger;
import uja.dae.rastreador.entidades.Rastreador;

/**
 *
 * @author Pedro
 */
public class DTORastreador {

    private String telefono;
    private String dni;
    private String nombre;
    private String clave;

    public DTORastreador(){

    }

    public DTORastreador(String telefono, String dni, String nombre, String clave) {
        this.telefono = telefono;
        this.dni = dni;
        this.nombre = nombre;
        this.clave = clave;

    }

    public DTORastreador(Rastreador rastreador) {
        this.telefono = rastreador.getTelefono();
        this.dni = rastreador.getDni();
        this.nombre = rastreador.getNombre();
        this.clave = rastreador.getPassword();

    }

    public String getDni() {
        return dni;
    }

    public String getClave() {
        return clave;
    }

    public Rastreador aRastreador() {
        return new Rastreador(telefono, dni, nombre, clave);
    }

    /**
     * @return the telefono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * @param telefono the telefono to set
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * @param dni the dni to set
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @param clave the clave to set
     */
    public void setClave(String clave) {
        this.clave = clave;
    }
    
    public String toJson(){
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(DTORastreador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
