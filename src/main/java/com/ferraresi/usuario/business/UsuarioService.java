package com.ferraresi.usuario.business;

import com.ferraresi.usuario.business.converter.UsuarioConverter;
import com.ferraresi.usuario.business.dto.UsuarioDTO;
import com.ferraresi.usuario.infrastructure.entity.Usuario;
import com.ferraresi.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){
       Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
       usuario = usuarioRepository.save(usuario);
       return usuarioConverter.paraUsuarioDTO(usuario = usuarioRepository.save(usuario));
    }

}
