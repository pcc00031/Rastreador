package uja.dae.rastreador.entidades;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.Set;

/**
 *
 * @author Pedro
 */
public class ContactoTest {

    public ContactoTest() {

    }

    @Test
    void testValidationContacto() {
        java.util.UUID uuid = new java.util.UUID(1, 1);
        Usuario usuario = new Usuario("692967791");
        usuario.setUuid(uuid);

        java.util.UUID uuid2 = new java.util.UUID(2, 1);
        Usuario otroUsuario = new Usuario("692967792");
        otroUsuario.setUuid(uuid2);
        Contacto contacto = new Contacto(usuario, otroUsuario);
        contacto.setFecha_contacto(LocalDateTime.now());

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Contacto>> violations = validator.validate(contacto);

        Assertions.assertThat(violations).isEmpty();
    }
}
