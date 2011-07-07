package javax.swing.processor;

import javax.swing.annotation.Property;

public interface PropertyProcessor {

   <C> boolean accept(Property property, Class<C> componentClass);
   <C> void process(Property property, C component);
}
