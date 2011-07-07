package br;

import static javax.swing.layout.Layouts.border;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusListener;
import java.util.Date;

import javax.inject.Inject;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.annotation.Action;
import javax.swing.annotation.BindGroup;
import javax.swing.annotation.Bindable;
import javax.swing.annotation.InitUI;
import javax.swing.annotation.Properties;
import javax.swing.annotation.Property;
import javax.swing.bind.BindManager;
import javax.swing.bind.HasBindManager;
import javax.swing.model.table.EntityTableModel;

import net.miginfocom.swing.MigLayout;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import br.com.cooperalfa.model.Usuario;

//@Component
//@Scope("prototype")
@Properties({
         @Property(name = "title", value = "#{tradutor.traduz('login.title','Entrar')}"),
         @Property(name = "visible", value = "true"),
         @Property(name = "dimension", value = "800,600"),
         @Property(name = "alwaysOnTop", value = "true"),
         @Property(name = "defaultCloseOperation", value = "EXIT_ON_CLOSE")
})
public class LoginFormTeste extends JFrame implements ApplicationContextAware, HasBindManager {

   private static final long  serialVersionUID = -6040427781663021616L;

   @BindGroup({
            @Bindable(source = "checkbox.selected", property = "enabled"),
            @Bindable(source = "usuario.login", property = "text")
   })
   @Action(method = "aoSairDoCampoLogin", listener = FocusListener.class, qualifier = "focusLost")
   private JTextField         login;

   @Bindable(source = "login.text.length > 0 and login.enabled", property = "enabled")
   private JPasswordField     senha;

   private JCheckBox          checkbox;

   @BindGroup({
            @Bindable(source = "login.text", property = "login"),
            @Bindable(source = "senha.text", property = "senha")
   })
   private Usuario            usuario;

   @Property(name = "text", value = "#{traducao['login.lbl_login.text']}")
   private JLabel             lbl_login;

   @Property(name = "text", value = "Senha")
   @Bindable(source = "login.text.length > 0", property = "visible")
   private JLabel             lbl_senha;

   @Action(method = "aoClicarNoBotaoOk")
   @Properties({
      @Property(name = "text", value = "#{(1 == 1).toString()}")
   })
   @Bindable(source = "checkbox.selected", property = "enabled")
   private JButton            btn_ok;

   @Action(method = "aoClicarNoBotaoSair")
   @Properties({
      @Property(name = "text", value = "#{tradutor.traduz('botao.sair')}")
   })
   private JButton            btn_sair;

   @Inject
   @BindGroup({
            @Bindable(property = "enabled", source = "checkbox.selected"),
            @Bindable(property = "txt1.text", source = "login.text")
   })
   private LookUpPanel        interno;

   @SuppressWarnings("unused")
   private ApplicationContext context;

   @Bindable(source = "checkbox.selected", property = "visible")
   private JTable             tabela;

   private BindManager        manager;

   public void aoClicarNoBotaoOk(ActionEvent event) {
      System.out.println(usuario.getLogin());
      System.out.println(usuario.getSenha());
   }

   public void aoClicarNoBotaoSair(ActionEvent event) {
      usuario.setLogin(new Date().toString());
   }

   @Override
   public BindManager getBindingManager() {
      return this.manager == null ? (this.manager = new BindManager(this)) : this.manager;
   }

   @SuppressWarnings("serial")
   @InitUI
   public void init() {

      setLayout(border());
      add(new JPanel() {
         {
            setLayout(new MigLayout("", "[30%][100%]", ""));
            add(lbl_login, "align right");
            add(login, "growx, wrap");
            add(lbl_senha, "align right");
            add(senha, "width 30%, wrap");
            add(interno, "growx, spanx 2, wrap");
            add(checkbox, "wrap");
            add(new JScrollPane(tabela), "growx, spanx 2, h 100%");
         }
      }, BorderLayout.CENTER);
      add(new JPanel() {
         {
            setLayout(new MigLayout("center"));
            add(btn_ok, "sg btns");
            add(btn_sair, "sg btns");
         }
      }, BorderLayout.SOUTH);
      tabela.setModel(new EntityTableModel().forEntity(Usuario.class));
      validate();
   }

   @Override
   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      this.context = applicationContext;
   }

}
