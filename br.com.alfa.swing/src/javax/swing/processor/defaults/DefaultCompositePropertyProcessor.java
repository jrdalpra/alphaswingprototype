package javax.swing.processor.defaults;

import java.util.ArrayList;

import javax.swing.annotation.Property;
import javax.swing.processor.PropertyProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DefaultCompositePropertyProcessor implements PropertyProcessor {

   private ArrayList<PropertyProcessor> processors;

   @Autowired
   public DefaultCompositePropertyProcessor(ApplicationContext spring) {
      processors = new ArrayList<PropertyProcessor>();
      processors.add(spring.getBean(DefaultExternalPropertyProcessor.class));
      processors.add(new DefaultStringPropertyProcessor());
      processors.add(new DefaultDimensionPropertyProcessor());
      processors.add(new DefaultTextCompomentTextPropertyProcessor());
      processors.add(new DefaultBooleanPropertyProcessor());
      processors.add(new DefaultConstantPropertyProcessor());
      processors.add(new DefaultNullPropertyProcessor());
   }

   @Override
   public <C> boolean accept(Property property, Class<C> componentClass) {
      return true;
   }

   @Override
   public <C> void process(Property property, C component) {
      if (component == null || property == null) {
         return;
      }
      boolean processed = false;
      for (PropertyProcessor processor : processors) {
         if (processor.accept(property, component.getClass())) {
            processor.process(property, component);
            processed = true;
            break;
         }
      }
      if (!processed) {
         throw new RuntimeException("não processou!" + property); // TODO
                                                                  // traduzir
      }
   }

}
