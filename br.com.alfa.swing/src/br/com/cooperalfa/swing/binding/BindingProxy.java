package br.com.cooperalfa.swing.binding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.swing.processor.HasBindableSupport;

import org.springframework.util.StringUtils;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.vidageek.mirror.dsl.Mirror;

public class BindingProxy implements MethodInterceptor, HasBindableSupport {

   @SuppressWarnings("unchecked")
   public static <T> T bindable(Class<T> clazz) {
      BindingProxy interceptor = new BindingProxy();
      Enhancer enhancer = new Enhancer();
      enhancer.setSuperclass(clazz);
      enhancer.setInterfaces(new Class[] {
         HasBindableSupport.class
      });
      enhancer.setCallback(interceptor);
      T proxy = (T) enhancer.create();
      interceptor.support = new PropertyChangeSupport(proxy);
      interceptor.bean = proxy;
      interceptor.started = true;
      return proxy;
   }

   private PropertyChangeSupport support;
   private Object                bean;
   private Boolean               started = false;

   public void addPropertyChangeListener(PropertyChangeListener listener) {
      support.addPropertyChangeListener(listener);
   }

   public void removePropertyChangeListener(PropertyChangeListener listener) {
      support.removePropertyChangeListener(listener);
   }

   @Override
   public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
      Object retValFromSuper = null;
      try {
         if (!Modifier.isAbstract(method.getModifiers())) {
            retValFromSuper = proxy.invokeSuper(obj, args);
         }
      } finally {
         if (started) {
            String methodName = method.getName();
            if (methodName.equalsIgnoreCase("addPropertyChangeListener")) {
               try {
                  new Mirror().on(obj).invoke().method(method).withArgs(args);
               } catch (Exception e) {
                  if (args.length == 1) {
                     addPropertyChangeListener((PropertyChangeListener) args[0]);
                  } else {
                     addPropertyChangeListener((String) args[0], (PropertyChangeListener) args[1]);
                  }
               }
            } else if (methodName.startsWith("set")) {
               String fieldName = fieldOfSetter(methodName);
               Object _new = args[0];
               proxy.invokeSuper(obj, args);
               support.firePropertyChange(fieldName, null, _new);
            }
         }
      }
      return retValFromSuper;
   }

   private String fieldOfSetter(String methodName) {
      return StringUtils.uncapitalize(methodName.replace("set", ""));
   }

   @SuppressWarnings("unchecked")
   @Override
   public <T> T getBean() {
      return (T) bean;
   }

   @Override
   public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
      support.addPropertyChangeListener(property, listener);
   }

   @Override
   public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
      support.removePropertyChangeListener(property, listener);
   }

}
