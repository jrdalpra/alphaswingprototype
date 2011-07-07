package javax.swing.processor.matchers;

import java.lang.annotation.Annotation;

import net.vidageek.mirror.dsl.Matcher;

public class AnnotationMatcher implements Matcher<Annotation> {

   private Class<?> annotationClass;

   public <A> AnnotationMatcher(Class<A> annotationClass)
   {
      this.annotationClass = annotationClass;
   }
   
   @Override
   public boolean accepts(Annotation annotation) {
      return annotationClass.equals(annotation.annotationType());
   }

}
