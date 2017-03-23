//Rafael Danilo dos Santos RA 408654
package ast;

import lexer.Symbol;

public class InstanceVariable extends Variable {
	Symbol qualifier;
    public InstanceVariable( String name, Type type , Symbol qualifier) {
        super(name, type);
        this.qualifier = qualifier;
    }
    
    public Symbol getQualifier(){
    	return qualifier;
    }

}