package com.site.chatteste.Service;

import com.site.chatteste.config.Usuario;
import com.site.chatteste.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void cadastrarUsuario(Usuario usuario, Model model) {
    
         usuarioRepository.save(usuario);
    
   }

   public Usuario findByUsername(String usuarioNome){
    return usuarioRepository.findByUsername(usuarioNome);
   }
   
}
