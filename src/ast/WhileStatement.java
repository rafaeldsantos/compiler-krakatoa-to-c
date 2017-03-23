//Rafael Danilo dos Santos RA 408654
package ast;

public class WhileStatement extends Statement{
	private Expr expr;
	private Statement statement;
	
	public WhileStatement(Expr expr, Statement statement){
		this.expr = expr;
		this.statement = statement;
	}
	
	@Override
	public void genC(PW pw) {
		// TODO Auto-generated method stub
		pw.printIdent("while (");
		expr.genC(pw, false);
		pw.print(")");
		pw.print("{");
		pw.add();
		pw.println("");
		statement.genC(pw);
		pw.sub();
		pw.println("");
		pw.printlnIdent("}");
		
	}
	
	public void genKra(PW pw) {
		// TODO Auto-generated method stub
		pw.printIdent("while (");
		expr.genKra(pw, false);
		pw.print(")");
		pw.printIdent("{");
		pw.add();
		pw.println("");
		statement.genKra(pw);
		pw.sub();
		pw.println("");
		pw.printlnIdent("}");
		
	}

}
