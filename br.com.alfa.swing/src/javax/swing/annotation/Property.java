package javax.swing.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({
         ElementType.TYPE,
         ElementType.FIELD
})
public @interface Property {

   int constant() default 0;

   /**
    * é uma propriedade dinamica, ou seja, será avaliada de tempo em tempo ...
    * tipo um bind???
    * 
    * @return
    */
   boolean dynamic() default false;

   String name();

   String value() default "null";
}
