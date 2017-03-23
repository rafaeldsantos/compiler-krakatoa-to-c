//Rafael Danilo dos Santos RA 408654
package ast;

import java.util.Iterator;

public class MessageSendToVariable extends MessageSend { 
	String objectName;
	String methodName;
	String className;
	MethodDec method;
    public MessageSendToVariable(Type type, ExprList paramlist, String objectName,String methodName,String className,MethodDec method) {
		super(type, paramlist);
		// TODO Auto-generated constructor stub
		this.methodName = methodName;
		this.objectName = objectName;
		this.className = className;
		this.method = method;
	}

    
    public void genC( PW pw, boolean putParenthesis ) {
    	
    	pw.print("( ("+method.getType().getCname()+" (*) (");
    	ParamList plist = method.gerParamList();
    	pw.print("_class_"+className+"*");
    	if(plist!=null){
    		Iterator<Variable> it = plist.elements();
    		while(it.hasNext()){
    			pw.print(",");
    			Variable var = it.next();
    			pw.print(var.getType().getCname());
//    			if(it.hasNext()){
//    				pw.print(",");
//    			}
    		}
    	}
    	pw.print("))");
    	pw.print( "_"+objectName+"->vt["+"enum_"+className+"_"+methodName+"]) (_"+objectName);
		if(exprlist!=null){
			pw.print(", ");
			exprlist.genC(pw);
			pw.print(" ");
		}
		pw.print(")");
    }

	@Override
	public void genKra(PW pw, boolean putParenthesis) {
		// TODO Auto-generated method stub
		pw.print(objectName+"."+methodName+"(");
		if(exprlist!=null){
			pw.print(" ");
			exprlist.genKra(pw);
			pw.print(" ");
		}
		pw.print(")");
	}

    
}    