//Rafael Danilo dos Santos RA 408654
package ast;

import lexer.Symbol;

public class NullStatement extends Statement{
	Symbol Null = Symbol.NULL;
	@Override
	public void genC(PW pw) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void genKra(PW pw) {
		//pw.print("null");
		
	}

}
