//Rafael Danilo dos Santos RA 408654
package ast;

public class ObjectExpr extends Expr{
	KraClass classe;
	
	public ObjectExpr(KraClass classe){
		this.classe = classe;
	}
	
	@Override
	public void genC(PW pw, boolean putParenthesis) {
		// TODO Auto-generated method stub
		pw.print("new_"+classe.getName()+"()");
	}

	@Override
	public void genKra(PW pw, boolean putParenthesis) {
		//if ( putParenthesis )
	      //    pw.print("(");
		pw.print("new "+classe.getName()+"()");
		//if ( putParenthesis )
	      //    pw.print("(");
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return classe;
		//return null;
	}

}
