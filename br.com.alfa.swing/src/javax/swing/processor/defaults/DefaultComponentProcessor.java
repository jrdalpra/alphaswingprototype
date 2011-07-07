package javax.swing.processor.defaults;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JTree;
import javax.swing.annotation.Action;
import javax.swing.annotation.BindGroup;
import javax.swing.annotation.Bindable;
import javax.swing.annotation.InitUI;
import javax.swing.annotation.Properties;
import javax.swing.annotation.Property;
import javax.swing.bind.BindManager;
import javax.swing.bind.Binder;
import javax.swing.bind.HasBindManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.processor.ComponentProcessor;
import javax.swing.processor.PropertyProcessor;
import javax.swing.processor.matchers.AnnotationMatcher;
import javax.swing.processor.matchers.ManageableFieldsMatcher;
import javax.swing.text.JTextComponent;

import net.vidageek.mirror.dsl.AccessorsController;
import net.vidageek.mirror.dsl.Matcher;
import net.vidageek.mirror.dsl.Mirror;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import br.com.cooperalfa.swing.binding.BindingProxy;

@Component("firstComponentProcessor")
public class DefaultComponentProcessor implements ComponentProcessor, ApplicationContextAware, PropertyChangeListener,
         ChangeListener {

   private PropertyProcessor  propertyProcessor;
   private ApplicationContext spring;
   List<String>               methods = new ArrayList<String>();

   {
      methods.add("addChangeListener");
      methods.add("addWindowFocusListener");
      methods.add("addWindowListener");
   }

   public DefaultComponentProcessor() {
   }

   protected Object _new(Field field) {
      // TODO factories
      try {
         return isBindable(field) ? BindingProxy.bindable(field.getType()) : field.getType().newInstance();
      } catch (InstantiationException e) {
         e.printStackTrace();
      } catch (IllegalAccessException e) {
         e.printStackTrace();
      }
      return null;
   }

   @Override
   public <C> C apply(C component) {
      return apply(component, null);
   }

   public <C> C apply(C component, Object parent) {
      if (component == null) {
         return null;
      }
      List<Property> properties = propertiesOf(component.getClass());
      for (Property property : properties) {
         propertyProcessor().process(property, component);
      }
      List<Field> fields = fieldsOf(component.getClass());
      AccessorsController _component = new Mirror().on(component);
      Object child = null;
      for (Field field : fields) {
         child = apply(_component.get().field(field), component);
         properties = propertiesOfField(field);
         for (Property property : properties) {
            propertyProcessor().process(property, child);
         }
         applyActionsFor(child, field, component);
         bind(child, field, component);
      }
      initUI(component);
      return component;
   }

   private <C> void applyActionsFor(Object child, final Field field, final C parent) {
      // TODO ações padrões (CoC)
      if (child instanceof JButton) {
         ((JButton) child).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               new Mirror().on(parent).invoke().method(field.getAnnotation(Action.class).method()).withArgs(e);
            }
         });
      }
      if (child instanceof JTree) {
         JTree.class.cast(child).addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
               new Mirror().on(parent).invoke().method(field.getAnnotation(Action.class).method()).withArgs(e);
            }
         });
      }
   }

   private void applyListenersTo(Object source, BindManager manager) {
      if (JTextComponent.class.isInstance(source)) {
         ((JTextComponent) source).getDocument().putProperty("source", source);
         ((JTextComponent) source).getDocument().addDocumentListener(manager);
      }
      for (String method : methods) {
         try {
            new Mirror().on(source).invoke().method(method).withArgs(manager);
         } catch (Exception e) {
         }
      }
   }

   private <C> void bind(final C sourceComponent, final Field sourceField, final Object parent) {
      if (sourceComponent == null || parent == null) {
         return;
      }

      List<Field> fields = fieldsOf(parent.getClass());

      String source = null;
      for (Field field : fields) {
         if (new Mirror().on(parent).get().field(field) == sourceComponent) {
            source = field.getName();
            break;
         }
      }

      boolean contains = false;
      List<Bindable> bindables = null;
      bl_Fields: for (final Field field : fields) {
         if (isBindable(field)) {
            bindables = bindablesOf(field);
            contains = false;
            for (Bindable bindable : bindables) {
               if (bindable.source().contains(source)) {
                  contains = true;
                  break;
               }
            }
            if (!contains) {
               continue bl_Fields;
            }
            if (HasBindManager.class.isInstance(parent)) {
               final Object target = new Mirror().on(parent).get().field(field);
               ((HasBindManager) parent).getBindingManager().add(
                        spring.getBean(Binder.class).target(target).bindables(bindables).process());
               applyListenersTo(sourceComponent, ((HasBindManager) parent).getBindingManager());
               ((HasBindManager) parent).getBindingManager().apply(null);
            }
         }
      }

      List<Method> methods = new Mirror().on(parent.getClass()).reflectAll().methods();
      bl_Methods: for (Method method : methods) {
         if (isBindable(method)) {
            bindables = bindablesOf(method);
            contains = false;
            for (Bindable bindable : bindables) {
               if (bindable.source().contains(source)) {
                  contains = true;
                  break;
               }
            }
            if (!contains) {
               continue bl_Methods;
            }
            if (HasBindManager.class.isInstance(parent)) {
               final Object target = new Mirror().on(parent).invoke().method(method).withoutArgs();
               ((HasBindManager) parent).getBindingManager().add(
                        spring.getBean(Binder.class).target(target).bindables(bindables).process());
               applyListenersTo(sourceComponent, ((HasBindManager) parent).getBindingManager());
               ((HasBindManager) parent).getBindingManager().apply(null);
            }
         }
      }

   }

   private List<Bindable> bindablesOf(Field field) {
      List<Bindable> result = new ArrayList<Bindable>();
      if (field.isAnnotationPresent(Bindable.class)) {
         result.add(field.getAnnotation(Bindable.class));
      } else if (field.isAnnotationPresent(BindGroup.class)) {
         result.addAll(Arrays.asList(field.getAnnotation(BindGroup.class).value()));
      }
      return result;
   }

   private List<Bindable> bindablesOf(Method method) {
      List<Bindable> result = new ArrayList<Bindable>();
      if (method.isAnnotationPresent(Bindable.class)) {
         result.add(method.getAnnotation(Bindable.class));
      } else if (method.isAnnotationPresent(BindGroup.class)) {
         result.addAll(Arrays.asList(method.getAnnotation(BindGroup.class).value()));
      }
      return result;
   }

   private <C> Object child(Field field, C parent) {
      try {
         boolean springCandidate = false; // isSpringCandidate(field.getType());
         Object child = springCandidate ? spring.getBean(field.getType()) : (field.getType().isAnnotationPresent(
                  Bindable.class)
                  || field.isAnnotationPresent(Bindable.class) ? BindingProxy.bindable(field.getType()) : field
                  .getType().newInstance());
         List<Property> properties = propertiesOfField(field);
         properties.addAll(propertiesOf(field.getType()));
         if (!springCandidate) {
            for (Property property : properties) {
               propertyProcessor().process(property, child);
            }
            try {
               List<Field> fields = new Mirror().on(child.getClass()).reflectAll().fieldsMatching(fieldMatcher());
               for (Field fieldx : fields) {
                  // new
                  // Mirror().on(child).set().field(fieldx).withValue(child(fieldx,
                  // child));
               }
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
         applyActionsFor(child, field, parent);
         if (!springCandidate) {
            initUI(child);
         }
         return child;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   protected Matcher<Field> fieldMatcher() {
      return new ManageableFieldsMatcher();
   }

   private List<Field> fieldsOf(Class<? extends Object> clazz) {
      return new Mirror().on(clazz).reflectAll().fieldsMatching(fieldMatcher());
   }

   protected String getInitMethodName() {
      return "initUI";
   }

   private <C> void initUI(C component) {

      List<Method> methods = new Mirror().on(component.getClass()).reflectAll().methods();
      for (Method method : methods) {
         try {
            if (isAInitUICandidate(method)) {
               new Mirror().on(component).invoke().method(method).withoutArgs();
            }
         } catch (Exception e) {
         }
      }
   }

   @Override
   public <C> C instanciate(C component) {
      if (component.getClass().isAnnotationPresent(Service.class)) {
         return component;
      }
      List<Field> fields = fieldsOf(component.getClass());
      Object _new = null;
      AccessorsController _component = new Mirror().on(component);
      for (Field field : fields) {
         if (_component.get().field(field) == null) {
            _new = _new(field);
            _component.set().field(field).withValue(_new);
         }
      }

      if (HasBindManager.class.isInstance(component)) {
         applyListenersTo(component, HasBindManager.class.cast(component).getBindingManager());
      }

      return component;
   }

   protected boolean isAInitUICandidate(Method method) {
      return method.isAnnotationPresent(InitUI.class) || method.getName().equals(getInitMethodName());
   }

   private Boolean isBindable(Field field) {
      return field.getType().isAnnotationPresent(Bindable.class) || field.isAnnotationPresent(Bindable.class)
               || field.isAnnotationPresent(BindGroup.class);
   }

   private Boolean isBindable(Method method) {
      return method.isAnnotationPresent(Bindable.class) || method.getReturnType().isAnnotationPresent(Bindable.class)
               || method.isAnnotationPresent(BindGroup.class);
   }

   public void process(final Object parent, final Field current, final Bindable bind, final ExpressionParser parser,
            final EvaluationContext context) {
      Expression expression = parser.parseExpression(bind.source());
      Object value = expression.getValue(context);
      if (value != null) {
         new Mirror().on(new Mirror().on(parent).get().field(current)).invoke().setterFor(bind.property())
                  .withValue(value);
      }
   }

   protected <C> List<Property> propertiesOf(Class<C> componentClass) {
      List<Annotation> retorno = new Mirror().on(componentClass).reflectAll()
               .annotationsMatching(new AnnotationMatcher(Properties.class));
      return retorno.size() <= 0 ? propertyOf(componentClass) : Arrays.asList(((Properties) retorno.get(0)).value());
   }

   protected List<Property> propertiesOfField(Field field) {
      List<Annotation> annotations = new Mirror().on(field).reflectAll()
               .annotationsMatching(new AnnotationMatcher(Properties.class));
      return annotations.size() <= 0 ? propertyOfField(field) : Arrays
               .asList(((Properties) annotations.get(0)).value());
   }

   @Override
   public void propertyChange(PropertyChangeEvent evt) {
   }

   @SuppressWarnings({
      "unchecked"
   })
   protected <C> List<Property> propertyOf(C component) {
      List<Annotation> annotations = new Mirror().on(component.getClass()).reflectAll()
               .annotationsMatching(new AnnotationMatcher(Property.class));
      return annotations.isEmpty() ? Collections.EMPTY_LIST : Arrays.asList(((Property) annotations.get(0)));
   }

   @SuppressWarnings("unchecked")
   protected List<Property> propertyOfField(Field field) {
      List<Annotation> annotations = new Mirror().on(field).reflectAll()
               .annotationsMatching(new AnnotationMatcher(Property.class));
      return annotations.isEmpty() ? Collections.EMPTY_LIST : Arrays.asList(((Property) annotations.get(0)));
   }

   protected PropertyProcessor propertyProcessor() {
      return propertyProcessor == null ? (propertyProcessor = spring.getBean(DefaultCompositePropertyProcessor.class))
               : propertyProcessor;
   }

   @Override
   public void setApplicationContext(ApplicationContext context) throws BeansException {
      this.spring = context;
   }

   @Override
   public void stateChanged(ChangeEvent e) {

   }
}
