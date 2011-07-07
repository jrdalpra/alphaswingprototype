package br.com.cooperalfa.model;

import java.beans.PropertyChangeListener;
import java.io.Serializable;

import javax.swing.annotation.Bindable;

@Bindable
public class Usuario implements Serializable {

   private static final long serialVersionUID = -8755289295100479695L;

   private String            login;
   private String            senha;

   public void addPropertyChangeListener(PropertyChangeListener listener) {
   }

   public String getLogin() {
      return login;
   }

   public String getSenha() {
      return senha;
   }

   public void removePropertyChangeListener(PropertyChangeListener listener) {
   }

   public void setLogin(String login) {
      this.login = login;
   }

   public void setSenha(String senha) {
      this.senha = senha;
   }

}
