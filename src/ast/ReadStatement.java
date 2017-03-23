//Rafael Danilo dos Santos RA 408654
package ast;

import java.util.ArrayList;
import java.util.Iterator;

public class ReadStatement extends Statement{
	private ArrayList<String> idList;
	private ArrayList<Variable> varList;
	public ReadStatement(ArrayList<String> idList,ArrayList<Variable> varList){
		this.idList = idList;
		this.varList = varList;
	}
	@Override
	public void genC(PW pw) {
		// TODO Auto-generated method stub
//		pw.printIdent("read( ");
		//list.genKra(pw);
		Iterator<Variable> it =varList.iterator();
		while(it.hasNext()){
			Variable s = it.next();
			pw.printIdent("{");
			pw.add();
			pw.println("");
			pw.printIdent("char __s[512];");
			pw.println("");
			pw.printIdent("gets(__s);");
			pw.println("");
			pw.printIdent("sscanf(__s, \"%d\", &_"+s.getName()+");");
			pw.println("");
			pw.sub();
			pw.printIdent("}");
			
			pw.println("");

		}

	}
	@Override
	public void genKra(PW pw) {
		pw.printIdent("read( ");
		//list.genKra(pw);
		Iterator<String> it =idList.iterator();
		while(it.hasNext()){
			String s = it.next();
			//pw.print("\"");
			pw.print(s);
			//pw.print("\"");
			if(it.hasNext()){
				pw.print(", ");
			}
		}
		pw.print(" );");
		
	}

}
