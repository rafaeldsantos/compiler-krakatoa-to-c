//Rafael Danilo dos Santos RA 408654
package ast;

public class ReturnStatement extends Statement{
	Expr expr;
	
	public ReturnStatement (Expr expr){
		this.expr = expr;
	}
	
	@Override
	public void genC(PW pw) {
		// TODO Auto-generated method stub
		pw.printIdent("return ");
		expr.genC(pw, false);
		pw.print(";");
	}

	@Override
	public void genKra(PW pw) {
		pw.printIdent("return ");
		expr.genKra(pw, false);
		pw.print(";");
	}

}
