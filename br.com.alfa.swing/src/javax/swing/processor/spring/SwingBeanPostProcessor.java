package javax.swing.processor.spring;

import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.processor.ComponentProcessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class SwingBeanPostProcessor implements BeanPostProcessor {

   private ComponentProcessor processor;

   @Inject
   public SwingBeanPostProcessor(@Named("firstComponentProcessor") ComponentProcessor processor) {
      this.processor = processor;
   }

   @Override
   public Object postProcessAfterInitialization(Object bean, String name) throws BeansException {
      return processor.apply(bean);
   }

   @Override
   public Object postProcessBeforeInitialization(Object bean, String name) throws BeansException {
      return processor.instanciate(bean);
   }

}
