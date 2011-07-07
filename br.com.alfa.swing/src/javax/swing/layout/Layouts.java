package javax.swing.layout;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.util.Arrays;
import java.util.List;

import javax.swing.JLabel;

public class Layouts {

   public static LayoutManager form(JLabel... labels){
      return null;
   }
   
   public static FlowLayout flow(){
      return new FlowLayout(FlowLayout.CENTER);
   }
   
   public static BorderLayout border(){
      return new BorderLayout();
   }
   
   public static List<Constraint> constraints(Constraint... constraints){
      return Arrays.asList(constraints);
   }
   
   public static XAxis x(Position position){
      return new XAxis(position);
   }
   
   public static RowAxis row(Integer row){
      return null;
   }

   public static ColumnAxis column(Integer row){
      return null;
   }

   
}
