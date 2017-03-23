//Rafael Danilo dos Santos RA 408654
package ast;

import java.util.*;

public class InstanceVariableList {

    public InstanceVariableList() {
       instanceVariableList = new ArrayList<InstanceVariable>();
    }

    public void addElement(InstanceVariable instanceVariable) {
       instanceVariableList.add( instanceVariable );
    }

    public Iterator<InstanceVariable> elements() {
    	return this.instanceVariableList.iterator();
    }

    public int getSize() {
        return instanceVariableList.size();
    }

    public void genKra(PW pw) {
        Iterator<InstanceVariable> it = this.elements();
    	while(it.hasNext()){
    		InstanceVariable var = it.next();
    		pw.printIdent(var.getType().name +" "+var.getName()+";");
    		pw.println("");
    	}
    }
    
    public void genC(PW pw) {
        Iterator<InstanceVariable> it = this.elements();
    	while(it.hasNext()){
    		InstanceVariable var = it.next();
    		pw.printIdent(var.getType().getCname() +" _"+var.getName()+";");
    		pw.println("");
    	}
    }
    
    private ArrayList<InstanceVariable> instanceVariableList;

}
