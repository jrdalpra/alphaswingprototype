package javax.swing.processor.defaults;

import javax.swing.annotation.Property;
import javax.swing.processor.PropertyProcessor;

import net.vidageek.mirror.dsl.Mirror;

public class DefaultStringPropertyProcessor implements PropertyProcessor {

   @Override
   public <C> boolean accept(Property property, Class<C> componentClass) {
      try {
         return new Mirror().on(componentClass).reflect().field(property.name()).getType().equals(String.class);
      } catch (NullPointerException e) {
      }
      return false;
   }

   @Override
   public <C> void process(Property property, C component) {
      new Mirror().on(component).invoke().setterFor(property.name()).withValue(property.value());
   }

}
