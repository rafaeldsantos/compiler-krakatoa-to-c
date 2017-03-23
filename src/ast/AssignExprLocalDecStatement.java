//Rafael Danilo dos Santos RA 408654
package ast;

public class AssignExprLocalDecStatement extends Statement{
	private Expr expr;
	
	public AssignExprLocalDecStatement(Expr expr){
		this.expr = expr;
		
	}
	
	@Override
	public void genC(PW pw) {
		// TODO Auto-generated method stub
		if(expr!=null){
			pw.printIdent("");
			expr.genC(pw, false);
		}
		pw.print(";");
	}

	@Override
	public void genKra(PW pw) {
		if(expr!=null){
			pw.printIdent("");
			expr.genKra(pw, false);
			//Expr x = null;
			//expr.genKra(pw, false);
		}
		pw.print(";");
	}
}
