package uja.dae.rastreador.servicios;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import uja.dae.rastreador.entidades.Contacto;
import uja.dae.rastreador.entidades.Rastreador;
import uja.dae.rastreador.entidades.Usuario;
import uja.dae.rastreador.excepciones.RastreadorNoRegistrado;
import uja.dae.rastreador.excepciones.RastreadorYaRegistrado;
import uja.dae.rastreador.excepciones.UsuarioNoRegistrado;
import uja.dae.rastreador.repositorios.RepositorioRastreadores;
import uja.dae.rastreador.repositorios.RepositorioUsuarios;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author pcc00031
 */
@SpringBootTest(classes = uja.dae.rastreador.app.VidTrackerApp.class)
public class VidTrackerTest {

    @Autowired
    ServicioVidTracker vidTrackerTest;

    @Autowired
    RepositorioUsuarios usuarios;

    @Autowired
    RepositorioRastreadores rastreadores;

    // METODOS AUXILIARES
    public Usuario crearUsuario() {
        Usuario usuario = new Usuario("692967791");
        return usuario;
    }

    public Rastreador crearRastreador() {
        Rastreador rastreador = new Rastreador(
                "666532145",
                "75624589F",
                "Pepe",
                "Pepito2");
        return rastreador;
    }

    public List<UUID> cargaContactos() {
        List<UUID> contactos = new ArrayList<>();
        // usuarios contactos
        Usuario usuario1 = new Usuario("666123457");
        Usuario usuario2 = new Usuario("666123459");
        Usuario usuario3 = new Usuario("666123458");

        java.util.UUID uuid1 = vidTrackerTest.altaUsuario(usuario1);
        java.util.UUID uuid2 = vidTrackerTest.altaUsuario(usuario2);
        java.util.UUID uuid3 = vidTrackerTest.altaUsuario(usuario3);

        // creacion de lista de uuids
        contactos.add(uuid1);
        contactos.add(uuid2);
        contactos.add(uuid3);
        contactos.add(uuid3); // contacto repetido

        return contactos;
    }

    @Test
    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    public void testAltaUsuarioInvalido() {
        Usuario usuario = crearUsuario();
        usuario.setFecha_alta(LocalDateTime.MAX); // fecha incorrecta
        Assertions.assertThatThrownBy(() -> {
            vidTrackerTest.altaUsuario(usuario);
        })
                .isInstanceOf(ConstraintViolationException.class);

    }

    @Test
    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    public void testAltaUsuarioValido() {
        Usuario usuario = crearUsuario();
        java.util.UUID uuid = vidTrackerTest.altaUsuario(usuario);

        Assertions.assertThat(usuarios.buscar(uuid)).isNotNull();
        Assertions.assertThat(usuarios.verUsuarios().size() == 1);
    }

