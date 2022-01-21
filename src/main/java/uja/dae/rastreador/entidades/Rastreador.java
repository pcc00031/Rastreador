package uja.dae.rastreador.entidades;

import ch.qos.logback.core.encoder.EchoEncoder;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import uja.dae.rastreador.util.CodificadorPassword;
import uja.dae.rastreador.util.ExprReg;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Pedro
 */
@Entity
public class Rastreador implements Serializable {

    @Pattern(regexp = ExprReg.TLF)
    private String telefono;

    @Id
    @Size(min = 9, max = 9)
    @Pattern(regexp = ExprReg.DNI)
    private String dni;

    @NotBlank
    private String nombre;

    @NotBlank
    private String password;

    @PositiveOrZero
    private Integer numPositivos = 0;

    @PositiveOrZero
    private Integer numCuraciones = 0;

    public Rastreador() {
        this.numPositivos = 0;
        this.numCuraciones = 0;
    }

    public Rastreador(String telefono, String dni, String nombre, String password) {
        this.telefono = telefono;
        this.dni = dni;
        this.nombre = nombre;
        this.password = password;
        this.numPositivos = 0;
        this.numCuraciones = 0;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean claveValida(String clave) {
        //-  return CodificadorPassword.igual(clave, this.password);
        return true;
    }

    public Integer getNumPositivos() {
        return numPositivos;
    }

    public void setNumPositivos(Integer numPositivos) {
        this.numPositivos += numPositivos;
    }

    public Integer getNumCuraciones() {
        return numCuraciones;
    }

    public void setNumCuraciones(Integer numCuraciones) {
        this.numCuraciones = numCuraciones;
    }

}
