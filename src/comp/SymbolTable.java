//Rafael Danilo dos Santos RA 408654
package comp;

import java.util.*;

import ast.*;

public class SymbolTable {

    public SymbolTable() {
        globalTable = new HashMap<String, KraClass>();
        localTable  = new HashMap<String, Variable>();
        instanceTable = new HashMap<String, InstanceVariable>();
    }

    public Object putInGlobal( String key, KraClass value ) {
       return globalTable.put(key, value);
    }

    public KraClass getInGlobal( String key ) {
       return globalTable.get(key);
    }

    public Variable putInLocal( String key, Variable value ) {
       return localTable.put(key, value);
    }

    public Variable putInInstance( String key, InstanceVariable value ) {
        return instanceTable.put(key, value);
     }
    
    public InstanceVariable getInInstance( String key ) {
        return instanceTable.get(key);
     }
    
    public Variable getInLocal( String key ) {
       return localTable.get(key);
    }

    public Object get( String key ) {
        // returns the object corresponding to the key.
        Object result;
        if ( (result = localTable.get(key)) != null ) {
              // found local identifier
            return result;
        }
        else {
              // global identifier, if it is in globalTable
            return globalTable.get(key);
        }
    }

    public void removeLocalIdent() {
           // remove all local identifiers from the table
         localTable.clear();
    }


    private HashMap<String, KraClass> globalTable;
    private HashMap<String, Variable> localTable;
    private HashMap<String, InstanceVariable> instanceTable;
}