package uja.dae.rastreador.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author Pedro
 */
@SpringBootApplication(scanBasePackages = {
        "uja.dae.rastreador.servicios",
        "uja.dae.rastreador.repositorios",
        "uja.dae.rastreador.controladoresREST",
        "uja.dae.rastreador.controller",
        "uja.dae.rastreador.seguridad"})
@EntityScan(basePackages = "uja.dae.rastreador.entidades")
public class VidTrackerApp extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(VidTrackerApp.class);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(VidTrackerApp.class);
    }

}
