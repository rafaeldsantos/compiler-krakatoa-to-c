//Rafael Danilo dos Santos RA 408654
package ast;

abstract public class Expr {
    abstract public void genC( PW pw, boolean putParenthesis );
    abstract public void genKra( PW pw, boolean putParenthesis );
      // new method: the type of the expression
    abstract public Type getType();
}