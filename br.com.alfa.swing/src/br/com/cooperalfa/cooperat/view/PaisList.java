package br.com.cooperalfa.cooperat.view;

import static javax.swing.layout.Layouts.*;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.annotation.PreDestroy;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.Panels;
import javax.swing.annotation.Action;
import javax.swing.annotation.Properties;
import javax.swing.annotation.Property;
import javax.swing.bind.BindManager;
import javax.swing.bind.HasBindManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.model.table.EntityTableModel;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.cooperalfa.cooperat.view.components.CustomFrame;
import br.com.cooperalfa.model.Pais;
import br.com.cooperalfa.swing.binding.BindingProxy;

@Component
@Scope("prototype")
@Properties({
   @Property(name = "title", value = "#{tradutor.traduz('pais.lista','Lista de Paises')}")
})
public class PaisList extends CustomFrame implements HasBindManager, ApplicationContextAware {

   private static final long  serialVersionUID = -6540520398843493937L;
   private BindManager        manager;

   @Properties({
            @Property(name = "fillsViewportHeight", value = "true"),
            @Property(name = "autoCreateRowSorter", value = "true"),
            @Property(name = "selectionMode", value = "SINGLE")
   })
   private JTable             grid;

   @Properties({
      @Property(name = "text", value = "#{tradutor.traduz('btn.novo','Novo')}")
   })
   @Action(method = "novo")
   private JButton            btn_novo;
   private ApplicationContext spring;

   @PreDestroy
   public void destroy() {
      System.out.println("asdfasdf");
   }

   @Override
   public BindManager getBindingManager() {
      return this.manager == null ? (this.manager = new BindManager(this)) : this.manager;
   }

   public void initUI() {
      border().on(this);
      add(page().on(Panels.simple()).with(flow().on(Panels.simple()).with(btn_novo).getTarget(), Panels.scroll(grid))
                .getTarget(), BorderLayout.CENTER);
      grid.setModel(new EntityTableModel().forEntity(Pais.class));
      grid.getColumnModel().getColumn(0).setPreferredWidth(50);
      grid.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent e) {
            System.out.println(e);
         }
      });
   }

   public void novo(ActionEvent novo) {
      Pais pais = BindingProxy.bindable(Pais.class);
      pais.setId(10l);
      pais.setNome("Brasil");
      spring.getBean("paisForm", pais);

   }

   @Override
   public void setApplicationContext(ApplicationContext spring) throws BeansException {
      this.spring = spring;
   }

}
