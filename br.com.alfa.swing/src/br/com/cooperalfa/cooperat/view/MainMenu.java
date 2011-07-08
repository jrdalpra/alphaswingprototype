package br.com.cooperalfa.cooperat.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.annotation.Action;
import javax.swing.annotation.Bindable;
import javax.swing.annotation.InitUI;
import javax.swing.annotation.Properties;
import javax.swing.annotation.Property;
import javax.swing.bind.BindManager;
import javax.swing.bind.HasBindManager;
import javax.swing.event.TreeSelectionEvent;

import net.miginfocom.swing.MigLayout;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import br.com.cooperalfa.cooperat.business.Sessao;
import br.com.cooperalfa.cooperat.view.components.CustomPanel;

@Component("mainMenu")
@Properties({
         @Property(name = "defaultCloseOperation", value = "EXIT_ON_CLOSE"),
         @Property(name = "dimension", value = "800,600"),
         @Property(name = "visible", value = "true"),
         @Property(name = "title", value = "Cooperat - Cooperativa Agroindustrial Alfa")
})
public class MainMenu extends JFrame implements WindowFocusListener, HasBindManager {

   private static final long  serialVersionUID = -7411162628193362018L;

   @Inject
   private ApplicationContext spring;

   @Action(method = "clicar")
   @Property(name = "text", value = "Clique")
   @Bindable(property = "enabled", source = "sessao.logado")
   private JButton            btn_teste;

   @Inject
   private Sessao             sessao;

   // TODO retirar isso de entro do component, teria que jogar em um outro lugar
   private BindManager        manager;

   @Bindable(source = "sessao.logado", property = "enabled")
   private CustomPanel        conteudo;

   @Bindable(source = "sessao.logado", property = "visible")
   private JMenuBar           menu;

   @Action(method = "aoSelecionarPrograma")
   private JTree              programas;

   @Property(value = "#{new java.util.Date().toString()}", name = "text", dynamic = true)
   private JLabel             lbl_usuario_nome;

   private JTextField         programa;

   public void aoSelecionarPrograma(TreeSelectionEvent event) {
      System.out.println(event);
   }

   public void clicar(ActionEvent event) {
      spring.getBean(PaisForm.class);
   }

   @Override
   public BindManager getBindingManager() {
      return this.manager == null ? (this.manager = new BindManager(this)) : this.manager;
   }

   @Override
   @Bindable(source = "sessao.logado", property = "visible")
   public Container getContentPane() {
      return super.getContentPane();
   }

   @SuppressWarnings("serial")
   @InitUI
   public void primeiroMetodo() {
      setLayout(new BorderLayout());
      add(conteudo, BorderLayout.CENTER);
      add(new JPanel() {
         {
            setLayout(new MigLayout("center"));
            add(new JButton("Teste"));
            add(lbl_usuario_nome);
            setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
         }
      }, BorderLayout.SOUTH);
      setLocationRelativeTo(null);
      addWindowFocusListener(this);
      conteudo.setLayout(new MigLayout("", "[40%][100%]", ""));
      conteudo.add(new JPanel() {
         {
            setLayout(new BorderLayout());
            add(programa, BorderLayout.NORTH);
            add(programas, BorderLayout.CENTER);
         }
      }, "w 100%, h 100%");
      conteudo.add(new JPanel() {
         {
            setLayout(new MigLayout());
            add(btn_teste);
         }
      }, "w 100%, h 100%");
      setJMenuBar(menu);

      JMenu atual;
      menu.add(atual = new JMenu("Arquivo"));
      atual.add(new JMenuItem("Sair"));

   }

   private void verificaLogin() {
      if (!sessao.isLogado()) {
         spring.getBean(LoginForm.class);
      }
   }

   @Override
   public void windowGainedFocus(WindowEvent e) {
      verificaLogin();
   }

   @Override
   public void windowLostFocus(WindowEvent e) {
      manager.apply(null);
   }

}
