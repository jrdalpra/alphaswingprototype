package br.com.cooperalfa.cooperat.view.components;
import static javax.swing.layout.Layouts.*;
import javax.swing.JFrame;
import javax.swing.annotation.Properties;
import javax.swing.annotation.Property;

@SuppressWarnings("unused")
@Properties({
         @Property(name = "title", value = "#{tradutor.traduz('pais.classe','Pais')}"),
         @Property(name = "dimension", value = "800,600"),
         @Property(name = "defaultCloseOperation", value = "DISPOSE_ON_CLOSE"),
         @Property(name = "visible", value = "true")
})
public class CustomFrame extends JFrame {

   private static final long serialVersionUID = -6705288348705892466L;

}
