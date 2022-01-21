package uja.dae.rastreador.controladoresREST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import uja.dae.rastreador.controladoresREST.DTO.*;
import uja.dae.rastreador.entidades.Rastreador;
import uja.dae.rastreador.entidades.Usuario;
import uja.dae.rastreador.excepciones.RastreadorNoRegistrado;
import uja.dae.rastreador.excepciones.RastreadorYaRegistrado;
import uja.dae.rastreador.excepciones.UsuarioNoRegistrado;
import uja.dae.rastreador.excepciones.UsuarioYaRegistrado;
import uja.dae.rastreador.servicios.ServicioVidTracker;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author Pedro
 */
@RestController
@RequestMapping("vidtracker")
public class ControladorREST {
    final Logger LOG = Logger.getLogger("ServicioREST");

    @Autowired
    ServicioVidTracker servicio;

    @GetMapping("rastreador")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<String> rastreadorLogeado(Principal logeado) {
        try {
            LOG.log(Level.INFO, "Principal: " + logeado.getName());
            return ResponseEntity.status(HttpStatus.OK).body(logeado.getName());
        } catch (RastreadorNoRegistrado e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("usuarios")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<UUID> altaUsuario(@RequestBody DTOUsuario usu) {
        try {
            UUID uuid = servicio.altaUsuario(usu.aUsuario());
            return ResponseEntity.status(HttpStatus.CREATED).body(uuid);
        } catch (UsuarioYaRegistrado e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/rastreadores")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<Void> altaRastreador(@RequestBody DTORastreador rastreador) {
        try {
            servicio.altaRastreador(rastreador.aRastreador());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RastreadorYaRegistrado e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/rastreadores/{dni}")
    ResponseEntity<DTORastreador1> verRastreador(@PathVariable String dni) {
        Optional<Rastreador> rastreador = servicio.verRastreador(dni);
        return rastreador.map(r -> ResponseEntity.ok(new DTORastreador1(r)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuarios/{uuid}")
    ResponseEntity<DTOUsuario> verUsuario(@PathVariable UUID uuid) {
        Optional<Usuario> usuario = servicio.verUsuario(uuid);
        return usuario.map(u -> ResponseEntity.ok(new DTOUsuario(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("usuarios/{uuid}/notificacion")
    ResponseEntity<Boolean> notificarUsuario(
            Principal principal,
            @PathVariable UUID uuid,
            @RequestBody DTONotificacion notificacion) {
        try {
            LOG.log(Level.INFO, "UUID: " + uuid);
            LOG.log(Level.INFO, "Rastreador logeado: " + principal.getName());
            LOG.log(Level.INFO, "Fecha: " + notificacion.getFecha());
            LOG.log(Level.INFO, "Positivo: " + notificacion.isPositivo());
            if (!notificacion.isPositivo()) {
                servicio.notificarCuracion(principal.getName(), uuid, notificacion.getFecha());
                return ResponseEntity.status(HttpStatus.OK).body(notificacion.isPositivo());
            }
            servicio.notificarPositivo(principal.getName(), uuid, notificacion.getFecha());
            return ResponseEntity.status(HttpStatus.OK).body(notificacion.isPositivo());
        } catch (RastreadorNoRegistrado e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("usuarios/{uuid}/contactos")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<Void> notificarContacto(
            @PathVariable UUID uuid,
            @RequestBody List<DTOContacto> contactos) {
        try {
            List<LocalDateTime> fechas = new ArrayList<>();
            for (DTOContacto contacto : contactos) {
                fechas.add(contacto.getFecha_cont());
            }
            List<UUID> uuids = new ArrayList<>();
            for (DTOContacto contacto : contactos) {
                uuids.add(contacto.getContacto());
            }
            LOG.log(Level.INFO, "Total contactos: " + contactos.size());
            LOG.log(Level.INFO, "Total fechas: " + fechas.size());
            LOG.log(Level.INFO, "Total uuids: " + uuids.size());
            servicio.notificarContactoCercano(uuid, uuids, fechas);
            LOG.log(Level.INFO, "Total contactos: " + servicio.obtencionContactos(uuid).size());
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (UsuarioNoRegistrado e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/usuarios/{uuid}/contactos")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<List<DTOContacto>> verContactosUsuario(@PathVariable UUID uuid) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(servicio.obtencionContactos(uuid).stream()
                    .map(DTOContacto::new).collect(Collectors.toList()));
        } catch (UsuarioNoRegistrado e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/estadisticas/positivos")
    ResponseEntity<Long> numInfectados(@RequestParam("actual") boolean actual) {
        if (!actual)
            return ResponseEntity.status(HttpStatus.OK).body(servicio.infectadosTotal());
        return ResponseEntity.status(HttpStatus.OK).body(servicio.numPositivosAct());
    }

    @GetMapping("/estadisticas/positivo")
    ResponseEntity<Long> numInfectados(@RequestParam("dias") long dias) {
        return ResponseEntity.status(HttpStatus.OK).body(servicio.numPosDias(dias));
    }

    @GetMapping("/estadisticas/mediaContagio")
    ResponseEntity<Double> mediaContagio() {
        return ResponseEntity.status(HttpStatus.OK).body(servicio.mediaContagio());
    }

    @GetMapping("/logout")
    ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            LOG.log(Level.INFO, "Haciendo logout...");
            new SecurityContextLogoutHandler().logout(request, response, auth);
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
