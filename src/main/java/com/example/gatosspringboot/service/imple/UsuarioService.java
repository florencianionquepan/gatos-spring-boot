package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.exception.ExistingException;
import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.model.Persona;
import com.example.gatosspringboot.model.Rol;
import com.example.gatosspringboot.model.Socio;
import com.example.gatosspringboot.model.Usuario;
import com.example.gatosspringboot.repository.database.PersonaRepository;
import com.example.gatosspringboot.repository.database.RolRepository;
import com.example.gatosspringboot.repository.database.SocioRepository;
import com.example.gatosspringboot.repository.database.UsuarioRepository;
import com.example.gatosspringboot.service.interfaces.IEmailService;
import com.example.gatosspringboot.service.interfaces.INotificacionService;
import com.example.gatosspringboot.service.interfaces.IUsuarioService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UsuarioService implements IUsuarioService {

    private final UsuarioRepository usRepo;
    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepo;
    private final IEmailService emailService;
    private final PersonaRepository persoRepo;
    private final SocioRepository socioRepo;
    private final INotificacionService notiService;
    private ConcurrentHashMap<Long, String> tokenCache = new ConcurrentHashMap<>();
    private Logger logger= LoggerFactory.getLogger(UsuarioService.class);

    public UsuarioService(UsuarioRepository usRepo,
                          PasswordEncoder passwordEncoder,
                          RolRepository rolRepo,
                          IEmailService emailService,
                          PersonaRepository persoRepo,
                          SocioRepository socioRepo,
                          INotificacionService notiService) {
        this.usRepo = usRepo;
        this.passwordEncoder = passwordEncoder;
        this.rolRepo = rolRepo;
        this.emailService = emailService;
        this.persoRepo = persoRepo;
        this.socioRepo = socioRepo;
        this.notiService = notiService;
    }

    @Override
    public HashMap<Usuario, Persona> verTodos() {
        List<Usuario> usuarios= (List<Usuario>) this.usRepo.findAll();
        HashMap<Usuario,Persona> usuarioPersonaMap=new HashMap<>();
        for(Usuario usuario:usuarios){
            Optional<Persona> oPerso=this.persoRepo.findByEmail(usuario.getEmail());
            if(oPerso.isPresent()){
                usuarioPersonaMap.put(usuario,oPerso.get());
            }
        }
        return usuarioPersonaMap;
    }

    @Override
    public Usuario bloquearUsuario(Long id, String motivo) {
        Usuario user=this.findByIdOrException(id);
        user.setMotivo(motivo);
        if(user.getHabilitado()!=null && !user.getHabilitado()){
            throw new NonExistingException("El usuario ya esta bloqueado");
        }
        user.setHabilitado(false);
        return this.usRepo.save(user);
    }

    @Override
    public Usuario desbloquearUsuario(Long id, String motivo) {
        Usuario user=this.findByIdOrException(id);
        user.setMotivo(motivo);
        if(user.getHabilitado()!=null && user.getHabilitado()){
            throw new NonExistingException("El usuario ya esta desbloqueado");
        }
        user.setHabilitado(true);
        return this.usRepo.save(user);
    }

    private Usuario findByIdOrException(Long id){
        Optional<Usuario> oUser=this.usRepo.findById(id);
        if(oUser.isEmpty()){
            throw new NonExistingException(
                    String.format("El usuario con id %d no existe",id)
            );
        }
        return oUser.get();
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
        String url="http://localhost:4200/usuarios/"+nuevo.getId()+"/validacion/"+token;
        //String content="\nHaga click en el siguiente link: \n"+url;
        String content = "<p style=\"font-size: 20px;text-align: center;\">Haga clic en el siguiente botón:</p>";
        content += "<p style=\"text-align: center;\"><a href=\"" + url + "\" style=\"display: inline-block; background-color: #F5CDFF; color: #202124; padding: 10px 20px; text-decoration: none; border-radius: 10px; font-weight: bold; font-size: 16px;\">Validar Email</a></p>";
        content += "<p style=\"font-size: 15px;text-align: center;\">O copie y pegue la url: "+url+"</p>";
        String emailContent = "<html><head><style>"
                + "p { font-size: 20px; }"
                + "a { display: inline-block; background-color: #F5CDFF; color: #202124; padding: 10px 20px; text-decoration: none; border-radius: 10px; font-weight: bold; font-size: 16px; }"
                + "</style></head><body>" + content + "</body></html>";
        this.emailService.sendMessage(nuevo.getEmail(),subject,emailContent);
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
    @Transactional
    public Usuario agregarRolSocio(Long id) {
        Usuario user=this.findByIdOrException(id);
        Optional<Persona> oPerso=this.persoRepo.findByEmail(user.getEmail());
        if(oPerso.isEmpty()){
            throw new NonExistingException("Los datos personales del usuario no existen");
        }
        List<Rol> roles=user.getRoles();
        if (roles.stream()
                .anyMatch(rol->"ROLE_SOCIO".equals(rol.getNombre()))){
            throw new ExistingException("El usuario ya tiene rol admin");
        }
        Optional<Rol> oSocioRol=this.rolRepo.findById(3);
        if(oSocioRol.isEmpty()){
            throw new NonExistingException("El rol socio no existe en la bd");
        }
        roles.add(oSocioRol.get());
        user.setRoles(roles);
        Socio socio=new Socio(null,oPerso.get(),null);
        this.socioRepo.save(socio);
        this.notiService.nuevoRolSocio(oPerso.get());
        return this.usRepo.save(user);
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

    //Esto no se esta usando-> el rol socio se le da a usuarios existentes
/*    @Override
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

    private String generarPasswordAleatoria(){
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[4];
        secureRandom.nextBytes(randomBytes);
        StringBuilder sb = new StringBuilder();
        for (byte b : randomBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }*/

}
