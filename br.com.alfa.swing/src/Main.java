import javax.swing.SwingUtilities;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

   /**
    * @param args
    */
   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            ApplicationContext spring = new ClassPathXmlApplicationContext("applicationContext.xml");
            spring.getBean(br.com.cooperalfa.cooperat.view.MainMenu.class);
         }
      });
   }

}
