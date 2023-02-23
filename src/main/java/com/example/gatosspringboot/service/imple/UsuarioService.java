package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.model.Rol;
import com.example.gatosspringboot.model.Usuario;
import com.example.gatosspringboot.repository.database.UsuarioRepository;
import com.example.gatosspringboot.service.interfaces.IUsuarioService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UsuarioService implements IUsuarioService {

    private final UsuarioRepository usRepo;

    public UsuarioService(UsuarioRepository usRepo) {
        this.usRepo = usRepo;
    }

    @Override
    public List<Usuario> verTodos() {
        return (List<Usuario>) this.usRepo.findAll();
    }

    @Override
    public Usuario altaUsuario(Usuario us) {
        List<Rol> roles=new ArrayList<Rol>(
                Arrays.asList(
                        new Rol(1,"user",new ArrayList<Usuario>())
                        )
        );
        us.setRoles(roles);
        return this.usRepo.save(us);
    }
}
