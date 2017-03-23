//Rafael Danilo dos Santos RA 408654
package ast;

import java.util.ArrayList;
import java.util.Iterator;

public class StatementList {

    public StatementList() {
       statementList = new ArrayList<Statement>();
    }

    public void addElement(Statement statement) {
       statementList.add( statement );
    }

    public Iterator<Statement> elements() {
    	return this.statementList.iterator();
    }
    
    public int getSize() {
    	
        return statementList.size();
    }
   
    private ArrayList<Statement> statementList;
}
