package uja.dae.rastreador.seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import uja.dae.rastreador.entidades.Rastreador;
import uja.dae.rastreador.excepciones.RastreadorNoRegistrado;
import uja.dae.rastreador.repositorios.RepositorioRastreadores;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private RepositorioRastreadores repo;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public UserService() {}

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Rastreador r = repo.buscar(s).orElseThrow(RastreadorNoRegistrado::new);
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("RASTREADOR"));
        UserDetails userDet = new User(r.getDni(), encoder.encode(r.getPassword()), roles);

        return userDet;
    }
}
