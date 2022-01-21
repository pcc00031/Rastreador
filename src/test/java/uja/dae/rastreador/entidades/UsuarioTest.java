/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uja.dae.rastreador.entidades;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Pedro
 */
public class UsuarioTest {

    public UsuarioTest() {

    }

    @Test
    void testValidationUsuario() {
        java.util.UUID uuid = new java.util.UUID(1, 1);
        Usuario usuario = new Usuario("692967791");
        usuario.setUuid(uuid);

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);

        Assertions.assertThat(violations).isEmpty();
    }

    @Test
    void testAddContacto() {
        java.util.UUID uuid = new java.util.UUID(1, 1);
        Usuario usuario = new Usuario("692967791");
        usuario.setUuid(uuid);

        java.util.UUID uuid2 = new java.util.UUID(2, 1);
        Usuario otroUsuario = new Usuario("692967792");
        otroUsuario.setUuid(uuid2);

        Contacto contacto = new Contacto(usuario, otroUsuario);
        contacto.setFecha_contacto(LocalDateTime.now());
        usuario.nuevoContacto(contacto);

        List<Contacto> contactos = usuario.verContactos();
        Assertions.assertThat(contactos.size()).isEqualTo(1);
        Assertions.assertThat(contactos.get(0).getFecha_contacto()).isEqualTo(contacto.getFecha_contacto());

    }
}
