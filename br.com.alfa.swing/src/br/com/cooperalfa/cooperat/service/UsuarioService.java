package br.com.cooperalfa.cooperat.service;

import org.springframework.stereotype.Service;

import br.com.cooperalfa.model.Usuario;

@Service
public class UsuarioService {

   public Boolean autentica(Usuario usuario) {
      System.out.println(usuario.getLogin());
      System.out.println(usuario.getSenha());
      return true;
   }

}
