package uja.dae.rastreador.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import uja.dae.rastreador.entidades.Contacto;
import uja.dae.rastreador.entidades.Rastreador;
import uja.dae.rastreador.entidades.Usuario;
import uja.dae.rastreador.excepciones.*;
import uja.dae.rastreador.repositorios.RepositorioRastreadores;
import uja.dae.rastreador.repositorios.RepositorioUsuarios;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.springframework.context.annotation.Lazy;

/**
 * @author Pedro
 */
@Service
@Validated
public class ServicioVidTracker {

    @Autowired
    @Lazy
    BCryptPasswordEncoder bcrypt;

    @Autowired
    RepositorioUsuarios repositorioUsuarios;

    @Autowired
    RepositorioRastreadores repositorioRastreadores;

    public ServicioVidTracker() {
    }

    /* METODOS */

    /**
     * @param usuario
     * @return uuid usuario
     * @brief Alta de usuario
     */
    public UUID altaUsuario(@NotNull @Valid Usuario usuario) {

        // Generacion de un identificador aleatorio en base al tlfn del usuario
        Random r = new Random();
        r.setSeed(Long.parseLong(usuario.getTelefono()));
        UUID uuid = new UUID(r.nextLong(), r.nextLong());

        //Asignacion de atributos
        usuario.setUuid(uuid);
        usuario.setFecha_alta(LocalDateTime.now());

        // Comprobacion de usuario repetido
        if (repositorioUsuarios.buscar(uuid).isPresent()) {
            throw new UsuarioYaRegistrado();
        }

        // Alta de usuario
        repositorioUsuarios.guardar(usuario);
        return uuid;
    }

    /**
     * @param rastreador
     * @return dni rastreador
     * @brief Alta de un rastreador
     */
    public String altaRastreador(@NotNull @Valid Rastreador rastreador) {

        if (repositorioRastreadores.buscar(rastreador.getDni()).isPresent()) {
            throw new RastreadorYaRegistrado();
        }
        rastreador.setPassword(bcrypt.encode(rastreador.getPassword()));
        System.out.println(rastreador.getPassword());
        repositorioRastreadores.guardar(rastreador);

        return rastreador.getDni();
    }

    /**
     * @param dni   el DNI del rastreador
     * @param clave la clave de acceso
     * @return el objeto de la clase Rastreador
     * asociado
     * @post Realiza un login de un rastreador
     */
    public Rastreador loginRastreador(@NotBlank String dni, @NotBlank String clave) {
        if (repositorioRastreadores.buscar(dni).isEmpty()) {
            throw new RastreadorNoRegistrado();
        }
        Rastreador rastreador = repositorioRastreadores.buscar(dni).orElseThrow(RastreadorNoRegistrado::new);
        if (!rastreador.claveValida(clave)) {
            throw new RastreadorClaveInvalida();
        }

        return rastreador;
    }

    /**
     * @param dni
     * @return
     * @brief Realiza un login de un rastreador
     */
    @Transactional
    public Optional<Rastreador> verRastreador(@NotBlank String dni) {
        Optional<Rastreador> loginOptional = repositorioRastreadores.buscar(dni);
        return loginOptional;
    }

    /**
     * @param uuid
     * @return
     * @brief Localiza un usuario en el repositorio
     */
    public Optional<Usuario> verUsuario(UUID uuid) {
        Optional<Usuario> usu = repositorioUsuarios.buscar(uuid);
        return usu;
    }

    /**
     * @param uuid
     * @param contactosCercanos
     * @param fechas
     * @brief Notifica contactos cercanos de un
     * usuario
     */
    @Transactional
    public void notificarContactoCercano(UUID uuid, List<UUID> contactosCercanos, List<LocalDateTime> fechas) {
        Usuario usuario = repositorioUsuarios.buscar(uuid).orElseThrow(UsuarioNoRegistrado::new);

        for (int i = 0; i < contactosCercanos.size(); i++) {
            Usuario u = repositorioUsuarios.buscar(contactosCercanos.get(i)).orElseThrow(UsuarioNoRegistrado::new);
            // Creamos contacto
            Contacto c = new Contacto(u);
            c.setUsuarioPropietario(usuario);
            c.setFecha_contacto(fechas.get(i));

            // Agregamos contacto al usuario principal       
            usuario.nuevoContacto(c);

            // Creamos contacto en el otro sentido
            Usuario aux = new Usuario();
            aux = c.getUsuarioPropietario();
            Contacto cAux = new Contacto();
            cAux.setFecha_contacto(c.getFecha_contacto());
            cAux.setUsuarioPropietario(c.getUsuarioContactado());
            cAux.setUsuarioContactado(aux);

            // Agregamos contacto al usuario secundario
            u.nuevoContacto(cAux);
        }
    }

