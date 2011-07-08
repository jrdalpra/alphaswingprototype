package br.com.cooperalfa.cooperat.view;

import static javax.swing.layout.Layouts.*;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.inject.Inject;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
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
import javax.swing.validation.impl.BeanValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.cooperalfa.cooperat.business.Sessao;
import br.com.cooperalfa.model.Usuario;

@SuppressWarnings("serial")
@Component
@Scope("prototype")
@Properties({
         @Property(name = "title", value = "Entrar"),
         @Property(name = "dimension", value = "300,300"),
         @Property(name = "defaultCloseOperation", value = "DISPOSE_ON_CLOSE"),
         @Property(name = "resizable", value = "false")
})
public class LoginForm extends JDialog implements HasBindManager, WindowListener {

   @Property(name = "text", value = "Login")
   private JLabel         lbl_login;

   private JTextField     login;

   @Property(name = "text", value = "Senha")
   private JLabel         lbl_senha;

   @Bindable(source = "login.text.length > 0", property = "enabled")
   private JPasswordField senha;

   @Property(name = "text", value = "Ok")
   @Action(method = "fazLogin")
   private JButton        btn_ok;

   private BindManager    manager;

   @BindGroup({
            @Bindable(source = "login.text", property = "login"),
            @Bindable(source = "senha.text", property = "senha")
   })
   private Usuario        usuario;

   @Inject
   private BeanValidator  validador;

   @Inject
   private Sessao         sessao;

   @Autowired
   public LoginForm(MainMenu main) {
      super(main, true);
   }

   public void fazLogin(ActionEvent event) {
      validador.validate(usuario);
      if (validador.hasErrors()) {
         validador.showErrorsOn(this);
         validador.empty();
      } else {
         sessao.loga(usuario);
         setVisible(false);
      }
   }

   @Override
   public BindManager getBindingManager() {
      return this.manager == null ? this.manager = new BindManager(this) : this.manager;
   }

   @InitUI
   public void primeiroMetodo() {
      border().on(this);
      add(form(rows(row(lbl_login, login), row(lbl_senha, senha)), Panel.simple()), BorderLayout.CENTER);
      add(flow().on(Panel.simple()).center().with(btn_ok).getTarget(), BorderLayout.SOUTH);
      setLocationRelativeTo(getOwner());
      addWindowListener(this);
      setVisible(true);
   }

   @Override
   public void windowActivated(WindowEvent e) {
   }

   @Override
   public void windowClosed(WindowEvent e) {
      System.exit(0);
   }

   @Override
   public void windowClosing(WindowEvent e) {
   }

   @Override
   public void windowDeactivated(WindowEvent e) {
   }

   @Override
   public void windowDeiconified(WindowEvent e) {
   }

   @Override
   public void windowIconified(WindowEvent e) {
   }

   @Override
   public void windowOpened(WindowEvent e) {
   }

}
