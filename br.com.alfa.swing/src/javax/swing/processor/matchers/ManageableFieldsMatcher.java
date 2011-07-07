package javax.swing.processor.matchers;

import java.awt.Component;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.swing.annotation.Action;
import javax.swing.annotation.BindGroup;
import javax.swing.annotation.Bindable;
import javax.swing.annotation.Properties;
import javax.swing.annotation.Property;

import net.vidageek.mirror.dsl.Matcher;

public class ManageableFieldsMatcher implements Matcher<Field> {

   private List<String> packages;

   public ManageableFieldsMatcher() {
      packages = new ArrayList<String>();
      packages.add("javax.swing");
      packages.add("java.awt");
      packages.add("org.pushingpixels");
   }

   @Override
   public boolean accepts(Field field) {
      return !isDeclaredOnAPI(field) && (isAManageableClass(field) || hasManageableAnnotation(field));
   }

   private boolean hasManageableAnnotation(Field field) {
      return field.isAnnotationPresent(Action.class) || field.isAnnotationPresent(Properties.class)
               || field.isAnnotationPresent(Property.class) || field.isAnnotationPresent(Bindable.class)
               || field.isAnnotationPresent(BindGroup.class) || field.isAnnotationPresent(Inject.class);
   }

   private boolean isAManageableClass(Field field) {
      return Component.class.isAssignableFrom(field.getType()) || field.getType().isAnnotationPresent(Bindable.class);
   }

   private boolean isDeclaredOnAPI(Field field) {
      Package fieldPackage = field.getDeclaringClass().getPackage();
      if (fieldPackage != null) {
         for (String pack : packages) {
            if (fieldPackage.getName().contains(pack)) {
               return true;
            }
         }
      }
      return false;
   }

}
