package br.com.cooperalfa.cooperat.view.components;

import java.awt.Component;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;

public class CustomPanel extends JPanel {

   private static final long serialVersionUID = -3614022943164737510L;

   @Override
   public void setEnabled(boolean enabled) {
      List<Component> children = Arrays.asList(getComponents());
      for (Component child : children) {
         child.setEnabled(enabled);
      }
      super.setEnabled(enabled);
   }

}
