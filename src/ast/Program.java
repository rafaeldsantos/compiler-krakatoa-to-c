//Rafael Danilo dos Santos RA 408654
package ast;

import java.util.*;
import comp.CompilationError;

public class Program {

	public Program(ArrayList<KraClass> classList, ArrayList<MetaobjectCall> metaobjectCallList, 
			       ArrayList<CompilationError> compilationErrorList) {
		this.classList = classList;
		this.metaobjectCallList = metaobjectCallList;
		this.compilationErrorList = compilationErrorList;
	}


	public void genKra(PW pw) {
		//pw.println("" + classList.size() );
		Iterator<KraClass> classe = classList.iterator();
		while (classe.hasNext()) {
			KraClass temp = classe.next();
			temp.genKra(pw);
			pw.println("");
			pw.println("");
		}
		
	}

	public void genC(PW pw) {
		pw.println("#include <malloc.h>");
		pw.println("#include <stdlib.h>");
		pw.println("#include <stdio.h>");
		pw.println("");
		pw.println("typedef int boolean;");
		pw.println("#define true 1");	
		pw.println("#define false 0");
		pw.println("");
		pw.println("typedef void (*Func)();");
		Iterator<KraClass> classe = classList.iterator();
		while (classe.hasNext()) {
			KraClass temp = classe.next();
			temp.genC(pw);
			pw.println("");
			pw.println("");
		}
		classe = classList.iterator();
		
		pw.print("int main() { ");
		pw.add();
		pw.println("");
		pw.println("");
		pw.printIdent("_class_Program *program;");
		pw.println("");
		pw.println("");
		pw.printIdent("program = new_Program();");
		pw.println("");
		pw.println("");
		pw.printIdent("( ( void (*)(_class_Program *) ) program->vt[enum_Program_run] )(program); ");
		pw.println("");
		pw.println("");
		pw.printIdent("return 0;\n");
		pw.println("");
		pw.println("}");
	}
	
	public ArrayList<KraClass> getClassList() {
		return classList;
	}


	public ArrayList<MetaobjectCall> getMetaobjectCallList() {
		return metaobjectCallList;
	}
	

	public boolean hasCompilationErrors() {
		return compilationErrorList != null && compilationErrorList.size() > 0 ;
	}

	public ArrayList<CompilationError> getCompilationErrorList() {
		return compilationErrorList;
	}

	
	private ArrayList<KraClass> classList;
	private ArrayList<MetaobjectCall> metaobjectCallList;
	
	ArrayList<CompilationError> compilationErrorList;

	
}