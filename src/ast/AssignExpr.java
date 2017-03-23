//Rafael Danilo dos Santos RA 408654
package ast;

public class AssignExpr extends Expr{
	Expr left;
	Expr right;
	public AssignExpr(Expr left, Expr right){
		this.left = left;
		this.right = right;
		
	}
	
	@Override
	public void genC(PW pw, boolean putParenthesis) {
		// TODO Auto-generated method stub
		left.genC(pw, putParenthesis);
		if(right!= null){
			pw.print(" = ");

			right.genC(pw, putParenthesis);

		}

	}
	
	public void genKra(PW pw, boolean putParenthesis) {
		//if ( putParenthesis )
	    //      pw.print("(");
		left.genKra(pw, putParenthesis);
		if(right!= null){
			pw.print(" = ");
			//if(putParenthesis) pw.print("(");
			right.genKra(pw, putParenthesis);
			//if(putParenthesis) pw.print(")");
		}
		//if ( putParenthesis )
	      //    pw.print("(");
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return left.getType();
	}

}
