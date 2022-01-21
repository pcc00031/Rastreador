package uja.dae.rastreador.entidades;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 *
 * @author Pedro
 */
public class RastreadorTest {

    public RastreadorTest() {

    }

    @Test
    void testRastreadorValido() {
        Rastreador rastreador = new Rastreador(
                "666532145",
                "75624589F",
                "Pepe",
                "Pepito");

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Rastreador>> violations = validator.validate(rastreador);  //vamos guardando los fallos 

        Assertions.assertThat(violations).isEmpty();  //comprueba que no haya violaciones
    }

    @Test
    void testRastreadorTlfInvalido() {
        Rastreador rastreador = new Rastreador(
                "1234",
                "75624589F",
                "Pepe",
                "Pepito");

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Rastreador>> violations = validator.validate(rastreador);  //vamos guardando los fallos 

        Assertions.assertThat(violations).isNotEmpty();  //comprueba que no haya violaciones
    }

    @Test
    void testRastreadorDniInvalido() {
        Rastreador rastreador = new Rastreador(
                "666532145",
                "75629F",
                "Pepe",
                "Pepito");

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Rastreador>> violations = validator.validate(rastreador);  //vamos guardando los fallos 

        Assertions.assertThat(violations).isNotEmpty();  //comprueba que no haya violaciones
    }

    @Test
    void testRastreadorNombreInvalido() {
        Rastreador rastreador = new Rastreador(
                "666532145",
                "75624589F",
                "",
                "Pepito");

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Rastreador>> violations = validator.validate(rastreador);  //vamos guardando los fallos 

        Assertions.assertThat(violations).isNotEmpty();  //comprueba que no haya violaciones
    }

    @Test
    void testRastreadorClaveInvalido() {  //salta el test, posible codificador
        Rastreador rastreador = new Rastreador(
                "666532145",
                "75624589F",
                "Pepe",
                "");

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Rastreador>> violations = validator.validate(rastreador);  //vamos guardando los fallos 

        Assertions.assertThat(violations).isNotEmpty();  //comprueba que no haya violaciones
    }

    @Test
    void testComprobacionClaveValida() {
        String clave = "Pepito";

        Rastreador rastreador = new Rastreador(
                "666532145",
                "75624589F",
                "Pepe",
                clave);

        Assertions.assertThat(rastreador.claveValida(clave)).isTrue();
    }

    @Test
    void testComprobacionClaveInvalida() {
        String clave = "Pepito1";

        Rastreador rastreador = new Rastreador(
                "666532145",
                "75624589F",
                "Pepe",
                "Pepito2");

        Assertions.assertThat(rastreador.claveValida(clave)).isFalse();
    }
}
