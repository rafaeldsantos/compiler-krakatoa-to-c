//Rafael Danilo dos Santos RA 408654
package ast;

public class LiteralBoolean extends Expr {

    public LiteralBoolean( boolean value ) {
        this.value = value;
    }

    @Override
	public void genC( PW pw, boolean putParenthesis ) {
       pw.print( value ? "1" : "0" );
    }

    @Override
	public Type getType() {
        return Type.booleanType;
    }

    public static LiteralBoolean True  = new LiteralBoolean(true);
    public static LiteralBoolean False = new LiteralBoolean(false);

    private boolean value;

	@Override
	public void genKra(PW pw, boolean putParenthesis) {
		// TODO Auto-generated method stub
		//if ( putParenthesis )
	      //    pw.print("(");
		if(value){
			pw.print("true");
		}else{
			pw.print("false");
		}
		//if ( putParenthesis )
	      //    pw.print("(");
	}
}
