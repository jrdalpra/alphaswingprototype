package javax.swing.processor.defaults;

import javax.annotation.PostConstruct;
import javax.swing.annotation.Property;
import javax.swing.processor.PropertyProcessor;

import net.vidageek.mirror.dsl.Mirror;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.StandardBeanExpressionResolver;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DefaultExternalPropertyProcessor implements PropertyProcessor {

   @Autowired
   private ApplicationContext             spring;

   @Autowired
   @Qualifier("alfaBeanExpressionResolver")
   private StandardBeanExpressionResolver resolver;

   private BeanExpressionContext          context;

   @PostConstruct
   public void init(){
      context = new BeanExpressionContext((ConfigurableBeanFactory) ((AbstractRefreshableConfigApplicationContext)spring).getBeanFactory(), null);
   }

   @Override
   public <C> boolean accept(Property property, Class<C> componentClass) {
      return property.value().startsWith("#{") && property.value().endsWith("}");
   }

   @Override
   public <C> void process(Property property, C component) {
      new Mirror().on(component).invoke().setterFor(property.name()).withValue(resolver.evaluate(property.value(), context));
   }

}
