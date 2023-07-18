package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.ExistingException;
import com.example.gatosspringboot.model.Rol;
import com.example.gatosspringboot.model.Usuario;
import com.example.gatosspringboot.repository.database.RolRepository;
import com.example.gatosspringboot.repository.database.UsuarioRepository;
import com.example.gatosspringboot.service.interfaces.IEmailService;
import com.example.gatosspringboot.service.interfaces.IUsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements IUsuarioService {

    private final UsuarioRepository usRepo;
    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepo;
    private final IEmailService emailService;
    private Logger logger= LoggerFactory.getLogger(UsuarioService.class);

    public UsuarioService(UsuarioRepository usRepo,
                          PasswordEncoder passwordEncoder,
                          RolRepository rolRepo,
                          IEmailService emailService) {
        this.usRepo = usRepo;
        this.passwordEncoder = passwordEncoder;
        this.rolRepo = rolRepo;
        this.emailService = emailService;
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
        Usuario creado=this.usRepo.save(us);
        return creado.getMail();
    }

    @Override
    public Usuario altaUsuarioVoluntario(String email) {
        Usuario nuevoUsuario=new Usuario();
        nuevoUsuario.setMail(email);
        String password=this.generarPasswordAleatoria();
        nuevoUsuario.setContrasenia(password);
        String emailUsuarioCreado=this.altaUsuarioCompleto(nuevoUsuario);
        if(!emailUsuarioCreado.isEmpty()){
            String subject="Su cuenta como voluntario ha sido generada!";
            String content="Puede iniciar sesión con su email y su contraseña: "+password;
            this.emailService.sendMessage(email,subject,content);
        }
        Optional<Usuario> oUsuario=this.usRepo.findByEmail(emailUsuarioCreado);
        if(oUsuario.isEmpty()) {
            throw new ExistingException
                    (String.format("Fallo al crear el usuario")
                    );
        }
        return oUsuario.get();
    }

    @Override
    public String modiPassword(Usuario user, String oldPassword) {
        Usuario guardado=this.buscarByEmail(user.getMail());
        String oldPassEncriptada=guardado.getContrasenia();
        if(!passwordEncoder.matches(oldPassword, oldPassEncriptada)){
            throw new ExistingException
                    (String.format("No es posible verificar tu identidad para modificar la contraseña")
                    );
        }
        guardado.setContrasenia(passwordEncoder.encode(user.getContrasenia()));
        Usuario modi=this.usRepo.save(guardado);
        return modi.getMail();
    }

    private Usuario buscarByEmail(String email){
        Optional<Usuario> oUsuario=this.usRepo.findByEmail(email);
        if(oUsuario.isEmpty()){
            throw new ExistingException
                    (String.format("El usuario con el email %d no existe",email)
                    );
        }
        return oUsuario.get();
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

    private String generarPasswordAleatoria(){
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[4];
        secureRandom.nextBytes(randomBytes);
        StringBuilder sb = new StringBuilder();
        for (byte b : randomBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}
