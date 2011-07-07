package javax.swing.processor.defaults;

import javax.swing.annotation.Property;
import javax.swing.processor.PropertyProcessor;

import net.vidageek.mirror.dsl.Mirror;

public class DefaultNullPropertyProcessor implements PropertyProcessor {

   @Override
   public <C> boolean accept(Property property, Class<C> componentClass) {
      return property.value().equals("null") || property.value().equals("?");
   }

   @Override
   public <C> void process(Property property, C component) {
      new Mirror().on(component).invoke().setterFor(property.name()).withValue(new Object[0]);
   }

}
