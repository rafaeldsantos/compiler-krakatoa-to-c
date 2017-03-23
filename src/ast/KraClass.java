//Rafael Danilo dos Santos RA 408654
package ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import comp.SymbolTable;

/*
 * Krakatoa Class
 */
public class KraClass extends Type {
	
   public KraClass( String name ) {
      super(name);
      this.publicMethodList = new MethodList();
	  this.privateMethodList = new MethodList();
      this.instanceVariableList = new InstanceVariableList(); 
   }
   
   public String getCname() {
      return "_class_"+getName()+"*";
   }
   
   public void addInstanceVariableList (InstanceVariableList instanceVariableList){
	   this.instanceVariableList = instanceVariableList;
   }
   
   public void genKra(PW pw){
	   	pw.printIdent("class "+this.getName());
	   	if(this.getSuperClass() != null) pw.print(" extends "+this.superclass.getName());
	   	pw.print(" {");
		pw.add();
		pw.println("");
		pw.println("");
		Iterator<InstanceVariable> itvar = instanceVariableList.elements();
		while(itvar.hasNext()){
			InstanceVariable var =  itvar.next();
			if(var.getType() != null){
			pw.print(var.getQualifier().toString()+" " + var.getType().getName()+" "+var.getName()+";");
			}else{
				pw.print("");
			}
			pw.println("");
		}
		pw.println("");
		Iterator<MethodDec> it = publicMethodList.elements();
		while(it.hasNext()){
			MethodDec method = it.next();
			method.genKra(pw);
			pw.println("");
		}
		it = privateMethodList.elements();
		while(it.hasNext()){
			MethodDec method = it.next();
			method.genKra(pw);
			pw.println("");
		}
		pw.println("");
		pw.sub();
		pw.printIdent("}");
   }
   
   public void setSuperClass(KraClass superclass) {
	   this.superclass = superclass;
   }

   public KraClass getSuperClass() {
	   return superclass;
   }
   
   public void setMethods(MethodList publicMethodList, MethodList privateMethodList) {
	   this.publicMethodList = publicMethodList;
	   this.privateMethodList = privateMethodList;
   }
   
   public void addPublicMethod(MethodDec m) {
//	   System.out.println("add public");
	   this.publicMethodList.addElement(m);
   }
   
   public void addPrivateMethod(MethodDec m) {
//	   System.out.println("add private "+m.getId());
	   this.privateMethodList.addElement(m);
  }
   
   public Variable getInLocal( String key ) {
       return localTable.get(key);
    }
   
   public Variable putInLocal( String key, Variable value ) {
       return localTable.put(key, value);
    }
   
   public MethodDec methodPublicSearch(String key){
	   Iterator<MethodDec> it = publicMethodList.elements();
	   while(it.hasNext()){
		   MethodDec m = it.next();
		   if(m.getId().equals(key)){
			   return m;
		   }
	   }
	   return null;
   }
   
   public MethodDec methodPrivateSearch(String key){
	   Iterator<MethodDec> it = privateMethodList.elements();
	   while(it.hasNext()){
		   MethodDec m = it.next();
		   //if(m != null){
			   if(m.getId().equals(key)){
				   return m;
			   }
		   
	   	}
	   return null;
   }
   
   
   static public String OwnerMethod2(String method, KraClass classe){
	   String out = classe.getName();
	   MethodDec m = classe.methodPrivateSearch(method);
	   if(m!=null) return out;
	   m = classe.methodPublicSearch(method);
	   if(m!=null) return out;
	   return OwnerMethod(method,classe.superclass);
   }
   
   static public String OwnerMethod(String method, KraClass classe){
	   if(classe.getSuperClass() != null) {
		   String out;
		   out = OwnerMethod(method,classe.superclass);
		   if(out != null){
			   return out;
		   }
	   }
	   if(classe.methodSearch(method)!=null)
		   return classe.getName();
	   return null;
   }
   
   public MethodDec methodSearch(String key){
	   MethodDec method = this.methodPrivateSearch(key);
	   if( method != null) return method;
	   //MethodDec
	   method = this.methodPublicSearch(key);
	   if( method != null) return method;
	   KraClass superclass = this.getSuperClass();
	   if(superclass != null) {
		   method = superclass.methodSearch(key);
		   if(method != null) return method;
	   }
	   return null;
   }
   /*public Variable varSearch(String key){
	   Iterator<InstanceVariable>it =instanceVariableList.elements();
	   return null;
   }*/
   
   public InstanceVariableList getVarList(){
	   return this.instanceVariableList;
   }

   protected MethodList createMethodList(KraClass superClass, MethodList methods){
	   if(superClass.superclass != null){
		   methods = createMethodList(superClass.superclass,methods);
	   }
	   Iterator<MethodDec> it = superClass.publicMethodList.getArray().iterator();
	   while(it.hasNext()){
		   MethodDec m = it.next();
		   if(methods.hasMethod(m.getId())){
			   methods.replaceMethod(m.getId(), m);
		   }else{
			   methods.addElement(m);
		   }
	   }
	   
	   return methods;
   }
   
