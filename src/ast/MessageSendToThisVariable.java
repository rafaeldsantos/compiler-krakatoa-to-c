//Rafael Danilo dos Santos RA 408654
package ast;

import java.util.Iterator;

public class MessageSendToThisVariable  extends MessageSend { 
	String objectName;
	String methodName;
	KraClass classe;
	KraClass objectClass;
	MethodDec method;
    public MessageSendToThisVariable(Type type, ExprList paramlist, String objectName,String methodName, KraClass classe,KraClass objectClass, MethodDec method) {
		super(type, paramlist);
		// TODO Auto-generated constructor stub
		this.methodName = methodName;
		this.objectName = objectName;
		this.classe = classe;
		this.objectClass = objectClass;
		this.method = method;
	}

    
    public void genC( PW pw, boolean putParenthesis ) {
    	// TODO Auto-generated method stub
    	//receber classe do VT
    	pw.print("( ("+method.getType().getCname()+" (*) (");
    	ParamList plist = method.gerParamList();
    	pw.print("_class_"+objectClass.getName()+"*");
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
    			pw.print("this->_"+classe.getName()+"_"+objectName+"->vt[ enum_"+objectClass.getName()+"_"+methodName+"])(");
    			pw.print("this->_"+classe.getName()+"_"+objectName);
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
		pw.print("this."+objectName+"."+methodName+"(");
		if(exprlist!=null){
			pw.print(" ");
			exprlist.genKra(pw);
			pw.print(" ");
		}
		pw.print(")");
	}

    
}    

