package br;

import javax.annotation.PostConstruct;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.annotation.BindGroup;
import javax.swing.annotation.Bindable;
import javax.swing.annotation.Properties;
import javax.swing.annotation.Property;

import br.com.cooperalfa.model.Cliente;

@Properties({
   @Property(name = "title", value = "{pais.classe}")
})
public class FormCliente extends JFrame {

   @Properties({
            @Property(name = "text", value = "Salvar"),
            @Property(name = "enabled", value = "false"),
            @Property(name = "icon", value = "classpath:META-INF/images/salvar.png")
   })
   private JButton    salvar;

   @BindGroup({
            @Bindable(source = "cliente.nome"),
            @Bindable(source = "checkbox.selected", property = "enabled")
   })
   private JTextField nome;

   private JCheckBox  checkBox;

   // se não estiver marcado na classe, pode marcar direto na UI
   @Bindable
   private Cliente    cliente;

   // ou um método "initUI" - permitindo que o usuário mude o padrão
   @PostConstruct
   public void init() {

   }

}
