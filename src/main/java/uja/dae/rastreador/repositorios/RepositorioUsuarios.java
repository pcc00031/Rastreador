package uja.dae.rastreador.repositorios;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uja.dae.rastreador.entidades.Contacto;
import uja.dae.rastreador.entidades.Usuario;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Pedro
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class RepositorioUsuarios {

    @PersistenceContext
    EntityManager em;

    // Operaciones principales
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Optional<Usuario> buscar(java.util.UUID uuid) {
        return Optional.ofNullable(em.find(Usuario.class, uuid));
    }

    public void guardar(Usuario usuario) {
        em.persist(usuario);
    }

    public void borrarContacto(Usuario usuario, Contacto contacto) {
        usuario = em.merge(usuario);
        usuario.borrarContacto(contacto);
        em.remove(contacto);
    }

    public void actualizar(Usuario usuario) {
        em.merge(usuario);
    }

    // Operaciones especificas con sentencias personalizadas
    public List<Usuario> verUsuarios() {
        List<Usuario> usuarios;
        usuarios = em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
        return usuarios;
    }

    @Transactional
    public List<Contacto> verContactos(java.util.UUID uuid) {
        List<Contacto> lc;
        Query q = em.createQuery("Select c from Usuario u, Contacto c where c.usuarioPropietario.uuid=:uid AND u.uuid=:uid",
                Contacto.class);
        q.setParameter("uid", uuid);
        lc = q.getResultList();
        return lc;
    }

    public List<Contacto> contactosAntiguos(LocalDateTime f1) {
        List<Contacto> lc;
        Query q = em.createQuery("Select c from Contacto c where c.fecha_contacto < :mayor",
                Contacto.class);
        q.setParameter("mayor", f1);
        lc = q.getResultList();
        return lc;
    }

    public List<Contacto> obtenerContactosFecha(LocalDateTime f1, LocalDateTime f2, java.util.UUID uuid) {
        List<Contacto> lc;
        Query q = em.createQuery("Select c from Contacto c, Usuario u where c.usuarioPropietario.uuid =:uid " +
                "AND u.uuid =:uid AND c.fecha_contacto between :menor and :actual", Contacto.class);
        q.setParameter("uid", uuid);
        q.setParameter("actual", f1);
        q.setParameter("menor", f2);
        lc = q.getResultList();
        return lc;
    }

    public long infectados() {
        long infectados;
        String sql = "Select Count(u.uuid) from Usuario u "
                + "where u.fecha_pos is not null";
        Query q = em.createQuery(sql);
        infectados = (long) q.getSingleResult();
        return infectados;
    }

    public long positivosActuales() {
        long positivos;
        String sql = "Select Count(u.uuid) from Usuario u "
                + "where u.positivo = TRUE";
        Query q = em.createQuery(sql);
        positivos = (long) q.getSingleResult();
        return positivos;
    }

    public long positivosFecha(LocalDateTime f1, LocalDateTime f2) {
        long positivos;
        String sql = "Select Count(u.uuid) from Usuario u "
                + "where u.fecha_pos between :menor and :actual";
        Query q = em.createQuery(sql);
        q.setParameter("actual", f1);
        q.setParameter("menor", f2);
        positivos = (long) q.getSingleResult();
        return positivos;
    }

    @Transactional(readOnly = true)
    public long contagiosPos() {
        long contagios;
        String sql = "Select Count(c.id) from Usuario u, Contacto c "
                + "where u.positivo = TRUE AND c.usuarioPropietario.uuid = u.uuid "
                + "AND c.usuarioContactado.positivo = TRUE";
        Query q = em.createQuery(sql);
        contagios = (long) q.getSingleResult();
        return contagios;
    }

}
