package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.ExistingException;
import com.example.gatosspringboot.exception.NonExistingException;
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
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UsuarioService implements IUsuarioService {

    private final UsuarioRepository usRepo;
    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepo;
    private final IEmailService emailService;
    private ConcurrentHashMap<Long, String> tokenCache = new ConcurrentHashMap<>();
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
    public Usuario altaUsuario(Usuario usuario) {
        this.existeEmail(usuario.getEmail());
        List<Rol> rolesUser=new ArrayList<Rol>(
                List.of(this.rolRepo.findById(1).get())
        );
        usuario.setContrasenia(this.passwordEncoder.encode(usuario.getContrasenia()));
        usuario.setRoles(rolesUser);
        //inicialmente se crea como validado en false:
        usuario.setValidado(false);
        Usuario nuevo=this.usRepo.save(usuario);
        this.enviarUrlToken(nuevo);
        return nuevo;
    }

    @Override
    public Boolean validarUsuario(Long id, String token) {
        Optional<Usuario> oUsuario=this.usRepo.findById(id);
        if(oUsuario.isEmpty()){
            throw new NonExistingException("El usuario no existe");
        }
        if(!this.validarToken(id, token)){
            throw new NonExistingException(
                    "El código expiro o no es correcto." +
                            "Vuelva al sitio para solicitar el envio del link nuevamente"
            );
        }
        Usuario validado=oUsuario.get();
        validado.setValidado(true);
        this.usRepo.save(validado);
        return true;
    }

    @Override
    public void enviarValidacion(String email) {
        Optional<Usuario> oUsuario=this.usRepo.findByEmail(email);
        logger.info("ouser="+oUsuario);
        if(oUsuario.isEmpty()){
            throw new NonExistingException(
                    String.format("El usuario con el email %s no existe",email)
            );
        }
        this.enviarUrlToken(oUsuario.get());
    }

    private void enviarUrlToken(Usuario nuevo) {
        String subject="Valida tu email!";
        String token=this.generarToken(nuevo);
        String url="http://localhost:4200:/usuarios/"+nuevo.getId()+"/validacion/"+token;
        String content="\nHaga click en el siguiente link: \n"+url;
        this.emailService.sendMessage(nuevo.getEmail(),subject,content);
    }

    private String generarToken(Usuario nuevo) {
        String token = UUID.randomUUID().toString();
        tokenCache.put(nuevo.getId(),token);
        //logger.info("token cache= "+tokenCache);
        return token;
    }

    private boolean validarToken(Long id,String token) {
        String storekToken=tokenCache.get(id);
        return storekToken!=null && storekToken.equals(token);
    }


    @Override
    public Usuario agregarRolVoluntario(String email) {
        Usuario usuario=this.buscarByEmailOrException(email);
        List<Rol> roles=usuario.getRoles();
        this.validarNoExistaRolVoluntario(roles);
        Optional<Rol> oVoluRol=this.rolRepo.findById(2);
        if(oVoluRol.isEmpty()){
            throw new NonExistingException("El rol voluntario no existe en la bd");
        }
        roles.add(oVoluRol.get());
        usuario.setRoles(roles);
        String subject="Su solicitud como voluntario ha sido aceptada!";
        String content="Ya puede gestionar todos los gatitos para dar en adopcion!" +
                "\nEsperamos que disfrute formar parte de Gatshan :) ";
        this.emailService.sendMessage(email,subject,content);
        return this.usRepo.save(usuario);
    }

    private void validarNoExistaRolVoluntario(List<Rol> roles) {
        boolean existeRolConId2 = roles.stream()
                .anyMatch(rol -> rol.getId() == 2);
        if (existeRolConId2) {
            throw new RuntimeException("El usuario ya posee el rol de voluntario!");
        }
    }

    @Override
    public String modiPassword(Usuario user, String oldPassword) {
        Usuario guardado=this.buscarByEmailOrException(user.getEmail());
        String oldPassEncriptada=guardado.getContrasenia();
        if(!passwordEncoder.matches(oldPassword, oldPassEncriptada)){
            throw new ExistingException
                    (String.format("No es posible verificar tu identidad para modificar la contraseña")
                    );
        }
        guardado.setContrasenia(passwordEncoder.encode(user.getContrasenia()));
        Usuario modi=this.usRepo.save(guardado);
        return modi.getEmail();
    }

    @Override
    public Usuario altaUsuarioSocio(String email) {
        Usuario nuevoUsuario=new Usuario();
        nuevoUsuario.setEmail(email);
        String password=this.generarPasswordAleatoria();
        nuevoUsuario.setContrasenia(password);
        Usuario admin=this.altaUsuarioSocio(nuevoUsuario);
        if(!admin.getEmail().isEmpty()){
            String subject="Su cuenta como socio ha sido generada!";
            String content="\nPuede iniciar sesión con su email y su contraseña: "+password;
            this.emailService.sendMessage(email,subject,content);
        }
        return admin;
    }

    @Override
    //por ahora siempre traera sus roles de bd:
    public Usuario agregarRolSocio(Usuario user) {
        List<Rol> roles=user.getRoles();
        Optional<Rol> oSocioRol=this.rolRepo.findById(3);
        if(oSocioRol.isEmpty()){
            throw new NonExistingException("El rol socio no existe en la bd");
        }
        if(roles.stream().noneMatch(rol->rol.getId()==3)){
            roles.add(oSocioRol.get());
            user.setRoles(roles);
            return this.usRepo.save(user);
        }
        return user;
    }

    @Override
    public Usuario buscarByEmail(String email) {
        Optional<Usuario> oUser=this.usRepo.findByEmail(email);
        if(oUser.isEmpty()){
            throw new NonExistingException(
                    String.format("El usuario con email %s no existe",email));
        }
        return oUser.get();
    }

    private Usuario altaUsuarioSocio(Usuario admin){
        this.existeEmail(admin.getEmail());
        //traigo role_admin
        List<Rol> roles=new ArrayList<Rol>(
                List.of(this.rolRepo.findById(2).get())
        );
        admin.setRoles(roles);
        admin.setContrasenia(passwordEncoder.encode(admin.getContrasenia()));
        return this.usRepo.save(admin);
    }

    private Usuario buscarByEmailOrException(String email){
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
                    (String.format("El email %s ya se encuentra registrado",email)
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
