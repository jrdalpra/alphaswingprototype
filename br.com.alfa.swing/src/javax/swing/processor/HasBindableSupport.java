package javax.swing.processor;

import java.beans.PropertyChangeListener;

public interface HasBindableSupport {

   void addPropertyChangeListener(String property, PropertyChangeListener listener);

   void addPropertyChangeListener(PropertyChangeListener listener);

   void removePropertyChangeListener(String property, PropertyChangeListener listener);

   void removePropertyChangeListener(PropertyChangeListener listener);

   <T> T getBean();

}
