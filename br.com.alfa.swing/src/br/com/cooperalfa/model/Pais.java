package br.com.cooperalfa.model;

import java.io.Serializable;

import javax.swing.annotation.Bindable;

@Bindable
public class Pais implements Serializable {

   private static final long serialVersionUID = 1349394210133012209L;

   private Long              id;

   private String            nome;

   private Boolean           ativo;

   public Boolean getAtivo() {
      return ativo;
   }

   public Long getId() {
      return id;
   }

   public String getNome() {
      return nome;
   }

   public void setAtivo(Boolean ativo) {
      this.ativo = ativo;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public void setNome(String nome) {
      this.nome = nome;
   }

}
