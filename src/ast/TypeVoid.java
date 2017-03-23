//Rafael Danilo dos Santos RA 408654
package ast;

public class TypeVoid extends Type {
    
    public TypeVoid() {
        super("void");
    }
    
   public String getCname() {
      return "void";
   }
   
   public String getName() {
	      return "void";
	   }
}