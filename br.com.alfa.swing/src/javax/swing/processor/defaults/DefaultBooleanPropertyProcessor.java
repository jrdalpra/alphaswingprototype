package javax.swing.processor.defaults;

import javax.swing.annotation.Property;
import javax.swing.processor.PropertyProcessor;

import net.vidageek.mirror.dsl.Mirror;

public class DefaultBooleanPropertyProcessor implements PropertyProcessor {

   @Override
   public <C> boolean accept(Property property, Class<C> componentClass) {
      try {
         Class<?> fieldType = new Mirror().on(componentClass).reflect().field(property.name()).getType();
         return fieldType.equals(boolean.class) || fieldType.equals(Boolean.class);
      } catch (NullPointerException e) {
      }
      return false;
   }

   @Override
   public <C> void process(Property property, C component) {
      new Mirror().on(component).invoke().setterFor(property.name()).withValue(Boolean.parseBoolean(property.value()));
   }

}
