//Rafael Danilo dos Santos RA 408654
package ast;

import java.util.Iterator;

public class LocalVariableExpr extends Expr{
	private Type type;
	private LocalVariableList localVariableList;
	
	public LocalVariableExpr(Type type,LocalVariableList localVariableList){
		this.type = type;
		this.localVariableList = localVariableList;
	}
	
	@Override
	public void genC(PW pw, boolean putParenthesis) {
		// TODO Auto-generated method stub
		if(type!=null){
			pw.print(type.getCname());
			}
			Iterator<Variable> it = localVariableList.elements();
			while(it.hasNext()){
				Variable v = it.next();
				if(v!=null){
					pw.print(" _"+v.getName());
				}
				if(it.hasNext()){
					pw.print(", ");
				}
			}
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return this.type;
	}

	@Override
	public void genKra(PW pw, boolean putParenthesis) {
		// TODO Auto-generated method stub
		
		if(type!=null){
		pw.print(type.getName());
		}
		Iterator<Variable> it = localVariableList.elements();
		while(it.hasNext()){
			Variable v = it.next();
			if(v!=null){
				pw.print(" "+v.getName());
			}
			if(it.hasNext()){
				pw.print(", ");
			}
		}
		
	}

}
