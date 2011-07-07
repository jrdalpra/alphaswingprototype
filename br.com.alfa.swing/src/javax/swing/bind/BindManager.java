package javax.swing.bind;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.script.MirrorPropertyAccessor;

import org.springframework.expression.spel.support.StandardEvaluationContext;

public class BindManager implements ChangeListener, DocumentListener, WindowListener, WindowFocusListener {

   private List<Binder>              binders = new ArrayList<Binder>();
   private Object                    root;
   private StandardEvaluationContext context;
   private Object                    lock    = new Object();

   public BindManager(Object root) {
      this.root = root;
      context = new StandardEvaluationContext(root);
      context.addPropertyAccessor(new MirrorPropertyAccessor());
   }

   public boolean add(Binder e) {
      return binders.add(e.context(context));
   }

   public void apply(final Object event) {
      new Thread(new Runnable() {
         @Override
         public void run() {
            synchronized (lock) {
               for (Binder binder : getBinders()) {
                  binder.apply(event);
               }
            }
         }
      }).start();
   }

   @Override
   public void changedUpdate(DocumentEvent e) {
      apply(e);
   }

   protected List<Binder> getBinders() {
      return new ArrayList<Binder>(binders);
   }

   @Override
   public void insertUpdate(DocumentEvent e) {
      apply(e);
   }

   @Override
   public void removeUpdate(DocumentEvent e) {
      apply(e);
   }

   @Override
   public void stateChanged(ChangeEvent e) {
      apply(e);
   }

   @Override
   public void windowActivated(WindowEvent e) {
      apply(e);

   }

   @Override
   public void windowClosed(WindowEvent e) {
      apply(e);

   }

   @Override
   public void windowClosing(WindowEvent e) {
      apply(e);

   }

   @Override
   public void windowDeactivated(WindowEvent e) {
      apply(e);

   }

   @Override
   public void windowDeiconified(WindowEvent e) {
      apply(e);

   }

   @Override
   public void windowGainedFocus(WindowEvent e) {
      apply(e);

   }

   @Override
   public void windowIconified(WindowEvent e) {
      apply(e);

   }

   @Override
   public void windowLostFocus(WindowEvent e) {
      apply(e);

   }

   @Override
   public void windowOpened(WindowEvent e) {
      apply(e);

   }

}
