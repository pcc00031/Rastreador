package uja.dae.rastreador.repositorios;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uja.dae.rastreador.entidades.Rastreador;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

/**
 * @author Carlos
 */
@Repository                                           //indica que la clase es un repositorio. Repositorio: mecanismo para encapsular el comportamiento de almacenamiento, recuperación y búsqueda que emula una colección de objetos
@Transactional(propagation = Propagation.REQUIRED)    //@Transactional: automatiza la transaccion. Required: Da soporte a la transacción actual o crea una nueva sino existe.
public class RepositorioRastreadores {
    //@PersistenceContext se encarga de crear un EntityManager único para cada hilo. En una aplicación de producción, puede tener varios clientes llamando a su aplicación al mismo tiempo. 
    //Para cada llamada, la aplicación crea un hilo. Cada hilo debe usar su propio EntityManager. @PersistenceContextle permite especificar qué unidad de persistencia desea utilizar.

    @PersistenceContext
    EntityManager em;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true) //Supports: Da soporte a la transacción actual, sino existe, ejecuta sin transacción.
    public Optional<Rastreador> buscar(String dni) {                    //readOnly: nos permitirá acceder a los métodos Lazy
        return Optional.ofNullable(em.find(Rastreador.class, dni));
    }

    public void guardar(Rastreador rastreador) {
        em.persist(rastreador);
    }

    public void actualizar(Rastreador rastreador) {
        em.merge(rastreador);
    }

    public List<Rastreador> verRastreadores() {
        List<Rastreador> rastreadores;
        rastreadores = em.createQuery("SELECT r FROM Rastreador r", Rastreador.class).getResultList();
        return rastreadores;
    }

}
