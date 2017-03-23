//Rafael Danilo dos Santos RA 408654
package ast;

import java.util.Iterator;

import lexer.Symbol;

public class MethodDec {
	// MethodDec ::= Qualifier Type Id "("[ FormalParamDec ] ")" "{" StatementList "}"
	private Symbol qualifier;
	private String id;
	private Type type;
	private ParamList formalParamDec;
	private StatementList statementlist;
	private LocalVariableList localVariable;
	private KraClass classe;
	
	public MethodDec (Type type, String id, Symbol qualifier, KraClass classe){
		this.qualifier = qualifier;
		this.type = type;
		this.id = id;
		this.formalParamDec = new ParamList(); //formalParamDec;
		this.statementlist = new StatementList();//statementlist;
		this.localVariable = new LocalVariableList();
		this.classe = classe;
	}
	
	public Boolean hasParam(){
		return formalParamDec.getSize() == 0;
	}
	
	public Type getType(){
		return this.type;
	}
	
	public void addStatementList(StatementList s){
		this.statementlist = s;
	}
	
	public void addLocalVariable(Variable v){
		this.localVariable.addElement(v);
	}
	
	public void addParameter(Variable v){
		this.formalParamDec.addElement(v);
	}
	
	public void addStatement(Statement s){
		this.statementlist.addElement(s);
	}
	
	public void genKra(PW pw){
		pw.printIdent(this.getQualifier()+" "+this.getType().getName()+" "+this.getId()+"(");
		if(formalParamDec != null){
			Iterator<Variable> it = formalParamDec.elements();
			while(it.hasNext()){
				Variable v = it.next();
				pw.print(v.getType().getName()+ " "+v.getName());
				if(it.hasNext()) pw.print(", ");
			}
		}
		pw.print(") {");
		pw.add();
		pw.println("");
		Iterator<Statement> it = statementlist.elements();
		while(it.hasNext()){
			Statement s = it.next();
			if(! (s instanceof NullStatement) ){
				//System.out.println(s.toString());
				s.genKra(pw);
			pw.println("");
		}
		}
		pw.println("");
		pw.sub();
		pw.printIdent("}");
	}
	
	public String getId(){
		return id;
	}
	
	public Symbol getQualifier(){
		return qualifier;
	}
	
	public ParamList gerParamList(){
		return formalParamDec;
	}

	public String getClassName(){
		return this.classe.getName();
		
	}
	
	public void genC(PW pw) {
		pw.add();
		pw.print(this.getType().getCname()+" _"+this.classe.getName()+"_"+this.getId()+"( ");
		pw.print("_class_"+classe.getName()+" *this");
		if(formalParamDec != null){
			Iterator<Variable> it = formalParamDec.elements();
			while(it.hasNext()){
				Variable v = it.next();
				
				if( !(v.getType() instanceof KraClass) ){
					pw.print(", "+v.getType().getName()+ " _"+v.getName());
				}else{
					KraClass aux = (KraClass) v.getType();
					pw.print(", "+aux.getCname()+ " _"+v.getName());
				}
			}
		}
		pw.println(") {");		
		Iterator<Statement> it = statementlist.elements();
		while(it.hasNext()){
			Statement s = it.next();
			if(! (s instanceof NullStatement) ){
				s.genC(pw);
			pw.println("");
		}
		}
		pw.println("");
		pw.sub();
		pw.printIdent("}");
		
	}
}
