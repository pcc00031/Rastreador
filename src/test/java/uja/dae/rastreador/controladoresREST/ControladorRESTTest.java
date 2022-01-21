package uja.dae.rastreador.controladoresREST;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.annotation.DirtiesContext;
import uja.dae.rastreador.controladoresREST.DTO.DTORastreador;
import uja.dae.rastreador.controladoresREST.DTO.DTORastreador1;
import uja.dae.rastreador.controladoresREST.DTO.DTOUsuario;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import org.springframework.util.Base64Utils;
import uja.dae.rastreador.entidades.Rastreador;
import uja.dae.rastreador.entidades.Usuario;
import uja.dae.rastreador.excepciones.RastreadorNoRegistrado;
import uja.dae.rastreador.excepciones.UsuarioNoRegistrado;
import uja.dae.rastreador.servicios.ServicioVidTracker;

/**
 * @author Pedro
 */

@SpringBootTest(classes = uja.dae.rastreador.app.VidTrackerApp.class)
@AutoConfigureMockMvc
public class ControladorRESTTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    ServicioVidTracker servicio;


    /**
     * Crear un TestRestTemplate para las pruebas
     */
//    @PostConstruct
//    void crearRestTemplateBuilder() {
//        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
//                .rootUri("http://localhost:" + localPort + "/vidtracker")
//                .additionalMessageConverters(List.of(springBootJacksonConverter));
//
//        restTemplate = new TestRestTemplate(restTemplateBuilder);
//    }

    @Test
    @WithMockUser(username = "75931474X", password = "carlos", roles = "RASTREADOR")  //FIXME como recuperar un usuario dado de alta en el servicio REST?
    void testAltaUsuarioValido() throws Exception{
        DTOUsuario usuario1 = new DTOUsuario("666123456");
        String json = usuario1.toJson();
        ResultActions result = mockMvc.perform(post("/vidtracker/usuarios").contentType(MediaType.APPLICATION_JSON).content(json));

        Optional<Usuario> usuario2 = servicio.verUsuario(usuario1.getUuid());

        result.andExpect(status().isCreated());
        //result.andExpect(content().string(usuario1.getUuid()));   //FIXME ?
        Assertions.assertThat(usuario2.orElseThrow(UsuarioNoRegistrado::new)!=null);
    }

    @Test
    @WithMockUser(username = "75931474X", password = "carlos", roles = "RASTREADOR")  //necesario para el basic auth
    void testAltaRastreadorValido() throws Exception{
        DTORastreador rastreador1 = new DTORastreador("666123456", "77712345P", "Pepito", "tomate");
        String json = rastreador1.toJson();
        ResultActions result = mockMvc.perform(post("/vidtracker/rastreadores").contentType(MediaType.APPLICATION_JSON).content(json));

        Optional<Rastreador> rastreador2 = servicio.verRastreador(rastreador1.getDni());

        result.andExpect(status().isCreated());
       // result.andExpect(content().string(rastreador1.getDni()));
        Assertions.assertThat(rastreador2.orElseThrow(RastreadorNoRegistrado::new)!=null);
    }

    @Test
    @WithMockUser(username = "75931474X", password = "carlos", roles = "RASTREADOR")
    void testVerRastreadorValido() throws Exception{
        servicio.altaRastreador(new Rastreador("666123455", "75931475X", "PTOLOMEO", "ptolocago"));
        ResultActions result = mockMvc.perform(get("/vidtracker/rastreadores/75931475X"));

        result.andExpect(status().isOk());
        Optional<Rastreador> rastreador1 = servicio.verRastreador("75931475X");
        Rastreador rastreadorNoOptional = rastreador1.orElseThrow(RastreadorNoRegistrado::new);

        result.andExpect(content().json(new DTORastreador1(rastreadorNoOptional).toJson()));
    }

    @Test
    @WithMockUser(username = "75931474X", password = "carlos", roles = "RASTREADOR")
    void testVerRastreadorInvalido() throws Exception{

        ResultActions result = mockMvc.perform(get("/vidtracker/rastreadores/75931478X"));

        result.andExpect(status().isNotFound());
    }
}
