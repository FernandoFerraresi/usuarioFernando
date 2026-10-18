package com.ferraresi.usuario.business;

import com.ferraresi.usuario.business.converter.UsuarioConverter;
import com.ferraresi.usuario.business.dto.UsuarioDTO;
import com.ferraresi.usuario.infrastructure.entity.Usuario;
import com.ferraresi.usuario.infrastructure.exceptions.ConflictException;
import com.ferraresi.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.ferraresi.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder PasswordEncoder;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){
       emailExiste(usuarioDTO.getEmail());
       usuarioDTO.setSenha(PasswordEncoder.encode(usuarioDTO.getSenha()));
       Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
       usuario = usuarioRepository.save(usuario);
       return usuarioConverter.paraUsuarioDTO(usuario = usuarioRepository.save(usuario));
    }

    public void emailExiste(String email){
        try {
            boolean existe = verificaEmailExistente(email);
            if (existe) {
                throw new ConflictException("Email já cadastrado" + email);
            }
        }catch (ConflictException e){
            throw new ConflictException("Email já cadastrado" + e.getCause());
        }
    }

    public boolean verificaEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario buscarusuarioPorEmail(String email){
        return usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Email Não encontrado" + email));
    }
    public void deletaUsuarioPorEmail (String email){
        usuarioRepository.deleteByEmail(email);
    }
}

