package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.ExistingException;
import com.example.gatosspringboot.model.Rol;
import com.example.gatosspringboot.model.Usuario;
import com.example.gatosspringboot.repository.database.RolRepository;
import com.example.gatosspringboot.repository.database.UsuarioRepository;
import com.example.gatosspringboot.service.interfaces.IUsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements IUsuarioService {

    private final UsuarioRepository usRepo;
    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepo;

    public UsuarioService(UsuarioRepository usRepo,
                          PasswordEncoder passwordEncoder,
                          RolRepository rolRepo) {
        this.usRepo = usRepo;
        this.passwordEncoder = passwordEncoder;
        this.rolRepo = rolRepo;
    }

    @Override
    public List<Usuario> verTodos() {
        return (List<Usuario>) this.usRepo.findAll();
    }

    @Override
    //metodo para crear usuario con email + contraseña rol USER
    public String altaUsuarioCompleto(Usuario us) {
        this.existeEmail(us.getMail());
        //traigo role_user
        List<Rol> roles=new ArrayList<Rol>(
                List.of(this.rolRepo.findById(1).get())
        );
        us.setRoles(roles);
        us.setContrasenia(passwordEncoder.encode(us.getContrasenia()));
        this.usRepo.save(us);
        return us.getMail();
    }

    @Override
    public String altaUsuarioEmail(String email) {
        return null;
    }

    //No se admiten emails repetidos
    private void existeEmail(String email){
        Optional<Usuario> oUsuario=this.usRepo.findByEmail(email);
        if(oUsuario.isPresent()){
            throw new ExistingException
                    (String.format("El email %d ya se encuentra registrado",email)
                    );
        }
    }

}
