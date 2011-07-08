package javax.swing;

public class Panel {

   public static JPanel simple() {
      return new JPanel();
   }

   public static JPanel titled(String title) {
      JPanel result = new JPanel();
      result.setBorder(BorderFactory.createTitledBorder(" " + title.trim() + " "));
      return result;
   }

}
