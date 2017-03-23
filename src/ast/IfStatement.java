//Rafael Danilo dos Santos RA 408654
package ast;

public class IfStatement extends Statement{
	private Expr expr;
	private Statement ifstatement, elsestatement;
	
	public IfStatement(Expr expr, Statement ifstatement, Statement elsestatement){
		this.expr = expr;
		this.elsestatement = elsestatement;
		this.ifstatement = ifstatement;
	}
	
	@Override
	public void genC(PW pw) {
		// TODO Auto-generated method stub
				pw.printIdent("if( ");
				expr.genC(pw, false);
				pw.print(" ){");
				pw.println("");
				pw.add();
				//pw.printIdent("");
				ifstatement.genC(pw);
				pw.sub();
				pw.println("");
				if(elsestatement != null) {
					pw.printIdent("} else {");
					pw.println("");
					pw.add();
					elsestatement.genC(pw);
					pw.sub();
					pw.println("");
					pw.printIdent("}");
				}else{
					pw.printIdent("}");
					pw.println("");
				}
	}
	
	public boolean hasReturn() {
		if(ifstatement instanceof ReturnStatement) return true;
		if(elsestatement instanceof ReturnStatement) return true;	
		return false;
	}
	
	public void genKra(PW pw) {
		// TODO Auto-generated method stub
		pw.printIdent("if( ");
		expr.genKra(pw, false);
		pw.print(" ){");
		pw.println("");
		pw.add();
		//pw.printIdent("");
		ifstatement.genKra(pw);
		pw.sub();
		pw.println("");
		if(elsestatement != null) {
			pw.printIdent("} else {");
			pw.println("");
			pw.add();
			elsestatement.genKra(pw);
			pw.sub();
			pw.println("");
			pw.printIdent("}");
		}else{
			pw.printIdent("}");
			pw.println("");
		}
	}

}
