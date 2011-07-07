package javax.swing.processor.defaults;

import javax.swing.annotation.Property;
import javax.swing.processor.PropertyProcessor;
import javax.swing.text.JTextComponent;

public class DefaultTextCompomentTextPropertyProcessor implements PropertyProcessor {

   @Override
   public <C> boolean accept(Property property, Class<C> componentClass) {
      return property.name().equalsIgnoreCase("text") && JTextComponent.class.isAssignableFrom(componentClass);
   }

   @Override
   public <C> void process(Property property, C component) {
      ((JTextComponent)component).setText(property.value());
   }

}
