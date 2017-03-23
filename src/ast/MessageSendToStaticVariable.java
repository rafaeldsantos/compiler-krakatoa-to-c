//Rafael Danilo dos Santos RA 408654
package ast;

public class MessageSendToStaticVariable extends MessageSend {
	KraClass className;
	String methodName;
	KraClass classe;
    public MessageSendToStaticVariable(Type type, ExprList paramlist, KraClass className,String methodName,KraClass classe) {
		super(type, paramlist);
		// TODO Auto-generated constructor stub
		this.methodName = methodName;
		this.className = className;
		this.classe = classe;
	}

    
    public void genC( PW pw, boolean putParenthesis ) {
    	String name = KraClass.OwnerMethod2(methodName, classe);
    	pw.print("_"+name+"_"+methodName+"(");
		if(exprlist!=null){
			pw.print(" ");
			exprlist.genC(pw);
			pw.print(" ");
		}
		pw.print(");");
    }

	@Override
	public void genKra(PW pw, boolean putParenthesis) {
		// TODO Auto-generated method stub
		pw.print(className+"."+methodName+"(");
		if(exprlist!=null){
			pw.print(" ");
			exprlist.genKra(pw);
			pw.print(" ");
		}
		pw.print(");");
	}


	
}
