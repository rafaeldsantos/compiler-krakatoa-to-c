//Rafael Danilo dos Santos RA 408654
package ast;

import java.util.*;


public class MethodList {

	    public MethodList() {
	    	methodList = new ArrayList<MethodDec>();
	    }

	    public void addElement(MethodDec method) {
	    	methodList.add( method );
	    }

	    public Iterator<MethodDec> elements() {
	    	return this.methodList.iterator();
	    }

	    public int getSize() {
	        return methodList.size();
    }
	   
	    public boolean replaceMethod(String methodName, MethodDec new_method){
	    	Iterator<MethodDec> it = methodList.iterator();
	    	while(it.hasNext()){
	    		MethodDec m = it.next();
	    		if(m.getId().equals(methodName)) {
	    			int i = methodList.indexOf(m);
	    			methodList.remove(m);
	    			methodList.add(i, new_method);
	    			return true;
	    		}
	    	}
	    	
	    	return false;
	    }
	    
	    public boolean hasMethod(String methodName){
	    	Iterator<MethodDec> it = methodList.iterator();
	    	while(it.hasNext()){
	    		MethodDec m = it.next();
	    		if(m.getId().equals(methodName)) return true;
	    	}
	    	return false;
	    }
	    
	    public MethodDec getMethod(String methodName){
	    	Iterator<MethodDec> it = methodList.iterator();
	    	while(it.hasNext()){
	    		MethodDec m = it.next();
	    		if(m.getId().equals(methodName)) return m;
	    	}
	    	return null;
	    }
	    
	    public ArrayList<MethodDec> getArray(){
	    	return methodList;
	    }
	    
    private ArrayList<MethodDec> methodList;

}


