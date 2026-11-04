package com.ferraresi.usuario.business;

import com.ferraresi.usuario.business.converter.UsuarioConverter;
import com.ferraresi.usuario.business.dto.UsuarioDTO;
import com.ferraresi.usuario.infrastructure.entity.Usuario;
import com.ferraresi.usuario.infrastructure.exceptions.ConflictException;
import com.ferraresi.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.ferraresi.usuario.infrastructure.repository.UsuarioRepository;
import com.ferraresi.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder PasswordEncoder;
    private final JwtUtil jwtUtil;

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

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO dto) {
        //Aqui buscamos o email do usuário através do token (tirar a obrigatoriedade do emaol)
    String email = jwtUtil.extrairEmailToken(token.substring(7));

    //Criptografia de Senha
    dto.setSenha(dto.getSenha() != null ? PasswordEncoder.encode(dto.getSenha()) : null);

    //Busca os dados do usuário no banco de dados
    Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(() ->
            new ResourceNotFoundException("Email não localizado"));

        //Mesclou os dados que recebemos na requisição DTO com os dados do banco de dados
        Usuario usuario = usuarioConverter.updateUsuario(dto, usuarioEntity);

        //Salvou os dados do usuário convertido e depois pegou o retorno e converteu para Usuario DTO
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }
}

