package javax.swing.annotation;

import java.awt.event.ActionListener;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.EventListener;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD })
public @interface Action {

   String method();
   
   Class<? extends EventListener> listener() default ActionListener.class;

   String qualifier() default "actionPerformed";

}