   public void genC(PW pw) {
		// TODO Auto-generated method stub
	   	pw.println("");
	 	pw.println("typedef struct _St_"+this.getName()+" {");
	 	pw.add();
	 	pw.add();
	 	pw.add();
//	 	pw.println("");
	 	pw.printIdent("Func *vt;");
	   	pw.println("");
	   	KraClass temp = this.superclass;
	   	
	   	while(temp!=null){
	   		Iterator<InstanceVariable> itvar = temp.instanceVariableList.elements();
			while(itvar.hasNext()){
				InstanceVariable var =  itvar.next();
				if(var.getType() != null){
					if( !(var.getType() instanceof KraClass)){
						pw.printIdent(var.getType().getName()+" _"+temp.getName()+"_"+var.getName()+";");
					}else{
						KraClass aux = (KraClass) var.getType();
						pw.print(aux.getCname()+ " _"+temp.getName()+"_"+var.getName()+";");
					}
				}else{
					pw.print("");
				}
				pw.println("");
			}
	   		temp = temp.superclass;
	   	}
	   	
		Iterator<InstanceVariable> itvar = instanceVariableList.elements();
		while(itvar.hasNext()){
			InstanceVariable var =  itvar.next();
			if(var.getType() != null){
				if( !(var.getType() instanceof KraClass)){
					pw.printIdent(var.getType().getName()+" _"+this.getName()+"_"+var.getName()+";");
				}else{
					KraClass aux = (KraClass) var.getType();
					pw.print(aux.getCname()+ " _"+this.getName()+"_"+var.getName()+";");
				}
			}else{
				pw.print("");
			}
			pw.println("");
		}
		
		pw.println("");
		pw.println("} _class_"+this.getName()+";");
		pw.println("");
		
		//methods
		Iterator<MethodDec> it = publicMethodList.elements();
		MethodList methods = new MethodList();
		methods = createMethodList(this,methods);
		
//		ENUM
		pw.println("");
		it = methods.elements();
		if(it.hasNext()){
			pw.print("enum enum_"+this.getName()+"{");
			pw.add();
			pw.println("");
			pw.println("");
			while(it.hasNext()){
				MethodDec method = it.next();
				pw.printIdent("enum_"+this.getName()+"_"+method.getId());
				if(it.hasNext()) pw.print(","); 
				pw.println("");
				
			}
			pw.sub();
			pw.println("};");
		}
		
		pw.println("_class_"+this.getName()+" *new_"+this.getName()+"(void);");
		pw.println("");

		
		it = privateMethodList.elements();
		while(it.hasNext()){
			MethodDec method = it.next();

			pw.add();
			method.genC(pw);
			pw.println("");
		}
		it = publicMethodList.elements();
		while(it.hasNext()){
			MethodDec method = it.next();
			method.genC(pw);
			pw.println("");
		}
		

		
//		MethodList methods = new MethodList();
//		methods = createMethodList(this,methods);

////		ENUM
//		pw.println("");
//		it = methods.elements();
//		pw.print("enum enum_"+this.getName()+"{");
//		pw.add();
//		pw.println("");
//		pw.println("");
//		while(it.hasNext()){
//			MethodDec method = it.next();
//			pw.printIdent("enum_"+this.getName()+"_"+method.getId());
//			if(it.hasNext()) pw.print(","); 
//			pw.println("");
//			
//		}
//		pw.sub();
//		pw.println("};");
		pw.println("");
		pw.print("Func VTclass_"+this.getName()+"[] = { ");
		
//		it = publicMethodList.elements();
		it = methods.elements();
		pw.add();
		pw.println("");
		while(it.hasNext()){
			MethodDec method = it.next();
			pw.printIdent("( void (*)() ) _"+method.getClassName()+"_"+method.getId());
			if(it.hasNext()) {
				pw.print(","); 
				pw.println("");
			}
			
		}
		pw.sub();
		pw.println("};");
		pw.println("");
		pw.println("_class_"+this.getName()+" *new_"+this.getName()+"(){");
		pw.println("");
		pw.add();
		pw.printIdent("_class_"+this.getName()+" *t;");
		pw.println("");
		pw.printIdent("if ( (t = malloc(sizeof(_class_"+this.getName()+"))) != NULL )");
		pw.println("");
		pw.add();
		pw.println("");
		pw.printIdent("t->vt = VTclass_"+this.getName()+"; ");
		pw.sub();
		pw.println("");
		pw.println("");
		pw.printIdent("return t;");
		pw.println("");
		pw.println("");
		pw.sub();
		pw.println("};");
		pw.println("");
//		pw.print("_class_"+this.getName()+" *new_"+this.getName()+"{");
//		pw.print("};");
		pw.println("");
//		pw.sub();
//		pw.printIdent("}");
	}
   
   private String name;
   private KraClass superclass;
   private LocalVariableList localVarList;
   private InstanceVariableList instanceVariableList;
   private HashMap<String, Variable> localTable;
   private MethodList publicMethodList, privateMethodList;
   // métodos públicos get e set para obter e iniciar as variáveis acima,
   // entre outros métodos

}
