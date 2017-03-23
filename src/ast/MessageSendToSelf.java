//Rafael Danilo dos Santos RA 408654
package ast;

import java.util.Iterator;

import lexer.Symbol;

public class MessageSendToSelf extends MessageSend {
	private String method;
	private String className;
	private MethodDec methodA;
	KraClass classe;
    public MessageSendToSelf(Type type, ExprList paramlist, String method, String className, MethodDec methodA, KraClass currentClass) {
		super(type, paramlist);
		// TODO Auto-generated constructor stub
		this.method = method;
		this.className =className;
		this.methodA = methodA;
		this.classe = currentClass;
	}

    
    
    public void genC( PW pw, boolean putParenthesis ) {
    	className = KraClass.OwnerMethod2(method, classe);
    	if(methodA.getQualifier() == Symbol.PUBLIC){
	    	pw.print("( ("+methodA.getType().getCname()+"(*)(");
	    	pw.print("_class_"+className+"*");
	    	ParamList plist = methodA.gerParamList();
	    	if(plist!=null){
	    		Iterator<Variable> it = plist.elements();
	    		while(it.hasNext()){
	    			pw.print(", ");
	    			Variable var = it.next();
	    			pw.print(var.getType().getCname());
//	    			if(it.hasNext()){
//	    				pw.print(",");
//	    			}
	    		}
	    	}
	    	pw.print(")");
	    	pw.print(")");
	    	pw.print("this->vt[enum_"+className+"_"+method+"]) (");
	    	pw.print("(");
	    	pw.print("_class_"+className+"*");
	    	pw.print(")");
	    	pw.print("this");
	    	if(exprlist!=null)pw.print(",");
	    	if(exprlist!=null){
				pw.print(" ");
				exprlist.genC(pw);
				pw.print(" ");
			}
			pw.print(") ");
    	} else{
    		pw.print("_"+className+"_"+method+"(");
//    		pw.print("_class_"+this.className+" *this");
    		pw.print("this");
	    	if(exprlist!=null)pw.print(",");
	    	if(exprlist!=null){
				pw.print(" ");
				exprlist.genC(pw);
				pw.print(" ");
			}
			pw.print(")");
    	}
    }

	@Override
	public void genKra(PW pw, boolean putParenthesis) {
		pw.print("this."+method+"(");
		if(exprlist!=null){
			pw.print(" ");
			exprlist.genKra(pw);
			pw.print(" ");
		}
		pw.print(")");
		
	}
    
    
}