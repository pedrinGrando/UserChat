package com.site.chatteste.Controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.site.chatteste.Repository.UsuarioRepository;
import com.site.chatteste.Service.UsuarioService;
import com.site.chatteste.config.Usuario;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UsuarioController {

    @Autowired
    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private Usuario usuarioLogado;

   @GetMapping("/")
   public String iniciarSistema(){
      return "login";
   }

     @GetMapping("/loginValida")
      public String realizarLogin(){
        return "login";
      }

      //Método para validar as informações do Usuario para o Login 
    @PostMapping("/loginValida")
    public String login(@RequestParam("usuarioNome") String usuarioNome, @RequestParam("senha") String senha, Model model, HttpSession session) {
        // banco de dados para verificar se o usuário existe
        Usuario usuario = usuarioService.findByUsername(usuarioNome);
        usuarioLogado = usuario;

        boolean valida = false;
        if (usuario != null) {
            // Verificar se a senha fornecida corresponde à senha criptografada no banco de dados
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(senha, usuario.getSenha())) {
                // Autenticação bem-sucedida
                session.setAttribute("usuario", usuario); // Armazena o usuário na sessão
                model.addAttribute("usernameSessao", usuario);
                valida  = true;
            }
        }
        if (valida) {
            return "redirect:/chat";
        } else {
            // Autenticação falhou
            model.addAttribute("mensagemLogin", "Incorrect password or username");
            return "login";
        }
    }

      @GetMapping("/startChat")
      public String trocarUsuario(Model model){

          model.addAttribute("usernameSessao", usuarioLogado);
        return "login";
      }


    @GetMapping("/chat")
    public String iniciarChat(){

      return "chat";
    }

    //MAPEAMENTO DO CADASTRO
    @GetMapping("/cadastro")
    public String exibirFormularioCadastro() {
        return "login"; // 
    }

    @PostMapping("/cadastro")
    public String cadastrarUsuario(@ModelAttribute("usuario") Usuario usuario, Model model) {
    // Criptografar a senha antes de salvar
      String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
      usuario.setSenha(senhaCriptografada);

        try {
         usuarioService.cadastrarUsuario(usuario, model);
         return "chat"; 
            
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("mensagem", "Existem dados que já estão cadastrados!");
            return "login"; 
        }
    }



}
    
