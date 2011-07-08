package br.com.cooperalfa.cooperat.view;

import static javax.swing.layout.Layouts.*;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.Panel;
import javax.swing.annotation.Action;
import javax.swing.annotation.BindGroup;
import javax.swing.annotation.Bindable;
import javax.swing.annotation.InitUI;
import javax.swing.annotation.Properties;
import javax.swing.annotation.Property;
import javax.swing.bind.BindManager;
import javax.swing.bind.HasBindManager;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.cooperalfa.model.Pais;

@Component
@Scope("prototype")
@Properties({
         @Property(name = "title", value = "Entrar"),
         @Property(name = "dimension", value = "800,600"),
         @Property(name = "defaultCloseOperation", value = "DISPOSE_ON_CLOSE")
})
public class PaisForm extends JFrame implements HasBindManager {

   private static final long serialVersionUID = 3173930668290108383L;

   private BindManager       manager;

   @Property(name = "text", value = "#{tradutor.traduz('pais.id','C�digo')}")
   private JLabel            lbl_id;
   private JTextField        id;

   @Property(name = "text", value = "#{tradutor.traduz('pais.nome','Nome')}")
   private JLabel            lbl_nome;
   private JTextField        nome;

   @Action(method = "salva")
   @Property(name = "text", value = "#{tradutor.traduz('btn.salvar','Salvar')}")
   private JButton           btn_ok;

   @BindGroup({
            @Bindable(property = "id", source = "id.text"),
            @Bindable(property = "nome", source = "nome.text")
   })
   private Pais              pais;

   @Override
   public BindManager getBindingManager() {
      return this.manager == null ? (this.manager = new BindManager(this)) : this.manager;
   }

   @InitUI
   public void initUI() {
      border().on(this);
      add(form(rows(row(lbl_id, id), row(lbl_nome, nome)), Panel.simple()), BorderLayout.CENTER);
      add(flow().on(Panel.simple()).center().with(btn_ok).getTarget(), BorderLayout.SOUTH);
      setLocationRelativeTo(null);
      setVisible(true);
   }

   public void salva(ActionEvent event) {
      System.out.println(pais.getNome());
   }

}
