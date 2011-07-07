package br;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Tradutor {

   public static final Map<String, String> TRADUCAO = new HashMap<String, String>();

   static {
      TRADUCAO.put("botao.ok", "Sair");
      TRADUCAO.put("botao.sair", "Sair");
   }

   private Properties traducao;

   @Autowired
   public Tradutor(Properties traducao) {
      this.traducao = traducao;
   }
   
   public String traduz(String chave, String padrao) {
      return TRADUCAO.containsKey(chave) ? TRADUCAO.get(chave) : (traducao.containsKey(chave) ? traducao.get(chave).toString() : padrao);

   }

   public String traduz(String chave) {
      return TRADUCAO.containsKey(chave) ? TRADUCAO.get(chave) : (traducao.containsKey(chave) ? traducao.get(chave).toString() : chave);
   }

}
