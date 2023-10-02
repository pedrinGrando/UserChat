package com.site.chatteste.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.site.chatteste.Repository.UsuarioRepository;
import com.site.chatteste.Service.UsuarioService;
import com.site.chatteste.config.Usuario;

@Controller
public class UsuarioController {
    
    //ATRIBUTOS
    private UsuarioService usuarioService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    //CRIACAO DO SERVICE NO CONTROLLER 
    @Autowired
    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    //USUARIO REPOSITORY
    @Autowired
    private UsuarioRepository usuarioRepository;

    //MAPEAMENTO DO CADASTRO E LOGIN

    @PostMapping("/cadastro")
    public String cadastrarUsuario(@ModelAttribute("usuario") Usuario usuario, Model model) {
     //Criptografar a senha antes de salvar
    String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
    usuario.setSenha(senhaCriptografada);

         usuarioService.cadastrarUsuario(usuario, model);

    return "redirect:/login"; // Redirecionar para a página de login após o cadastro
}

    //Caso de erro no login, ele retorna para pagina de login
    @GetMapping("/loginValida")
    public String loginPage() {
        return "login";
    }

}
