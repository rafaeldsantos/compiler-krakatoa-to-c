//Rafael Danilo dos Santos RA 408654
package ast;


abstract public class MessageSend  extends Expr  {
	protected Type type;
	protected ExprList exprlist;
//	protected String myclassname;
	public MessageSend(Type type, ExprList exprlist){
    	this.type = type;
    	this.exprlist = exprlist;
//    	this.myclassname = null;
	}
	public Type getType() { 
        return this.type;
    }
	
//	public getClassName(){
//		/
//	}
}

