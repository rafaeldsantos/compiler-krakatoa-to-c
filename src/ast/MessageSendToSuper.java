//Rafael Danilo dos Santos RA 408654
package ast;

public class MessageSendToSuper extends MessageSend { 
	String method;
	KraClass superClass;
	
    public MessageSendToSuper(Type type, ExprList paramlist, String method,KraClass superClass) {
		super(type, paramlist);
		this.method = method;
		this.superClass =superClass;
		
		
	}

    public void genC( PW pw, boolean putParenthesis ) {
    	String name = KraClass.OwnerMethod2(method, superClass);
    	pw.print("_"+name+"_"+method+"(");
    	pw.print("(_class_"+name+"*)this");
    	if(exprlist!=null) pw.print(",");
    	if(exprlist!=null){
			pw.print(" ");
			exprlist.genC(pw);
			pw.print(" ");
		}
		pw.print(")");
    }

	@Override
	public void genKra(PW pw, boolean putParenthesis) {
		// TODO Auto-generated method stub
		pw.print("super."+method+"(");
		if(exprlist!=null){
			pw.print(" ");
			exprlist.genKra(pw);
			pw.print(" ");
		}
		pw.print(")");
	}
    
}