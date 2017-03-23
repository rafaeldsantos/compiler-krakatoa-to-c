//Rafael Danilo dos Santos RA 408654
package ast;

public class WritelnStatement extends Statement{
	ExprList list;
	public WritelnStatement(ExprList list){
		this.list = list;
	}
	@Override
	public void genC(PW pw) {
		// TODO Auto-generated method stub
		pw.printIdent("puts(");
		list.genC(pw);
		pw.print(");");
		pw.println("");
		pw.printIdent("printf(\"\\n\");");
		
	}
	@Override
	public void genKra(PW pw) {
		pw.printIdent("writeln(");
		list.genKra(pw);
		pw.print(");");
		
	}

}
