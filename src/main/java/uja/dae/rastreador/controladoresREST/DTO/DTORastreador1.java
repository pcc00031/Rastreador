package uja.dae.rastreador.controladoresREST.DTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import uja.dae.rastreador.entidades.Rastreador;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pedro
 */
public class DTORastreador1 {

    private String telefono;
    private String dni;
    private String nombre;
    private String clave;

    int numPositivos;
    int numCuraciones;

    public DTORastreador1(){

    }

    public DTORastreador1(String telefono, String dni, String nombre, String clave) {
        this.telefono = telefono;
        this.dni = dni;
        this.nombre = nombre;
        this.clave = clave;
        this.numPositivos = 0;
        this.numCuraciones = 0;
    }

    public DTORastreador1(Rastreador rastreador) {
        this.telefono = rastreador.getTelefono();
        this.dni = rastreador.getDni();
        this.nombre = rastreador.getNombre();
       // this.clave = rastreador.getPassword();
        this.numPositivos = rastreador.getNumPositivos();
        this.numCuraciones = rastreador.getNumCuraciones();
    }

    public String getDni() {
        return dni;
    }

    public String getClave() {
        return clave;
    }

    public Rastreador aRastreador() {
       Rastreador r = new Rastreador();
       r.setNumPositivos(this.numPositivos);
       r.setNumCuraciones(this.numCuraciones);
       r.setNombre(this.nombre);
       r.setDni(this.dni);
       return r;
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

    public int getNumPositivos() {
        return numPositivos;
    }

    public void setNumPositivos(int numPositivos) {
        this.numPositivos = numPositivos;
    }

    public int getNumCuraciones() {
        return numCuraciones;
    }

    public void setNumCuraciones(int numCuraciones) {
        this.numCuraciones = numCuraciones;
    }

    public String toJson(){
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(DTORastreador1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
