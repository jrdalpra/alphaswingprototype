package javax.swing.processor.defaults;

import java.util.HashMap;
import java.util.Map;

import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.annotation.Property;
import javax.swing.processor.PropertyProcessor;

import net.vidageek.mirror.dsl.Mirror;

public class DefaultConstantPropertyProcessor implements PropertyProcessor, WindowConstants {

   public Map<String, Object> properties = new HashMap<String, Object>();

   public DefaultConstantPropertyProcessor() {
      properties.put("EXIT_ON_CLOSE", EXIT_ON_CLOSE);
      properties.put("DISPOSE_ON_CLOSE", DISPOSE_ON_CLOSE);
      properties.put("SINGLE_SELECTION", ListSelectionModel.SINGLE_SELECTION);
      properties.put("SINGLE", ListSelectionModel.SINGLE_SELECTION);
      properties.put("MULTIPLE_INTERVAL_SELECTION", ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
      properties.put("MULTIPLE", ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
      properties.put("SINGLE_INTERVAL_SELECTION", ListSelectionModel.SINGLE_INTERVAL_SELECTION);
      properties.put("SINGLE_INTERVAL", ListSelectionModel.SINGLE_INTERVAL_SELECTION);
      // TODO outras constantes
   }

   @Override
   public <C> boolean accept(Property property,
                             Class<C> componentClass) {
      return properties.containsKey(property.value());
   }

   @Override
   public <C> void process(Property property,
                           C component) {
      new Mirror().on(component).invoke().setterFor(property.name()).withValue(properties.get(property.value()));
   }

}
