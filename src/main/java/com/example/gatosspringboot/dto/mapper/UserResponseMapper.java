package com.example.gatosspringboot.dto.mapper;

import com.example.gatosspringboot.dto.UserResponseDTO;
import com.example.gatosspringboot.dto.UsuarioRespDTO;
import com.example.gatosspringboot.model.Persona;
import com.example.gatosspringboot.model.Usuario;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserResponseMapper implements IUserResponseMapper{

    private final IPersonaMapper persoMapper;
    private final IRolMapper rolMapper;

    public UserResponseMapper(IPersonaMapper persoMapper, IRolMapper rolMapper) {
        this.persoMapper = persoMapper;
        this.rolMapper = rolMapper;
    }

    @Override
    public UserResponseDTO mapToDTO(Usuario user, Persona perso, boolean esTransito, boolean esPadrino) {
        UserResponseDTO dto=new UserResponseDTO();
        dto.setEmail(user.getEmail());
        dto.setNombre(perso.getNombre());
        dto.setLocalidad(perso.getLocalidad());
        dto.setRoles(user.getRoles());
        dto.setEsTransito(esTransito);
        dto.setEsPadrino(esPadrino);
        return dto;
    }

    @Override
    public List<UsuarioRespDTO> mapToListDto(HashMap<Usuario, Persona> hashmap) {
        List<UsuarioRespDTO> dtos=new ArrayList<>();
        for(Map.Entry<Usuario,Persona> entry: hashmap.entrySet()){
            Usuario usuario=entry.getKey();
            Persona perso=entry.getValue();
            if(usuario!=null && perso!=null){
                UsuarioRespDTO dto=new UsuarioRespDTO();
                dto.setId(usuario.getId());
                if(usuario.getMotivo()!=null){
                    dto.setMotivo(usuario.getMotivo());
                }
                dto.setPersona(this.persoMapper.mapToDto(perso));
                dto.setVerificado(usuario.getValidado());
                if(usuario.getHabilitado()!=null){
                    dto.setBloqueado(usuario.getHabilitado());
                }
                dto.setRoles(this.rolMapper.mapToListRolDto(usuario.getRoles()));
                dtos.add(dto);
            }
        }
        return dtos;
    }
}
