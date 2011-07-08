package javax.swing;

import java.awt.Component;

public class Panels {

   public static JScrollPane scroll(Component view) {
      JScrollPane result = new JScrollPane(view);
      result.setBorder(BorderFactory.createEmptyBorder());
      return result;
   }

   public static JPanel simple() {
      return new JPanel();
   }

   public static JPanel titled(String title) {
      JPanel result = new JPanel();
      result.setBorder(BorderFactory.createTitledBorder(" " + title.trim() + " "));
      return result;
   }

}
