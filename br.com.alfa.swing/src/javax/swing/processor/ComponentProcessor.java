package javax.swing.processor;

public interface ComponentProcessor {

   <C> C instanciate(C component);
   
   <C> C apply(C component);
   
}
