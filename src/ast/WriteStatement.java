//Rafael Danilo dos Santos RA 408654
package ast;

import java.util.Iterator;

public class WriteStatement extends Statement{
	ExprList list;
	public WriteStatement(ExprList list){
		this.list = list;
	}
	
	@Override
	public void genC(PW pw) {
		// TODO Auto-generated method stub
		Iterator<Expr> it =  list.elements();
		while(it.hasNext()){
			pw.println("");
			Expr var = it.next();
			if(var.getType() instanceof TypeString )
			pw.printIdent("printf(\"%s \",");
		else
			pw.printIdent("printf(\"%d \",");
		var.genC(pw, true);
		pw.print(");");
		}
	}

	@Override
	public void genKra(PW pw) {
		pw.printIdent("write(");
		list.genKra(pw);
		pw.print(");");
		
	}

}
