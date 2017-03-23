//Rafael Danilo dos Santos RA 408654
package ast;

public class TypeBoolean extends Type {

   public TypeBoolean() { super("boolean"); }

   @Override
   public String getCname() {
      return "int";
   }

   public String getName() {
	      return "boolean";
	   }
}
