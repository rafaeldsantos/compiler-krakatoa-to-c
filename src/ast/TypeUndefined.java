//Rafael Danilo dos Santos RA 408654
package ast;

public class TypeUndefined extends Type {
    // variables that are not declared have this type
    
   public TypeUndefined() { super("undefined"); }
   
   public String getCname() {
      return "int";
   }
   
   public String getName() {
	      return "int";
	   }
}
