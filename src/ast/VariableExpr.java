//Rafael Danilo dos Santos RA 408654
package ast;

public class VariableExpr extends Expr {
    
    public VariableExpr( Variable v ) {
        this.v = v;
    }
    
    public void genC( PW pw, boolean putParenthesis ) {
    	if(v!=null){
    		pw.print( "_"+v.getName() );
    	}
    }
    
    public Type getType() {
        return v.getType();
    }
    
    private Variable v;

	@Override
	public void genKra(PW pw, boolean putParenthesis) {
		// TODO Auto-generated method stub
		//if ( putParenthesis )
	      //    pw.print("(");
		if(v!=null){
			pw.print( v.getName());
		}
		//if ( putParenthesis )
	      //s    pw.print("(");
	}
}