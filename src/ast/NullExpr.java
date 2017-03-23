//Rafael Danilo dos Santos RA 408654
package ast;

public class NullExpr extends Expr {
    
   public void genC( PW pw, boolean putParenthesis ) {
      pw.printIdent("NULL");
   }
   
   public Type getType() {
      //# corrija
      return null;
   }

@Override
public void genKra(PW pw, boolean putParenthesis) {
	// TODO Auto-generated method stub
	pw.printIdent("null");
}
}