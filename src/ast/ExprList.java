//Rafael Danilo dos Santos RA 408654
package ast;

import java.util.*;

public class ExprList {

    public ExprList() {
        exprList = new ArrayList<Expr>();
    }

    public void addElement( Expr expr ) {
        exprList.add(expr);
    }

    public void genC( PW pw ) {

        int size = exprList.size();
        for ( Expr e : exprList ) {
        	e.genC(pw, false);
            if ( --size > 0 )
                pw.print(", ");
        }
    }
    
    public void genKra( PW pw ) {

        int size = exprList.size();
        for ( Expr e : exprList ) {
        	//if(e instanceof LiteralString) pw.print("\"");
        	e.genKra(pw, false);
        	//System.out.println(e.getType());
        	//if(e instanceof LiteralString) pw.print("\"");
        	if ( --size > 0 )
                pw.print(", ");
        }
    }
    
    public Iterator<Expr> elements() {
        return exprList.iterator();
    }

    private ArrayList<Expr> exprList;

}
