//Rafael Danilo dos Santos RA 408654
package ast;

import lexer.Symbol;

public class BreakStatement extends Statement{
	Symbol breack = Symbol.BREAK;
	@Override
	public void genC(PW pw) {
		pw.printIdent("break;");
		
	}
	
	public void genKra(PW pw) {
		pw.printIdent("break;");
		
	}

}
