package javax.swing.bind;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.annotation.Bindable;
import javax.swing.event.DocumentEvent;

import net.vidageek.mirror.dsl.Mirror;
import net.vidageek.mirror.invoke.dsl.InvocationHandler;

import org.springframework.context.annotation.Scope;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Binder {

   private List<Bindable>              bindables;
   private static SpelExpressionParser parser            = new SpelExpressionParser();

   private Map<Bindable, Expression>   expressions       = new HashMap<Bindable, Expression>();
   private Map<Bindable, Expression>   targetExpressions = new HashMap<Bindable, Expression>();
   private InvocationHandler<Object>   _target;
   private EvaluationContext           context;
   private Object                      target;

   public Binder() {
   }

   public void apply(Object root,
                     Object event) {
      Object _old = null, _new = null;
      if (isSource(event)) {
         return;
      }
      for (Bindable bindable : expressions.keySet()) {
         _old = targetExpressions.get(bindable).getValue(target);
         _new = expressions.get(bindable).getValue(context);
         if (_new != null && (_old == null || !_old.equals(_new))) {
            targetExpressions.get(bindable).setValue(target, _new);
         }
      }
   }

   public Binder bindables(List<Bindable> bindables) {
      this.bindables = bindables;
      return this;
   }

   public Binder context(EvaluationContext context) {
      this.context = context;
      return this;
   }

   private Boolean isSource(Object event) {
      if (event == null) {
         return false;
      }
      if (DocumentEvent.class.isInstance(event)) {
         return DocumentEvent.class.cast(event).getDocument().getProperty("source") == target;
      }
      return false;
   }

   public Binder process() {
      for (Bindable bindable : bindables) {
         expressions.put(bindable, parser.parseExpression(bindable.source()));
         targetExpressions.put(bindable, parser.parseExpression(bindable.property()));
      }
      return this;
   }

   public Binder target(Object target) {
      this.target = target;
      _target = new Mirror().on(target).invoke();
      return this;
   }
}
