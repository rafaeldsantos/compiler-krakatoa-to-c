//Rafael Danilo dos Santos RA 408654
package ast;

public class ThisVariableExpr extends Expr{
	VariableExpr var;
	KraClass classe;
	
	public ThisVariableExpr(VariableExpr var, KraClass currentClass){
		this.var = var;
		this.classe = currentClass;
	}
	
	@Override
	public void genC(PW pw, boolean putParenthesis) {
		// TODO Auto-generated method stub
		pw.print("this->"+"_"+classe.getName()+"");
		var.genC(pw, false);
	}

	@Override
	public void genKra(PW pw, boolean putParenthesis) {
		//if ( putParenthesis )
	      //    pw.print("(");
		pw.print("this.");
		var.genKra(pw, false);
		//if ( putParenthesis )
	      //    pw.print("(");
		
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return var.getType();
	}
}
