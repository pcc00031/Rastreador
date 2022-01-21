package uja.dae.rastreador.entidades;

import javax.persistence.*;
import javax.validation.constraints.PastOrPresent;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Pedro
 */
@Entity
public class Contacto implements Serializable {

    @PastOrPresent
    private LocalDateTime fecha_contacto;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // usuario con el que se ha mantenido contacto
    @ManyToOne
    @JoinColumn(name = "usuario_contacto")
    private Usuario usuarioContactado;

    // usuario principal
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_propietario")
    private Usuario usuarioPropietario;

    public Contacto() {
    }

    public Contacto(Usuario contacto) {
        this.usuarioPropietario = null;
        this.usuarioContactado = contacto;
    }

    public Contacto(Usuario propietario, Usuario contacto) {
        this.usuarioPropietario = propietario;
        this.usuarioContactado = contacto;
    }

    /* GETTERS  Y SETTERS */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getFecha_contacto() {
        return fecha_contacto;
    }

    public void setFecha_contacto(LocalDateTime fecha_contacto) {
        this.fecha_contacto = fecha_contacto;
    }

    public Usuario getUsuarioContactado() {
        return usuarioContactado;
    }

    public Usuario getUsuarioPropietario() {
        return usuarioPropietario;
    }

    public void setUsuarioPropietario(Usuario usuarioPropietario) {
        this.usuarioPropietario = usuarioPropietario;
    }

    public void setUsuarioContactado(Usuario usuarioContactado) {
        this.usuarioContactado = usuarioContactado;
    }

    /* SOBRECARGA DE METODOS */
    /**
     * @brief Compara si son iguales dos objetos
     * del tipo Contacto
     * @post Si el usuarioContactado de dos
     * contactos es igual, estos contactos seran
     * el mismo
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Contacto contacto = (Contacto) obj;
        return (this.usuarioContactado.equals(contacto.getUsuarioContactado()));
    }
}