    /**
     * @brief Todos los dias, a las 12 de la
     * noche, comprueba contactos antiguos y los
     * borra si son superiores a 31 dias
     */
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")  //se actualiza todos los días a las 00:00:00
    public void borrarContactosAntiguos() {
        LocalDateTime fechaMenosMes = LocalDateTime.now().minusMonths(1);
        // obtenemos lista de contactos entre las fechas seleccionadas
        List<Contacto> contsAntiguos = repositorioUsuarios.contactosAntiguos(fechaMenosMes);
        for (Contacto contsAntiguo : contsAntiguos) {
            // borramos cada uno
            repositorioUsuarios.borrarContacto(contsAntiguo.getUsuarioPropietario(), contsAntiguo);
        }
    }

    /**
     * @param dni
     * @param uuid
     * @param fecha
     * @brief Notifica contagio de un usuario
     */
    public void notificarPositivo(@NotBlank String dni, UUID uuid, LocalDateTime fecha) {
        Rastreador rastreador = repositorioRastreadores.buscar(dni).orElseThrow(RastreadorNoRegistrado::new);
        Usuario usuario = repositorioUsuarios.buscar(uuid).orElseThrow(UsuarioNoRegistrado::new);

        usuario.setPositivo(true);
        usuario.setFecha_pos(fecha);
        repositorioUsuarios.actualizar(usuario);

        rastreador.setNumPositivos(1);
        repositorioRastreadores.actualizar(rastreador);
    }

    /**
     * @param dni
     * @param uuid
     * @param fecha
     * @brief Notifica curacion de un usuario
     */
    public void notificarCuracion(@NotBlank String dni, UUID uuid, LocalDateTime fecha) {
        Rastreador rastreador = repositorioRastreadores.buscar(dni).orElseThrow(RastreadorNoRegistrado::new);
        Usuario usuario = repositorioUsuarios.buscar(uuid).orElseThrow(UsuarioNoRegistrado::new);

        usuario.setPositivo(false);
        usuario.setFecha_cura(fecha);
        repositorioUsuarios.actualizar(usuario);

        rastreador.setNumCuraciones(rastreador.getNumCuraciones() + 1);
        repositorioRastreadores.actualizar(rastreador);
    }

    /**
     * @param uuid
     * @return
     * @brief Obtiene contactos en las ultimas dos
     * semanas con el usuario
     */
    public List<Contacto> obtencionContactos(UUID uuid) {
        Usuario usuario = repositorioUsuarios.buscar(uuid).orElseThrow(UsuarioNoRegistrado::new);
        if (usuario.getFecha_pos() == null) {
            throw new PositivoNoRegistrado();
        }
        LocalDateTime fechaPos = usuario.getFecha_pos();
        LocalDateTime dosSemanasAntes = fechaPos.minusDays(14);

        return repositorioUsuarios.obtenerContactosFecha(fechaPos, dosSemanasAntes, uuid);
    }

    /**
     * @return
     * @brief Numero de infectados en total
     */
    public long infectadosTotal() {
        long numInfectados;
        numInfectados = repositorioUsuarios.infectados();
        return numInfectados;
    }

    /**
     * @return
     * @brief Numero positivos actuales
     */
    public long numPositivosAct() {
        long positivosActuales;
        positivosActuales = repositorioUsuarios.positivosActuales();
        return positivosActuales;
    }

    /**
     * @return
     * @brief Numero de positivos en los ultimos
     * 'dias'
     */
    public long numPosDias(long dias) {
        long posQuincena;
        LocalDateTime fechaMenosQuince = LocalDateTime.now().minusDays(dias);
        LocalDateTime fechaActual = LocalDateTime.now();

        posQuincena = repositorioUsuarios.positivosFecha(fechaActual, fechaMenosQuince);

        return posQuincena;
    }

    /**
     * @return
     * @brief Media de contagiados por cada
     * usuario positivo
     */
    public double mediaContagio() {
        double mediaContagio = 0.0;
        double contagioPos;
        contagioPos = repositorioUsuarios.contagiosPos();
        mediaContagio = contagioPos / numPositivosAct();
        return mediaContagio;
    }

    /**
     * @param dni
     * @return numPositivos
     * @brief Positivos reportados por el
     * rastreador
     */
    public Integer postRastreador(String dni) {
        Rastreador rastreador = repositorioRastreadores.buscar(dni).orElseThrow(RastreadorNoRegistrado::new);
        return rastreador.getNumPositivos();
    }

}
