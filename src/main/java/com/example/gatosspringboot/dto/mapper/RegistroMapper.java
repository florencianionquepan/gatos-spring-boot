package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.RegistroDTO;
import com.example.gatosspringboot.model.Persona;
import com.example.gatosspringboot.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class RegistroMapper implements IRegistroMapper{

    private final IUsuarioPasswordMapper usMap;

    public RegistroMapper(IUsuarioPasswordMapper usMap) {
        this.usMap = usMap;
    }

    @Override
    public Persona mapToEntity(RegistroDTO dto) {
        Persona nueva=new Persona();
        nueva.setId(dto.getId());
        nueva.setDni(dto.getDni());
        nueva.setNombre(dto.getNombre());
        nueva.setApellido(dto.getApellido());
        nueva.setTel(dto.getTel());
        nueva.setFechaNac(dto.getFechaNac());
        nueva.setDire(dto.getDire());
        nueva.setLocalidad(dto.getLocalidad());
        nueva.setEmail(dto.getUsuario().getEmail());
        if(dto.getUsuario()!=null){
            Usuario user=new Usuario();
            user.setEmail(dto.getUsuario().getEmail());
            user.setContrasenia(dto.getUsuario().getPassword());
            nueva.setUsuario(user);
        }
        return nueva;
    }
}
