//Rafael Danilo dos Santos RA 408654
package ast;

import java.util.Iterator;

public class CompositeStatement extends Statement{
	StatementList statementList;
	
	public CompositeStatement(StatementList statementList){
		this.statementList = statementList;
	}
	
	@Override
	public void genC(PW pw) {
		// TODO Auto-generated method stub
		Iterator<Statement> it = this.statementList.elements();
		while(it.hasNext()){
			Statement state = it.next();
			state.genC(pw);
			pw.println("");
		}
	}

	@Override
	public void genKra(PW pw) {
		// TODO Auto-generated method stub
		Iterator<Statement> it = this.statementList.elements();
		while(it.hasNext()){
			Statement state = it.next();
			state.genKra(pw);
			pw.println("");
		}
	}

}
