//Rafael Danilo dos Santos RA 408654
package ast;

public class TypeString extends Type {
    
    public TypeString() {
        super("String");
    }
    
   public String getCname() {
      return "char *";
   }
   
   public String getName() {
	      return "char *";
	   }

}