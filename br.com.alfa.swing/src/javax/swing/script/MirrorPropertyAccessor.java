package javax.swing.script;

import net.vidageek.mirror.dsl.Mirror;
import net.vidageek.mirror.exception.MirrorException;

import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;
import org.springframework.stereotype.Component;

@Component
public class MirrorPropertyAccessor implements PropertyAccessor {

   @SuppressWarnings("rawtypes")
   @Override
   public Class[] getSpecificTargetClasses() {
      return null; // can work with all classes
   }

   @Override
   public boolean canRead(EvaluationContext context, Object target, String name) throws AccessException {
      return true;
   }

   @Override
   public TypedValue read(EvaluationContext context, Object target, String name) throws AccessException {
      try {
         return new TypedValue(new Mirror().on(target).invoke().getterFor(name));
      } catch (MirrorException e1) {
         try {
            return new TypedValue(new Mirror().on(target).get().field(name));
         } catch (MirrorException e2) {
            try {
               return new TypedValue(new Mirror().on(target).invoke().method(name).withoutArgs());
            } catch (MirrorException e3) {
               throw new AccessException("Error", e3);
            }
         }
      }
   }

   @Override
   public boolean canWrite(EvaluationContext context, Object target, String name) throws AccessException {
      return true;
   }

   @Override
   public void write(EvaluationContext context, Object target, String name, Object newValue) throws AccessException {
      try {
         new Mirror().on(target).invoke().setterFor(name).withValue(newValue);
      } catch (MirrorException e1) {
         try {
            new Mirror().on(target).invoke().method(name).withArgs(newValue);
         } catch (MirrorException e2) {
            try {
               new Mirror().on(target).set().field(name).withValue(newValue);
            } catch (MirrorException e3) {
               throw new AccessException("Error", e3);
            }
         }
      }
   }
}
