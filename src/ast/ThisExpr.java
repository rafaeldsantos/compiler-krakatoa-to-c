//Rafael Danilo dos Santos RA 408654
package ast;

public class ThisExpr extends Expr{
	KraClass classe;
	
	@Override
	public void genC(PW pw, boolean putParenthesis) {
		// TODO Auto-generated method stub
		pw.print("this");
	}
	
	public ThisExpr(KraClass classe){
		this.classe = classe;
	}

	@Override
	public void genKra(PW pw, boolean putParenthesis) {
		//if ( putParenthesis )
	      //    pw.print("(");
		pw.print("this");
		//if ( putParenthesis )
	      //    pw.print("(");
		
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return classe;
	}

}
