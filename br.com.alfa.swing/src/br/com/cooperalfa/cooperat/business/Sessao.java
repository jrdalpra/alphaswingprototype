package br.com.cooperalfa.cooperat.business;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import br.com.cooperalfa.cooperat.service.UsuarioService;
import br.com.cooperalfa.model.Usuario;

@Service
public class Sessao {

   @Inject
   private UsuarioService servico;

   private Usuario        usuario;

   public Boolean isLogado() {
      return this.usuario != null;
   }

   public void loga(Usuario usuario) {
      if (servico.autentica(usuario)) {
         this.usuario = usuario;
      } else {
         throw new RuntimeException("Usuário ou senha inválidos!");
      }
   }

   public void sair() {
      this.usuario = null;
   }

}
