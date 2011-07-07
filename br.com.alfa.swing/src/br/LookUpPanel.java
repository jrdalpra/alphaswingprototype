package br;

import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.annotation.InitUI;
import javax.swing.annotation.Property;

import net.miginfocom.swing.MigLayout;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LookUpPanel extends JPanel {

   private static final long serialVersionUID = -7305925587964102073L;

   @Property(name = "text", value = "123")
   private JTextField        txt1;

   @Property(name = "text", value = "...")
   private JButton           btn1;

   @Property(name = "text", value = "123")
   private JTextField        txt2;

   @Value("#{traducao['login.lbl_login.text']}")
   private String            teste;

   public LookUpPanel() {
   }

   public JButton getBtn1() {
      return btn1;
   }

   public String getTeste() {
      return teste;
   }

   public JTextField getTxt1() {
      return txt1;
   }

   public JTextField getTxt2() {
      return txt2;
   }

   @InitUI
   public void init() {
      setLayout(new MigLayout());
      add(txt1, "sgy teste1, w 20%");
      add(btn1, "sgy teste1");
      add(txt2, "sgy teste1, w 80%");
      txt1.setText(teste);
   }

   public void setBtn1(JButton btn1) {
      this.btn1 = btn1;
   }

   @Override
   public void setEnabled(boolean enabled) {
      List<java.awt.Component> children = Arrays.asList(getComponents());
      for (java.awt.Component component : children) {
         component.setEnabled(enabled);
      }
      super.setEnabled(enabled);
   }

   public void setTeste(String teste) {
      this.teste = teste;
   }

   public void setTxt1(JTextField txt1) {
      this.txt1 = txt1;
   }

   public void setTxt2(JTextField txt2) {
      this.txt2 = txt2;
   }

}