    @Test
    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)  //para que no se modifique la base de datos con el test
    public void testAltaRastreadorValido() {
        Rastreador rastreador = crearRastreador();
        String dni = vidTrackerTest.altaRastreador(rastreador);

        Assertions.assertThat(rastreadores.buscar(dni));
        Assertions.assertThat(rastreadores.verRastreadores().size() == 1);
    }

    @Test
    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)  //para que no se modifique la base de datos con el test
    public void testAltaRastreadorInvalido1() {
        Rastreador rastreador = null;

        Assertions.assertThatThrownBy(() -> {               //esperando excepcion
            vidTrackerTest.altaRastreador(rastreador);
        }).isInstanceOf(ConstraintViolationException.class);  //error de validaciones

    }

    @Test
    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)  //para que no se modifique la base de datos con el test
    public void testAltaRastreadorInvalido2() {
        Rastreador rastreador1 = crearRastreador();
        vidTrackerTest.altaRastreador(rastreador1);

        Rastreador rastreador2 = rastreador1;

        Assertions.assertThatThrownBy(() -> {               //esperando excepcion
            vidTrackerTest.altaRastreador(rastreador2);
        }).isInstanceOf(RastreadorYaRegistrado.class);  //error de validaciones //.class SIEMPRE

    }

    @Test
    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)  //para que no se modifique la base de datos con el test
    public void testNotificarPositivoValido() {
        Rastreador rastreador = crearRastreador();
        Usuario usuario = crearUsuario();

        String dni = vidTrackerTest.altaRastreador(rastreador);
        java.util.UUID uuid = vidTrackerTest.altaUsuario(usuario);

        Assertions.assertThat(rastreadores.buscar(dni).get().getNumPositivos() == 0);

        vidTrackerTest.notificarPositivo(dni, uuid, LocalDateTime.now());

        Assertions.assertThat(usuarios.buscar(uuid).orElseThrow(UsuarioNoRegistrado::new).getPositivo()).isTrue();
        Assertions.assertThat(usuarios.buscar(uuid).orElseThrow(UsuarioNoRegistrado::new).getFecha_pos()).isNotNull();
        Assertions.assertThat(rastreadores.buscar(dni).orElseThrow(RastreadorNoRegistrado::new).getNumPositivos() == 1);
    }

    @Test
    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)  //para que no se modifique la base de datos con el test
    public void testNotificarCuracionValido() {
        Rastreador rastreador = crearRastreador();
        Usuario usuario = crearUsuario();

        String dni = vidTrackerTest.altaRastreador(rastreador);
        java.util.UUID uuid = vidTrackerTest.altaUsuario(usuario);

        Assertions.assertThat(rastreadores.buscar(dni).get().getNumCuraciones() == 0);

        vidTrackerTest.notificarPositivo(dni, uuid, LocalDateTime.now());

        vidTrackerTest.notificarCuracion(dni, uuid, LocalDateTime.now());

        Assertions.assertThat(usuarios.buscar(uuid).get().getPositivo()).isFalse();
        Assertions.assertThat(usuarios.buscar(uuid).get().getFecha_pos()).isNotNull();
        Assertions.assertThat(rastreadores.buscar(dni).get().getNumCuraciones() == 1);
    }

    @Test
    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    public void testNotificarContactoCercanoValido() {
        Usuario usuario = new Usuario("666123456");
        java.util.UUID uuid = vidTrackerTest.altaUsuario(usuario);

        // creacion de fechas
        List<LocalDateTime> fechas = new ArrayList<>();
        LocalDateTime fecha1 = LocalDateTime.of(2021, Month.OCTOBER, 9, 12, 22);  //anio, mes, dia, hora, minuto
        LocalDateTime fecha2 = LocalDateTime.of(2021, Month.OCTOBER, 21, 12, 22);  //anio, mes, dia, hora, minuto
        LocalDateTime fecha3 = LocalDateTime.of(2021, Month.OCTOBER, 22, 12, 22);  //anio, mes, dia, hora, minuto
        LocalDateTime fecha4 = LocalDateTime.of(2021, Month.OCTOBER, 23, 12, 22);  //anio, mes, dia, hora, minuto

        fechas.add(fecha1);
        fechas.add(fecha2);
        fechas.add(fecha3);
        fechas.add(fecha4);
        List<java.util.UUID> contactos = cargaContactos();
        vidTrackerTest.notificarContactoCercano(uuid, contactos, fechas);

        // debe haber 3 contactos en total, uno de ellos se repite
        Assertions.assertThat(usuarios.verContactos(uuid).size()).isEqualTo(3);
        Assertions.assertThat(usuarios.verContactos(contactos.get(0)).size()).isEqualTo(1);
        Assertions.assertThat(usuarios.verContactos(contactos.get(1)).size()).isEqualTo(1);
        Assertions.assertThat(usuarios.verContactos(contactos.get(2)).size()).isEqualTo(1);

        // comprobamos que la fecha se actualiza con la ultima
        Assertions.assertThat(usuarios.verContactos(uuid).get(2).getFecha_contacto()).isNotEqualTo(fecha3);
        Assertions.assertThat(usuarios.verContactos(uuid).get(2).getFecha_contacto()).isEqualTo(fecha4);
    }

    @Test
    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    public void testObtencionContactosValido() {
        Rastreador rastreador = new Rastreador(
                "666532145",
                "75624589F",
                "Pepe",
                "Pepito2");

        String dni = vidTrackerTest.altaRastreador(rastreador);

        // usuario principal
        Usuario usuario = new Usuario("666123456");
        java.util.UUID uuid = vidTrackerTest.altaUsuario(usuario);

        //Notificamos el positivo y la fecha en que se produce
        vidTrackerTest.notificarPositivo(dni, uuid, LocalDateTime.now());

        // creacion de fechas
        List<LocalDateTime> fechas = new ArrayList<>();
        LocalDateTime fecha1 = LocalDateTime.of(2021, Month.OCTOBER, 9, 12, 22);  //anio, mes, dia, hora, minuto
        LocalDateTime fecha2 = LocalDateTime.now().minusDays(10);
        LocalDateTime fecha3 = LocalDateTime.of(2021, Month.OCTOBER, 22, 12, 22);  //anio, mes, dia, hora, minuto
        LocalDateTime fecha4 = LocalDateTime.now().minusDays(5);

        fechas.add(fecha1);
        fechas.add(fecha2);
        fechas.add(fecha3);
        fechas.add(fecha4);

        // creacion de uuids
        List<java.util.UUID> contactos = cargaContactos();

        vidTrackerTest.notificarContactoCercano(uuid, contactos, fechas);

        // debe haber 3 contactos en total, uno de ellos se repite
        Assertions.assertThat(usuarios.verContactos(uuid).size()).isEqualTo(3);

        // comprobamos que la fecha se actualiza con la ultima
        Assertions.assertThat(usuarios.verContactos(uuid).get(2).getFecha_contacto()).isNotEqualTo(fecha3);
        Assertions.assertThat(usuarios.verContactos(uuid).get(2).getFecha_contacto())
                .isCloseTo(fecha4, Assertions.byLessThan(1, ChronoUnit.SECONDS)); // comprobamos de esta forma por now()

        List<Contacto> contactosCercanos = vidTrackerTest.obtencionContactos(uuid);

        // un contacto se repite y otro esta fuera del rango de dos semanas
        Assertions.assertThat(contactosCercanos.size()).isEqualTo(2);
    }

    @Test
    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    void testBorrarContactosAntiguosValido() {

        // usuario principal
        Usuario usuario = new Usuario("666123456");

        // usuarios contactos     
        Usuario usuario2 = new Usuario("666123459");
        Usuario usuario3 = new Usuario("666123458");

        java.util.UUID uuid = vidTrackerTest.altaUsuario(usuario);
        java.util.UUID uuid1 = vidTrackerTest.altaUsuario(usuario2);
        java.util.UUID uuid2 = vidTrackerTest.altaUsuario(usuario3);

        LocalDateTime ldt = LocalDateTime.of(2021, Month.AUGUST, 20, 12, 22);
        LocalDateTime ldt1 = LocalDateTime.now();

        List<LocalDateTime> fechas = new ArrayList<>();

        fechas.add(ldt);
        fechas.add(ldt1);

        List<java.util.UUID> contactos = new ArrayList<>();
        contactos.add(uuid1);
        contactos.add(uuid2);

        vidTrackerTest.notificarContactoCercano(uuid, contactos, fechas);

        // comprobamos que el contacto ha sido agregado correctamente
        Assertions.assertThat(usuarios.verContactos(usuario.getUuid()).size()).isEqualTo(2);
        Assertions.assertThat(usuarios.verContactos(usuario.getUuid()).get(0).getFecha_contacto()).isEqualTo(ldt);

        // para que el contacto sea borrado, la diferencia de fechas debe ser mayor a 31
        long diferencia;
        diferencia = ChronoUnit.DAYS.between(usuarios.verContactos(usuario.getUuid()).get(0).getFecha_contacto(), LocalDateTime.now());
        Assertions.assertThat(diferencia).isGreaterThan(31);

        vidTrackerTest.borrarContactosAntiguos();

        // comprobamos que el contacto es eliminado por antiguedad
        Assertions.assertThat(usuarios.verContactos(usuario.getUuid()).size()).isEqualTo(1);
    }

    @Test
    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    public void testInfectadosTotalValido() {
        // usuario principal      
        Usuario usuario = new Usuario("666123456");

        // usuarios contactos
        Usuario usuario1 = new Usuario("666123457");
        Usuario usuario2 = new Usuario("666123459");
        Usuario usuario3 = new Usuario("666123458");

        Rastreador rastreador = new Rastreador(
                "666532145",
                "75624589F",
                "Pepe",
                "Pepito2");

        vidTrackerTest.altaUsuario(usuario);
        vidTrackerTest.altaUsuario(usuario1);
        vidTrackerTest.altaUsuario(usuario2);
        vidTrackerTest.altaUsuario(usuario3);
        vidTrackerTest.altaRastreador(rastreador);

        vidTrackerTest.notificarPositivo(rastreador.getDni(), usuario.getUuid(), LocalDateTime.now());
        vidTrackerTest.notificarPositivo(rastreador.getDni(), usuario2.getUuid(), LocalDateTime.now());

        Assertions.assertThat(vidTrackerTest.infectadosTotal()).isEqualTo(2);
    }

    @Test
    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    public void testNumPositivosActValido() {
        // usuario principal
        Usuario usuario = new Usuario("666123456");

        // usuarios contactos
        Usuario usuario1 = new Usuario("666123457");
        Usuario usuario2 = new Usuario("666123459");
        Usuario usuario3 = new Usuario("666123458");

        Rastreador rastreador = new Rastreador(
                "666532145",
                "75624589F",
                "Pepe",
                "Pepito2");

        vidTrackerTest.altaUsuario(usuario);
        vidTrackerTest.altaUsuario(usuario1);
        vidTrackerTest.altaUsuario(usuario2);
        vidTrackerTest.altaUsuario(usuario3);
        vidTrackerTest.altaRastreador(rastreador);

        vidTrackerTest.notificarPositivo(rastreador.getDni(), usuario.getUuid(), LocalDateTime.now());
        vidTrackerTest.notificarPositivo(rastreador.getDni(), usuario1.getUuid(), LocalDateTime.now());
        vidTrackerTest.notificarPositivo(rastreador.getDni(), usuario2.getUuid(), LocalDateTime.now());

        Assertions.assertThat(vidTrackerTest.numPositivosAct()).isEqualTo(3);

        vidTrackerTest.notificarCuracion(rastreador.getDni(), usuario2.getUuid(), LocalDateTime.now());

        Assertions.assertThat(vidTrackerTest.numPositivosAct()).isEqualTo(2);
    }

    @Test
    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    public void testMediaContagioValido() {
        Rastreador rastreador = new Rastreador(
                "666532145",
                "75624589F",
                "Pepe",
                "Pepito2");

        String dni = vidTrackerTest.altaRastreador(rastreador);

        // usuario principal
        Usuario usuario = new Usuario("666123456");
        java.util.UUID uuid = vidTrackerTest.altaUsuario(usuario);

        // creacion de fechas
        List<LocalDateTime> fechas = new ArrayList<>();
        LocalDateTime fecha1 = LocalDateTime.of(2021, Month.OCTOBER, 9, 12, 22);  //anio, mes, dia, hora, minuto
        LocalDateTime fecha2 = LocalDateTime.of(2021, Month.OCTOBER, 21, 12, 22);  //anio, mes, dia, hora, minuto
        LocalDateTime fecha3 = LocalDateTime.of(2021, Month.OCTOBER, 22, 12, 22);  //anio, mes, dia, hora, minuto

        fechas.add(fecha1);
        fechas.add(fecha2);
        fechas.add(fecha3);

        List<java.util.UUID> contactosCercanos = cargaContactos();
        contactosCercanos.remove(contactosCercanos.get(3));

        //Notificamos el positivo y la fecha en que se produce
        vidTrackerTest.notificarPositivo(dni, uuid, LocalDateTime.of(2021, Month.OCTOBER, 24, 12, 22));
        vidTrackerTest.notificarPositivo(dni, contactosCercanos.get(0), LocalDateTime.of(2021, Month.OCTOBER, 24, 12, 22));
        vidTrackerTest.notificarPositivo(dni, contactosCercanos.get(1), LocalDateTime.of(2021, Month.OCTOBER, 24, 12, 22));

        vidTrackerTest.notificarContactoCercano(uuid, contactosCercanos, fechas);
        Assertions.assertThat(vidTrackerTest.numPositivosAct()).isEqualTo(3);
        Assertions.assertThat(vidTrackerTest.mediaContagio()).isCloseTo(1.33, Assertions.offset(0.01));
    }

    @Test
    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    public void testNumPostQuincenaValido() {
        // usuario principal
        Usuario usuario = new Usuario("666123456");

        // usuarios contactos        
        Usuario usuario1 = new Usuario("666123457");
        Usuario usuario2 = new Usuario("666123459");
        Usuario usuario3 = new Usuario("666123458");

        Rastreador rastreador = new Rastreador(
                "666532145",
                "75624589F",
                "Pepe",
                "Pepito2");

        java.util.UUID uuid = vidTrackerTest.altaUsuario(usuario);
        java.util.UUID uuid1 = vidTrackerTest.altaUsuario(usuario1);
        java.util.UUID uuid2 = vidTrackerTest.altaUsuario(usuario2);
        vidTrackerTest.altaUsuario(usuario3);
        String dni = vidTrackerTest.altaRastreador(rastreador);

        LocalDateTime ldt1 = LocalDateTime.now();
        LocalDateTime ldt2 = LocalDateTime.now();
        ldt2.minusDays(6);
        ldt1.minusDays(10);

        vidTrackerTest.notificarPositivo(dni, uuid, ldt2);
        vidTrackerTest.notificarPositivo(dni, uuid1, ldt1);
        vidTrackerTest.notificarPositivo(dni, uuid2, LocalDateTime.of(2021, Month.OCTOBER, 9, 12, 22));

        Assertions.assertThat(vidTrackerTest.numPosDias(15)).isEqualTo(2);
    }

    @Test
    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    public void testPostRastreadorValido() {
        // usuario principal
        Usuario usuario = new Usuario("666123456");

        // usuarios contactos
        Usuario usuario1 = new Usuario("666123457");
        Usuario usuario2 = new Usuario("666123459");
        Usuario usuario3 = new Usuario("666123458");

        Rastreador rastreador = new Rastreador(
                "666532145",
                "75624589F",
                "Pepe",
                "Pepito2");

        java.util.UUID uuid = vidTrackerTest.altaUsuario(usuario);
        java.util.UUID uuid1 = vidTrackerTest.altaUsuario(usuario1);
        java.util.UUID uuid2 = vidTrackerTest.altaUsuario(usuario2);
        vidTrackerTest.altaUsuario(usuario3);
        String dni = vidTrackerTest.altaRastreador(rastreador);

        vidTrackerTest.notificarPositivo(dni, uuid, LocalDateTime.of(2021, Month.OCTOBER, 15, 12, 22));
        vidTrackerTest.notificarPositivo(dni, uuid1, LocalDateTime.of(2021, Month.OCTOBER, 19, 12, 22));
        vidTrackerTest.notificarPositivo(dni, uuid2, LocalDateTime.of(2021, Month.OCTOBER, 9, 12, 22));

        Assertions.assertThat(vidTrackerTest.postRastreador(dni)).isEqualTo(3);
    }
}
