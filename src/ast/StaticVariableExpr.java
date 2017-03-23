//Rafael Danilo dos Santos RA 408654
package ast;

public class StaticVariableExpr extends Expr{
	Variable var;
	KraClass className;
	
	 public StaticVariableExpr(KraClass className, Variable var) {
	        this.var = var;
	        this.className = className;
	    }	
	
	@Override
	public void genC(PW pw, boolean putParenthesis) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genKra(PW pw, boolean putParenthesis) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return var.getType();
	}

}
