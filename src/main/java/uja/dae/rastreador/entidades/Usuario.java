package uja.dae.rastreador.entidades;

import uja.dae.rastreador.util.ExprReg;

import javax.persistence.*;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Pedro
 */
@Entity
public class Usuario implements Serializable {

    @Id
    private java.util.UUID uuid;

    @Size(min = 9, max = 13)
    @Pattern(regexp = ExprReg.TLF)
    private String telefono;

    private Boolean positivo;

    @PastOrPresent
    private LocalDateTime fecha_alta = null;

    @PastOrPresent
    private LocalDateTime fecha_pos = null;

    @PastOrPresent
    private LocalDateTime fecha_cura = null;

    @OneToMany(mappedBy = "usuarioPropietario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Contacto> contactos;

    /* CONSTRUCTORES */
    public Usuario() {
    }

    public Usuario(String telefono) {
        this.uuid = null;
        this.telefono = telefono;
        this.positivo = false;
        this.fecha_alta = null;
        this.fecha_pos = null;
        this.fecha_cura = null;

        contactos = new ArrayList<>();
    }

    /* GETTERS Y SETTERS */
    public java.util.UUID getUuid() {
        return uuid;
    }

    public void setUuid(java.util.UUID uuid) {
        this.uuid = uuid;
    }

    public LocalDateTime getFecha_alta() {
        return fecha_alta;
    }

    public void setFecha_alta(LocalDateTime fecha_alta) {
        this.fecha_alta = fecha_alta;
    }

    public Boolean getPositivo() {
        return positivo;
    }

    public void setPositivo(Boolean positivo) {
        this.positivo = positivo;
    }

    public LocalDateTime getFecha_pos() {
        return fecha_pos;
    }

    public void setFecha_pos(LocalDateTime fecha_pos) {
        this.fecha_pos = fecha_pos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDateTime getFecha_cura() {
        return fecha_cura;
    }

    public void setFecha_cura(LocalDateTime fecha_cura) {
        this.fecha_cura = fecha_cura;
    }

    /**
     * @brief Agrega un nuevo contacto no repetido
     * al usuario
     * @param contacto
     * @return si esta presente o no
     */
    public boolean nuevoContacto(Contacto contacto) {
        if (!contactos.contains(contacto)) {
            contactos.add(contacto);
            return true;
        }
        contactos.set(contactos.indexOf(contacto), contacto);
        return false;
    }

    /**
     * @brief Eliminia un contacto de un usuario
     * @param contacto
     */
    public void borrarContacto(Contacto contacto) {
        contactos.remove(contacto);
    }

    /**
     * @brief Muestra contactos del usuario
     * @return lista de contactos
     */
    public List<Contacto> verContactos() {
        return contactos;
    }

    /**
     * @brief Muestra un contacto del usuario
     * @param uuid
     * @return
     */
    public Optional<Contacto> verContacto(java.util.UUID uuid) {
        return contactos.stream()
                .filter(c -> c.getUsuarioContactado().uuid.equals(uuid))
                .findFirst();
    }

    /* SOBRECARGA DE METODOS */
    /**
     * @brief Compara si son iguales dos objetos
     * del tipo Usuario
     * @post Si el UUID de dos usuarios es igual,
     * estos usuarios seran el mismo
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
        Usuario usuario = (Usuario) obj;
        return this.uuid.equals(usuario.getUuid());
    }

}
