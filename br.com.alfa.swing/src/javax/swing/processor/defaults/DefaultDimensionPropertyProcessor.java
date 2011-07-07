package javax.swing.processor.defaults;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.annotation.Property;
import javax.swing.processor.PropertyProcessor;

public class DefaultDimensionPropertyProcessor implements PropertyProcessor {

   @Override
   public <C> boolean accept(Property property, Class<C> componentClass) {
      return Component.class.isAssignableFrom(componentClass) && property.name().equalsIgnoreCase("dimension");
   }

   @Override
   public <C> void process(Property property, C component) {
      String[] values = property.value().replace("[", "").replace("]", "").split(",");
      ((Component)component).setSize(new Dimension(Integer.parseInt(values[0]), Integer.parseInt(values[1])));
   }

}
