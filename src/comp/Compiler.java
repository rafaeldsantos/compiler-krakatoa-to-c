//Rafael Danilo dos Santos RA 408654

package comp;

import ast.*;
import lexer.*;
import java.io.*;
import java.util.*;

public class Compiler {

	// compile must receive an input with an character less than
	// p_input.lenght
	public Program compile(char[] input, PrintWriter outError) {
		countwhile = 0;
		ArrayList<CompilationError> compilationErrorList = new ArrayList<>();
		signalError = new SignalError(outError, compilationErrorList);
		symbolTable = new SymbolTable();
		lexer = new Lexer(input, signalError);
		signalError.setLexer(lexer);

		Program program = null;
		lexer.nextToken();
		program = program(compilationErrorList);
		return program;
	}

	private Program program(ArrayList<CompilationError> compilationErrorList) {
		// Program ::= KraClass { KraClass }
		ArrayList<MetaobjectCall> metaobjectCallList = new ArrayList<>();
		ArrayList<KraClass> kraClassList = new ArrayList<>();
		Program program = new Program(kraClassList, metaobjectCallList, compilationErrorList);
		try {
			while (lexer.token == Symbol.MOCall) {
				metaobjectCallList.add(metaobjectCall());
			}
			kraClassList.add(classDec());
			while (lexer.token == Symbol.CLASS){
				KraClass temp = classDec();
				kraClassList.add(temp);
			}
			//System.out.println(lexer.getStringValue());
			if (lexer.token != Symbol.EOF) {
				signalError.show("End of file expected");
			}
			//KraClass s = symbolTable.getInGlobal("Program");
			if(symbolTable.getInGlobal("Program")==null){
				signalError.show("Source code without a class 'Program'");
			}
		} catch (RuntimeException e) {
			//System.out.println("erro: " + e);
			// if there was an exception, there is a compilation signalError
		}
		//System.out.println(kraClassList.size());
		return program;
	}

	/**
	 * parses a metaobject call as <code>{@literal @}ce(...)</code> in <br>
	 * <code>
	 * &#64;ce(5, "'class' expected") <br>
	 * clas Program <br>
	 *     public void run() { } <br>
	 * end <br>
	 * </code>
	 * 
	 * 
	 */
	@SuppressWarnings("incomplete-switch")
	private MetaobjectCall metaobjectCall() {
		String name = lexer.getMetaobjectName();
		lexer.nextToken();
		ArrayList<Object> metaobjectParamList = new ArrayList<>();
		if (lexer.token == Symbol.LEFTPAR) {
			// metaobject call with parameters
			lexer.nextToken();
			while (lexer.token == Symbol.LITERALINT || lexer.token == Symbol.LITERALSTRING
					|| lexer.token == Symbol.IDENT) {
				switch (lexer.token) {
				case LITERALINT:
					metaobjectParamList.add(lexer.getNumberValue());
					break;
				case LITERALSTRING:
					metaobjectParamList.add(lexer.getLiteralStringValue());
					break;
				case IDENT:
					metaobjectParamList.add(lexer.getStringValue());
				}
				lexer.nextToken();
				if (lexer.token == Symbol.COMMA)
					lexer.nextToken();
				else
					break;
			}
			if (lexer.token != Symbol.RIGHTPAR)
				signalError.show("')' expected after metaobject call with parameters");
			else
				lexer.nextToken();
		}
		if (name.equals("nce")) {
			if (metaobjectParamList.size() != 0)
				signalError.show("Metaobject 'nce' does not take parameters");
		} else if (name.equals("ce")) {
			if (metaobjectParamList.size() != 3 && metaobjectParamList.size() != 4)
				signalError.show("Metaobject 'ce' take three or four parameters");
			if (!(metaobjectParamList.get(0) instanceof Integer))
				signalError.show("The first parameter of metaobject 'ce' should be an integer number");
			if (!(metaobjectParamList.get(1) instanceof String) || !(metaobjectParamList.get(2) instanceof String))
				signalError.show("The second and third parameters of metaobject 'ce' should be literal strings");
			if (metaobjectParamList.size() >= 4 && !(metaobjectParamList.get(3) instanceof String))
				signalError.show("The fourth parameter of metaobject 'ce' should be a literal string");

		}

		return new MetaobjectCall(name, metaobjectParamList);
	}

	private KraClass classDec() {
		// Note que os métodos desta classe não correspondem exatamente às
		// regras
		// da gramática. Este método classDec, por exemplo, implementa
		// a produção KraClass (veja abaixo) e partes de outras produções.

		/*
		 * KraClass ::= ``class'' Id [ ``extends'' Id ] "{" MemberList "}"
		 * MemberList ::= { Qualifier Member } Member ::= InstVarDec | MethodDec
		 * InstVarDec ::= Type IdList ";" MethodDec ::= Qualifier Type Id "("[
		 * FormalParamDec ] ")" "{" StatementList "}" Qualifier ::= [ "static" ]
		 * ( "private" | "public" )
		 */
		
		//MethodList publicmethodlist = new MethodList();
		//MethodList privatemethodlist = new MethodList();
		if (lexer.token != Symbol.CLASS)
			signalError.show("'class' expected");
		lexer.nextToken();
		if (lexer.token != Symbol.IDENT)
			signalError.show(SignalError.ident_expected);
		String className = lexer.getStringValue();
		KraClass newClass = new KraClass(className);
		currentClass = newClass;
		// kraClassList.add(newClass);
		//System.out.println(className);
		symbolTable.putInGlobal(className, newClass);
		// kraClassList.add((KraClass) symbolTable.getInGlobal(className));
		lexer.nextToken();
		if (lexer.token == Symbol.EXTENDS) {
			lexer.nextToken();
			if (lexer.token != Symbol.IDENT)
				signalError.show(SignalError.ident_expected);
			String superclassName = lexer.getStringValue();
			KraClass x = symbolTable.getInGlobal(superclassName);
			if(x == null){
				signalError.show("Superclass "+superclassName+"wasnt declared");
			} 
			if(superclassName.equals(currentClass.getName())){
				signalError.show("Class"+currentClass.getName()+" is inheriting from itself ");
			}
			symbolTable.getInGlobal(className).setSuperClass((KraClass) symbolTable.get(superclassName));
			lexer.nextToken();
		} else {
			symbolTable.getInGlobal(className).setSuperClass(null);
		}
		if (lexer.token != Symbol.LEFTCURBRACKET)
			signalError.show("{ expected", true);
		lexer.nextToken();
		//System.out.println(lexer.getStringValue());
		//if(lexer.getStringValue().equals("static")) System.out.println("x");
		while (lexer.getStringValue().equals("static") || lexer.token == Symbol.PRIVATE || lexer.token == Symbol.PUBLIC) {
			
			boolean isstatic = false;
			if(lexer.getStringValue().equals("static")){
				isstatic = true;
				lexer.nextToken();
			}
			
			Symbol qualifier;
			switch (lexer.token) {
			case PRIVATE:
				lexer.nextToken();
				qualifier = Symbol.PRIVATE;
				break;
			case PUBLIC:
				lexer.nextToken();
				qualifier = Symbol.PUBLIC;
				break;
			default:
				signalError.show("private, or public expected");
				qualifier = Symbol.PUBLIC;
			}

			Type t = type();
		
			if (lexer.token != Symbol.IDENT)
				signalError.show("Identifier expected");
			String name = lexer.getStringValue();
			lexer.nextToken();
			if (lexer.token == Symbol.LEFTPAR) {
				MethodDec method = methodDec(t, name, qualifier);
				//System.out.println(method.getId());
				if (qualifier == Symbol.PRIVATE) {
					currentClass.addPrivateMethod(method);
				} else {
					currentClass.addPublicMethod(method);
				}
			} else if (qualifier != Symbol.PRIVATE)
				signalError.show("Attempt to declare a public instance variable");
			else
				instanceVarDec(t, name, newClass.getVarList(),qualifier,isstatic);
		}
		//System.out.println(newClass.getName());
		if(newClass.getName().equals("Program")){
			MethodDec m = newClass.methodSearch("run");
			if(m == null){
				signalError.show("Method 'run' was not found in class 'Program'");
			}
			if(m.getQualifier() == Symbol.PRIVATE){
				signalError.show("Method 'run' of class 'Program' cannot be private");
			}
			
			if(!m.hasParam()){
				signalError.show("Method 'run' of class 'Program' cannot take parameters");
			}
		}
		//System.out.println(lexer.getStringValue());
		if (lexer.token != Symbol.RIGHTCURBRACKET)
			signalError.show("public/private or \"}\" expected");
		lexer.nextToken();
		
		return newClass;
	}

	private void instanceVarDec(Type type, String name, InstanceVariableList out, Symbol qualifier, boolean isstatic) {
		// InstVarDec ::= [ "static" ] "private" Type IdList ";"
		// InstanceVariableList out = new InstanceVariableList();
		
		if (symbolTable.getInLocal(name)!= null ){
			signalError.show("Variable '"+name+"' is being redeclared");
		}
		InstanceVariable v = new InstanceVariable(name, type, qualifier);
		out.addElement(v);
		if(!isstatic){
			symbolTable.putInLocal(name,v);
		} //else{
			symbolTable.putInInstance(name, v);
		//}
		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();
			if (lexer.token != Symbol.IDENT)
				signalError.show("Identifier expected");
			String variableName = lexer.getStringValue();
			if (symbolTable.getInLocal(variableName)!= null){
				signalError.show("Variable '"+variableName+"' is being redeclared");
			}
			v = new InstanceVariable(variableName, type, qualifier);
			symbolTable.putInLocal(variableName,v);
			out.addElement(new InstanceVariable(variableName, type, qualifier));
			lexer.nextToken();
		}
		if (lexer.token != Symbol.SEMICOLON)
			signalError.show(SignalError.semicolon_expected);
		lexer.nextToken();
		//currentClass.addInstanceVariableList(out);
		// return out;
	}

	private MethodDec methodDec(Type type, String name, Symbol qualifier) {
		/*
		 * MethodDec ::= Qualifier Return Id "("[ FormalParamDec ] ")" "{"
		 * StatementList "}"
		 */
		//System.out.println(type.getName()+" "+name);
		Variable v = symbolTable.getInLocal(name);
		if(v!= null){
			signalError.show("test");
		}
		
		ParamList paramlist = null;
		KraClass c = currentClass.getSuperClass();
		//if(c!=null){
		
		
		MethodDec methodSuper =null;
		if(c != null){
			methodSuper = c.methodPublicSearch(name);
			if(methodSuper == null){
				methodSuper = c.methodPrivateSearch(name);
			}
			if(methodSuper != null){
				if(methodSuper.getType() != type){
					signalError.show("Method '"+name+"' of subclass '"+currentClass.getName()+"' has a signature different from method inherited from superclass '"+c.getName()+"'");
				}
			}
		}	
		
		MethodDec m = currentClass.methodPublicSearch(name);
		if(m == null){
			m = currentClass.methodPrivateSearch(name);
		}
		if(m != null){
			signalError.show("Method ' "+m.getId()+"' is being redeclared");
		}
		currentMethod = new MethodDec(type, name, qualifier,currentClass);
		//System.out.println(currentMethod.getId());
		lexer.nextToken();
		if (lexer.token != Symbol.RIGHTPAR) {
			paramlist = formalParamDec();
			//System.out.println("x")
			if(methodSuper!=null){
				Iterator<Variable> it =  methodSuper.gerParamList().elements();
				Iterator<Variable> it2 =  currentMethod.gerParamList().elements();
				while(it.hasNext()){
					if(it.next().getType() != it2.next().getType()){
						signalError.show("Method '"+currentMethod.getId()+"' is being redefined in subclass '"+currentClass.getName()+"' with a signature different from the method of superclass XY");
						break;
					}
				}
			}

		}
		
		if (lexer.token != Symbol.RIGHTPAR)
			signalError.show(") expected");

		lexer.nextToken();
		if (lexer.token != Symbol.LEFTCURBRACKET)
			signalError.show("{ expected");
		
		lexer.nextToken();
		
		StatementList slist = statementList();
		Iterator<Statement> it = slist.elements();
		boolean hasReturn = false;
		while (it.hasNext()) {
			Statement s = it.next();
			if (s instanceof ReturnStatement) {
				hasReturn = true;
			} else if (s instanceof IfStatement) {
				hasReturn = ((IfStatement) s).hasReturn();

			}
		}
		currentMethod.addStatementList(slist);
		if ((!hasReturn) && (type != Type.voidType))
			signalError.show("Return statement expected");

		if (lexer.token != Symbol.RIGHTCURBRACKET)
			signalError.show("} expected");
		
		lexer.nextToken();
		symbolTable.removeLocalIdent();
		return currentMethod;

	}

	private Expr localDec() {
		// LocalDec ::= Type IdList ";"
		LocalVariableExpr out =  null;
		LocalVariableList varlist = new LocalVariableList();
		Type type = type();
		
		if ( lexer.token != Symbol.IDENT ) {
			//System.out.println(lexer.getCurrentLine()+" "+lexer.getStringValue());
			signalError.show("Identifier expected XXX");
		}
		
		if( symbolTable.getInLocal( lexer.getStringValue() ) != null ){
			signalError.show("Variable '"+lexer.getStringValue()+"' has been declared");
		}
		
			Variable v = new Variable(lexer.getStringValue(), type);
			varlist.addElement(v);
			symbolTable.putInLocal(lexer.getStringValue(), v);
		

		lexer.nextToken();
		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();
			if ( lexer.token != Symbol.IDENT )
				signalError.show("Identifier expected");
			v = new Variable(lexer.getStringValue(), type);
			varlist.addElement(v);
			if( symbolTable.getInLocal( lexer.getStringValue() ) != null ){
				signalError.show("Variable '"+lexer.getStringValue()+"' has been declared 2");
			}
			symbolTable.putInLocal(lexer.getStringValue(), v);
			lexer.nextToken();
		}
		if(lexer.token != Symbol.SEMICOLON){
			signalError.show("Missing ';'");
		}
		//lexer.nextToken();
		out = new LocalVariableExpr(type, varlist);
		return out;
	}

	private ParamList formalParamDec() {
		// FormalParamDec ::= ParamDec { "," ParamDec }
		ParamList out = new ParamList();
		out.addElement(paramDec());
		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();
			out.addElement(paramDec());
		}
		return out;
	}

	private Parameter paramDec() {
		// ParamDec ::= Type Id
		Type temp_type = type();
		if (lexer.token != Symbol.IDENT)
			signalError.show("Identifier expected");
		Parameter out = new Parameter(lexer.getStringValue(), temp_type);
		symbolTable.putInLocal(lexer.getStringValue(), out);
		
		currentMethod.addParameter(out);
		
		lexer.nextToken();
		return out;
	}

	private Type type() {
		// Type ::= BasicType | Id
		Type result = null;

		switch (lexer.token) {
		case VOID:
			result = Type.voidType;
			break;
		case INT:
			result = Type.intType;
			break;
		case BOOLEAN:
			result = Type.booleanType;
			break;
		case STRING:
			result = Type.stringType;
			break;
		case IDENT:
			KraClass x = (KraClass) symbolTable.get(lexer.getStringValue());
			if (x != null) {
				result = x;
			}

			break;
		default:
			signalError.show("Type expected");
			result = Type.undefinedType;
		}
		lexer.nextToken();
		return result;
	}

	private Statement compositeStatement() {

		lexer.nextToken();
		StatementList list = statementList();
		if (lexer.token != Symbol.RIGHTCURBRACKET)
			signalError.show("} expected");
		else
			lexer.nextToken();

		return new CompositeStatement(list);
	}

	private StatementList statementList() {
		// CompStatement ::= "{" { Statement } "}"
		StatementList out = new StatementList();
		Symbol tk;
		// statements always begin with an identifier, if, read, write, ...
		while ((tk = lexer.token) != Symbol.RIGHTCURBRACKET && tk != Symbol.ELSE)
			out.addElement(statement());
		return out;
	}

	private Statement statement() {
		/*
		 * Statement ::= Assignment ``;'' | IfStat |WhileStat | MessageSend
		 * ``;'' | ReturnStat ``;'' | ReadStat ``;'' | WriteStat ``;'' |
		 * ``break'' ``;'' | ``;'' | CompStatement | LocalDec
		 */
		Statement out = null;
		switch (lexer.token) {
		case THIS:
		case IDENT:
		case SUPER:
		case INT:
		case BOOLEAN:
		case STRING:
			out = new AssignExprLocalDecStatement(assignExprLocalDec());
			break;
		case RETURN:
			out = returnStatement();
			break;
		case READ:
			out = readStatement();
			break;
		case WRITE:
			out = writeStatement();
			break;
		case WRITELN:
			out = writelnStatement();
			break;
		case IF:
			out = ifStatement();
			break;
		case BREAK:
			out = breakStatement();
			break;
		case WHILE:
			countwhile++;
			out = whileStatement();
			countwhile--;
			break;
		case SEMICOLON:
			out = nullStatement();
			break;
		case LEFTCURBRACKET:
			out = compositeStatement();
			break;
		default:
			signalError.show("Statement expected");
		}
		return out;
	}

	/*
	 * retorne true se 'name' é uma classe declarada anteriormente. É necessário
	 * fazer uma busca na tabela de símbolos para isto.
	 */
	private boolean isType(String name) {
		return this.symbolTable.getInGlobal(name) != null;
	}

	/*
	 * AssignExprLocalDec ::= Expression [ ``$=$'' Expression ] | LocalDec
	 */
	private Expr assignExprLocalDec() {
		
		Expr out = null;
		if (lexer.token == Symbol.INT || lexer.token == Symbol.BOOLEAN || lexer.token == Symbol.STRING ||
				// token é uma classe declarada textualmente antes desta
				// instrução
		(lexer.token == Symbol.IDENT && isType(lexer.getStringValue())&& !(lexer.getCurrentLine().contains("=")) && !lexer.getCurrentLine().contains(lexer.getStringValue()+".")  )) {
			/*&& !(lexer.getCurrentLine().contains("="))
			 * uma declaração de variável. 'lexer.token' é o tipo da variável
			 * 
			 * AssignExprLocalDec ::= Expression [ ``$=$'' Expression ] |
			 * LocalDec LocalDec ::= Type IdList ``;''
			 */
			//if(lexer.getCurrentLine().contains("B b;"))System.out.println("sim");
			out = localDec();

		} else {
			/*
			 * AssignExprLocalDec ::= Expression [ ``$=$'' Expression ]
			 */
			// tem que verificar se esse ident é uma variavel ou um metodo =(
			//System.out.println(lexer.getCurrentLine()+"YYY");
			if (lexer.token == Symbol.IDENT) {
				symbolTable.getInGlobal(lexer.getStringValue());
				if (symbolTable.getInLocal(lexer.getStringValue()) == null && !(lexer.getCurrentLine().contains(lexer.getStringValue()+".")) ) {
					// symbolTable.getInGlobal


					signalError.show("Variable " + lexer.getStringValue() + " not declared");
				}
			}
			/*
			if(lexer.token != Symbol.PLUS && lexer.token != Symbol.MINUS && lexer.token != Symbol.LITERALINT && lexer.token != Symbol.FALSE && lexer.token != Symbol.TRUE && lexer.token != Symbol.LITERALSTRING && lexer.token != Symbol.LEFTPAR && lexer.token != Symbol.NULL && lexer.token != Symbol.NOT && lexer.token != Symbol.NEW && lexer.token != Symbol.ASSIGN && lexer.token != Symbol.THIS){
				lexer.nextToken();
			}*/
			/*
			if (lexer.token == Symbol.DOT ){
				System.out.println("xxxxxxxxx");
				lexer.nextToken();
				if(lexer.token == Symbol.IDENT){
					lexer.nextToken();
				}
				exprList();
			}*/
			Expr right = expr();
			//System.out.println(right.getType() +" "+lexer.getCurrentLine());
			if (lexer.token == Symbol.ASSIGN) {
				lexer.nextToken();
				Expr left = expr();
				out = new AssignExpr(right, left);

				
				if(left.getType() == Type.voidType){
					//("x");
					signalError.show("Expression expected in the right-hand side of assignment");
				}
				//KraClass temps = (KraClass) left.getType();
				//if(right!=null) System.out.println("11111"+lexer.getCurrentLine()+"\n ccc "+right.getType());
				if(left.getType() != right.getType()){
					
					//System.out.println(left.getType().+" "+superClass.getName());
					if(left.getType() instanceof KraClass && right.getType() instanceof KraClass){
						KraClass temp = (KraClass) left.getType();
						KraClass superClass = (KraClass) right.getType();
						//System.out.println(temp.getSuperClass().getName()+" "+superClass.getName());
						while(temp.getSuperClass()!= null){
							if(temp.getSuperClass() == superClass) break;
							temp = temp.getSuperClass();
						}
						if(temp.getSuperClass() != superClass) {
							//System.out.println(temp.getName()+" "+superClass.getName());
							signalError.show("Atribuicao com tipos errados");
							
						}
					} else if( !(right.getType() instanceof KraClass && left instanceof NullExpr) ){
						
						signalError.show("Atribuicao com tipos errados");
					}
				}
				if (lexer.token != Symbol.SEMICOLON)
					signalError.show("';' expected", true);
				else
					lexer.nextToken();
			} else {
				//if(out instanceof MethodDec)
				out = right;
				//if(out==null) System.out.println("sim");
				if(out instanceof MessageSendToVariable && out.getType() != Type.voidType){
					//System.out.println("x");
					signalError.show("Message send 'a.m()' returns a value that is not used **");
				}
			}
		}
		return out;
	}

	private ExprList realParameters() {
		ExprList anExprList = null;

		if (lexer.token != Symbol.LEFTPAR)
			signalError.show("( expected");
		lexer.nextToken();
		if (startExpr(lexer.token))
			anExprList = exprList();
		if (lexer.token != Symbol.RIGHTPAR)
			signalError.show(") expected");
		lexer.nextToken();
		return anExprList;
	}

	private Statement whileStatement() {
		lexer.nextToken();
		if (lexer.token != Symbol.LEFTPAR)
			signalError.show("( expected");
		lexer.nextToken();
		Expr temp_expr = expr();
		if(temp_expr.getType() != Type.booleanType){
			signalError.show("non-boolean expression in  'while'");
		}
		if (lexer.token != Symbol.RIGHTPAR)
			signalError.show(") expected");
		lexer.nextToken();
		Statement temp_statement = statement();
		return new WhileStatement(temp_expr, temp_statement);

	}

	private Statement ifStatement() {

		lexer.nextToken();
		if (lexer.token != Symbol.LEFTPAR)
			signalError.show("( expected");
		lexer.nextToken();
		Expr temp_expr = expr();
		if (lexer.token != Symbol.RIGHTPAR)
			signalError.show(") expected");
		lexer.nextToken();
		Statement if_statement = statement();
		if (lexer.token == Symbol.ELSE) {
			lexer.nextToken();
			return new IfStatement(temp_expr, if_statement, statement());
		}
		return new IfStatement(temp_expr, if_statement, null);
	}

	private Statement returnStatement() {

		lexer.nextToken();
		Expr temp = expr();
		if(currentMethod.getType() == Type.voidType){
			signalError.show("Void method dont have return statement");
		}
		//temp.getType();
		if(temp instanceof ObjectExpr){
				if(!temp.getType().getName().equals(currentMethod.getType().getName() )){
					KraClass superClasse = (KraClass) temp.getType();
					if(!superClasse.getName().equals(currentMethod.getType().getName() )){
						signalError.show("Type error: type of the expression returned is not subclass of the method return type");
					}
				}
		}
		if (lexer.token != Symbol.SEMICOLON)
			signalError.show(SignalError.semicolon_expected);
		lexer.nextToken();
		return new ReturnStatement(temp);
	}

	private Statement readStatement() {
		ArrayList<String> idList = new ArrayList<>();
		ArrayList<Variable> varList = new ArrayList<>();
		lexer.nextToken();
		if (lexer.token != Symbol.LEFTPAR)
			signalError.show("( expected");
		lexer.nextToken();
		while (true) {
			if (lexer.token == Symbol.THIS) {
				lexer.nextToken();
				if (lexer.token != Symbol.DOT)
					signalError.show(". expected");
				lexer.nextToken();
			}
			if (lexer.token != Symbol.IDENT)
				signalError.show(SignalError.ident_expected);
			// verificar se a variavel existe
			
			String name = lexer.getStringValue();
			
			KraClass k = symbolTable.getInGlobal(name);
			if(k != null){
				lexer.nextToken();
				if (lexer.token != Symbol.DOT)
					signalError.show("Dot expected");
				lexer.nextToken();
				if (lexer.token != Symbol.IDENT)
					signalError.show(SignalError.ident_expected);
				
				
			}
			name = lexer.getStringValue();
			
			Variable v = symbolTable.getInLocal(name);
			
			if( v== null)v = symbolTable.getInInstance(name);
			if(v == null){
				System.out.println(lexer.getCurrentLine());
				signalError.show("Variable "+name+" wasnt declared");
			}else {
				Variable varIn = symbolTable.getInInstance(name);
				if(v.getType() != Type.intType && v.getType() != Type.stringType && varIn == null){
					signalError.show("'int' or 'String' expression expected ");
				}
				
			}
			varList.add(v);
			idList.add(name);
			lexer.nextToken();
			if (lexer.token == Symbol.COMMA)
				lexer.nextToken();
			else
				break;
		}

		if (lexer.token != Symbol.RIGHTPAR)
			signalError.show(") expected");
		lexer.nextToken();
		if (lexer.token != Symbol.SEMICOLON)
			signalError.show(SignalError.semicolon_expected);
		lexer.nextToken();
		return new ReadStatement(idList, varList);
	}

	private Statement writeStatement() {
		
		lexer.nextToken();
		if (lexer.token != Symbol.LEFTPAR)
			signalError.show("( expected");
		lexer.nextToken();
		ExprList list=null;
		//try{
			list = exprList();
		//}catch (RuntimeException e) {
			//System.out.println(currentClass.getName());
			//System.out.println(e);
	//	}
			//System.out.println(lexer.getCurrentLine());
		if(list != null){
			Iterator<Expr> it = list.elements();
			while(it.hasNext()){
				
				Expr temp = it.next();
				//System.out.println(temp.getType());
				if(temp.getType() == Type.booleanType){
					signalError.show("Command 'write' does not accept 'boolean' expressions");
				}
				//&& lexer.getCurrentLine(lexer.getStringValue()
				//System.out.println(lexer.getStringValue());
				if(temp.getType() != Type.intType && temp.getType() != Type.stringType){
					
					signalError.show("Command 'write' does not accept objects");
					//System.out.println(temp.getType());
				}
			}
		}
		
		if (lexer.token != Symbol.RIGHTPAR)
			signalError.show(") expected");
		lexer.nextToken();
		if (lexer.token != Symbol.SEMICOLON)
			signalError.show(SignalError.semicolon_expected);
		lexer.nextToken();
		return new WriteStatement(list);
	}

	private Statement writelnStatement() {

		lexer.nextToken();
		if (lexer.token != Symbol.LEFTPAR)
			signalError.show("( expected");
		lexer.nextToken();
		ExprList list = exprList();
		if(list != null){
			Iterator<Expr> it = list.elements();
			while(it.hasNext()){
				Expr temp = it.next();
				if(temp.getType() == Type.booleanType){
					signalError.show("Command 'write' does not accept 'boolean' expressions");
				}
				
			}
		}
		if (lexer.token != Symbol.RIGHTPAR)
			signalError.show(") expected");
		lexer.nextToken();
		if (lexer.token != Symbol.SEMICOLON)
			signalError.show(SignalError.semicolon_expected);
		lexer.nextToken();
		return new WritelnStatement(list);
	}

	private Statement breakStatement() {
		lexer.nextToken();
		if(countwhile<=0){
			signalError.show("'break' statement found outside a 'while' statement");
		}
		if (lexer.token != Symbol.SEMICOLON)
			signalError.show(SignalError.semicolon_expected);
		lexer.nextToken();
		//if(!iswhile){
		//	signalError.show("'break' statement found outside a 'while' statement");
		//}
		return new BreakStatement();
	}

	private Statement nullStatement() {
		lexer.nextToken();
		return new NullStatement();
	}

	private ExprList exprList() {
		// ExpressionList ::= Expression { "," Expression }

		ExprList anExprList = new ExprList();
		anExprList.addElement(expr());
		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();
			anExprList.addElement(expr());
		}
		return anExprList;
	}

	private Expr expr() {

		Expr left = simpleExpr();
		Symbol op = lexer.token;
		if (op == Symbol.EQ || op == Symbol.NEQ || op == Symbol.LE || op == Symbol.LT || op == Symbol.GE
				|| op == Symbol.GT) {
			lexer.nextToken();
			Expr right = simpleExpr();
			left = new CompositeExpr(left, op, right);
		}
		return left;
	}

	private Expr simpleExpr() {
		Symbol op;

		Expr left = term();
		while ((op = lexer.token) == Symbol.MINUS || op == Symbol.PLUS || op == Symbol.OR) {
			lexer.nextToken();
			Expr right = term();
			if( op == Symbol.PLUS  || op == Symbol.MINUS ){
				if(left.getType() == Type.booleanType || right.getType() == Type.booleanType){
					signalError.show("Boolens dont have "+ op.toString() +" operation");
				}
				if( (left.getType() == Type.intType || right.getType() == Type.intType) && (left.getType() != Type.intType || right.getType() != Type.intType) ){
					signalError.show("operator '+' of 'int' expects an 'int' value");
				}
				}else{
				
			}
			left = new CompositeExpr(left, op, right);
		}
		return left;
	}

	private Expr term() {
		Symbol op;

		Expr left = signalFactor();
		while ((op = lexer.token) == Symbol.DIV || op == Symbol.MULT || op == Symbol.AND) {
			lexer.nextToken();
			Expr right = signalFactor();
			left = new CompositeExpr(left, op, right);
		}
		return left;
	}

	private Expr signalFactor() {
		Symbol op;
		if ((op = lexer.token) == Symbol.PLUS || op == Symbol.MINUS) {
			lexer.nextToken();
			return new SignalExpr(op, factor());
		} else
			return factor();
	}

	/*
	 * Factor ::= BasicValue | "(" Expression ")" | "!" Factor | "null" |
	 * ObjectCreation | PrimaryExpr
	 * 
	 * BasicValue ::= IntValue | BooleanValue | StringValue BooleanValue ::=
	 * "true" | "false" ObjectCreation ::= "new" Id "(" ")" PrimaryExpr ::=
	 * "super" "." Id "(" [ ExpressionList ] ")" | Id | Id "." Id | Id "." Id
	 * "(" [ ExpressionList ] ")" | Id "." Id "." Id "(" [ ExpressionList ] ")"
	 * | "this" | "this" "." Id | "this" "." Id "(" [ ExpressionList ] ")" |
	 * "this" "." Id "." Id "(" [ ExpressionList ] ")"
	 */
	private Expr factor() {

		Expr e;
		ExprList exprList;
		String messageName, ident;

		switch (lexer.token) {
		// IntValue
		case LITERALINT:
			return literalInt();
		// BooleanValue
		case FALSE:
			lexer.nextToken();
			return LiteralBoolean.False;
		// BooleanValue
		case TRUE:
			lexer.nextToken();
			return LiteralBoolean.True;
		// StringValue
		case LITERALSTRING:
			String literalString = lexer.getLiteralStringValue();
			lexer.nextToken();
			return new LiteralString(literalString);
		// "(" Expression ")" |
		case LEFTPAR:
			lexer.nextToken();
			e = expr();
			if (lexer.token != Symbol.RIGHTPAR)
				signalError.show(") expected");
			lexer.nextToken();
			return new ParenthesisExpr(e);

		// "null"
		case NULL:
			lexer.nextToken();
			return new NullExpr();
		// "!" Factor
		case NOT:
			lexer.nextToken();
			e = expr();
			if(e.getType() != Type.booleanType){
				signalError.show("Operator '!' does not accepts '"+e.getType()+"' values ");
			}
			return new UnaryExpr(e, Symbol.NOT);
		// ObjectCreation ::= "new" Id "(" ")"
		case NEW:
			lexer.nextToken();
			if (lexer.token != Symbol.IDENT)
				signalError.show("Identifier expected");

			String className = lexer.getStringValue();
			//System.out.print(className);
			KraClass aClass = symbolTable.getInGlobal(className);
			if(aClass == null){
				signalError.show("'"+className+"' isnt declared");
			}
			//System.out.println(aClass.getName());
			/*
			 * // encontre a classe className in symbol table KraClass aClass =
			 * symbolTable.getInGlobal(className); if ( aClass == null ) ...
			 */
			ObjectExpr out = new ObjectExpr(aClass);
			lexer.nextToken();
			if (lexer.token != Symbol.LEFTPAR)
				signalError.show("( expected");
			lexer.nextToken();
			if (lexer.token != Symbol.RIGHTPAR)
				signalError.show(") expected");
			lexer.nextToken();
			/*
			 * return an object representing the creation of an object
			 */
			return out;//new ObjectExpr(className,aClass);
			//return null;
		/*
		 * PrimaryExpr ::= "super" "." Id "(" [ ExpressionList ] ")" | Id | Id
		 * "." Id | Id "." Id "(" [ ExpressionList ] ")" | Id "." Id "." Id "("
		 * [ ExpressionList ] ")" | "this" | "this" "." Id | "this" "." Id "(" [
		 * ExpressionList ] ")" | "this" "." Id "." Id "(" [ ExpressionList ]
		 * ")"
		 */
		case SUPER:
			if(currentClass.getName().equals("Program")){
				signalError.show("'super' used in class 'Program' that does not have a superclass");
			}
			// "super" "." Id "(" [ ExpressionList ] ")"
			lexer.nextToken();
			if (lexer.token != Symbol.DOT) {
				signalError.show("'.' expected");
			} else
				lexer.nextToken();
			if (lexer.token != Symbol.IDENT)
				signalError.show("Identifier expected");
			messageName = lexer.getStringValue();
			//System.out.println (messageName);
			KraClass superClass = currentClass.getSuperClass();
			//System.out.println(messageName);
			//if(superClass != null){
			//}
			MethodDec m = superClass.methodSearch(messageName);
			//System.out.println(m.getQualifier()+" "+messageName);
			//if(superClass != null){
				//m = superClass.methodSearch(messageName);
				if(m == null || m.getQualifier() == Symbol.PRIVATE){
					signalError.show("Method '"+messageName+"' was not found in the public interface of '"+currentClass.getName()+"' or its superclasses");
					//System.out.println("oi");
				}
				/*if(m.getQualifier() != Symbol.PRIVATE){
					signalError.show("Method '"+messageName+"' was not found in the public interface of '"+currentClass.getName()+"' or its superclasses");
					//System.out.println("oi");
				}*/
			//}
			//System.out.println(messageName);
			//KraClass kclass = symbolTable.getInGlobal(v.getType().getName());
			//MethodDec m = kclass.methodSearch(ident);
			//if(m == null) {
			//	signalError.show("My MOM");
			//}
			lexer.nextToken();
			exprList = realParameters();
			MessageSendToSuper msgsend = new MessageSendToSuper( m.getType(), exprList,messageName,superClass);
			return msgsend;
			//break;
		case IDENT:
			/*
			 * PrimaryExpr ::= Id | Id "." Id | Id "." Id "(" [ ExpressionList ]
			 * ")" | Id "." Id "." Id "(" [ ExpressionList ] ")" |
			 */

			String firstId = lexer.getStringValue();
			lexer.nextToken();
			if (lexer.token != Symbol.DOT) {
				// Id
				// retorne um objeto da ASA que representa um identificador
				Variable v = symbolTable.getInLocal(firstId);
				if(v instanceof InstanceVariable){
					signalError.show("Identifier '"+firstId+"' was not found");
				}
				return new VariableExpr(v);
			} else { // Id "."
				//if(currentClass.methodPublicSearch(firstId)== null && currentClass.methodPrivateSearch(firstId)== null){
				//	signalError.show("Message send to a non-object receiver");
				//}
				//System.out.println(firstId);
				Variable v = symbolTable.getInLocal(firstId);
				KraClass k = symbolTable.getInGlobal(firstId);
				if(k == null){
					if(v == null){
						signalError.show(firstId + " wasnt declared");
					}else{
						if( !(v.getType() instanceof KraClass) ){
							signalError.show("Message send to a non-object receiver, ("+v.getType()+")");
						}
					}
				}
				lexer.nextToken(); // coma o "."
				if (lexer.token != Symbol.IDENT) {
					signalError.show("Identifier expected");
				} else {
					// Id "." Id
					lexer.nextToken();
					ident = lexer.getStringValue();
					
					if (lexer.token == Symbol.DOT) {
						// Id "." Id "." Id "(" [ ExpressionList ] ")"
						/*
						 * se o compilador permite variáveis estáticas, é
						 * possível ter esta opção, como
						 * Clock.currentDay.setDay(12); Contudo, se variáveis
						 * estáticas não estiver nas especificações, sinalize um
						 * erro neste ponto.
						 */
						lexer.nextToken();
						if (lexer.token != Symbol.IDENT)
							signalError.show("Identifier expected");
						KraClass kclass = symbolTable.getInGlobal(firstId);
						if(kclass==null)System.out.println("simmmmm");
						messageName = lexer.getStringValue();
						MethodDec method = kclass.methodSearch(messageName);
											
						lexer.nextToken();
						exprList = this.realParameters();
						return new MessageSendToStaticVariable(method.getType(),exprList,kclass,messageName, currentClass);
					} else if (lexer.token == Symbol.LEFTPAR) {
						// Id "." Id "(" [ ExpressionList ] ")"
						//if(v==null)System.out.println(ident+" xdddddddddddddd");
						KraClass kclass;
						if(v!=null){
							kclass = symbolTable.getInGlobal(v.getType().getName());
						}else{
							kclass = symbolTable.getInGlobal(firstId);
						}
						
						MethodDec method = kclass.methodSearch(ident);
						if(method == null && !currentMethod.getId().equals(ident)) {
							signalError.show("Method '"+ident+"' was not found in class '"+currentClass.getName()+"' or its superclasses");
						}
						//if(method == null) System.out.println("errrrrrr");
						//if(method.getQualifier().equals(Symbol.PRIVATE) ) 
						//System.out.println(method.getId());
						if(method.getQualifier() == Symbol.PRIVATE){
							signalError.show("Method '"+method.getId()+"' was not found in the public interface of '"+currentClass.getName()+"' or its superclasses");
						}
						exprList = this.realParameters();
						MessageSendToVariable msgsend1 = new MessageSendToVariable( method.getType(), exprList,v.getName(), method.getId(),kclass.getName(),method);
						if(lexer.getCurrentLine().contains("a.getAnA()")) {
							//System.out.println(lexer.getCurrentLine()+" "+msgsend1.getType());
						}
						/*
						 * para fazer as conferências semânticas, procure por
						 * método 'ident' na classe de 'firstId'
						 */
						return msgsend1;
					} else {
						// retorne o objeto da ASA que representa Id "." Id
						//if(lexer.getCurrentLine().contains("A.staticAnInt")) {
							//System.out.println(firstId+"."+ident);
							KraClass classe = symbolTable.getInGlobal(firstId);
							Variable staticVar = symbolTable.getInInstance(ident);
							if(symbolTable.getInInstance(ident) != null){
								//System.out.println(lexer.getCurrentLine()+" legal");
								return new StaticVariableExpr(classe, staticVar);
							}
							
						//}
					}
				}
			}
			break;
		case THIS:
			/*
			 * Este 'case THIS:' trata os seguintes casos: PrimaryExpr ::=
			 * "this" | "this" "." Id | "this" "." Id "(" [ Express/ionList ] ")"
			 * | "this" "." Id "." Id "(" [ ExpressionList ] ")"
			 */
			lexer.nextToken();
			if (lexer.token != Symbol.DOT) {
				// only 'this'
				// retorne um objeto da ASA que representa 'this'
				// confira se não estamos em um método estático
				return new ThisExpr(currentClass);
			} else {
				lexer.nextToken();
				if (lexer.token != Symbol.IDENT)
					signalError.show("Identifier expected");
				ident = lexer.getStringValue();
				lexer.nextToken();
				// já analisou "this" "." Id
				if (lexer.token == Symbol.LEFTPAR) {
					// "this" "." Id "(" [ ExpressionList ] ")"
					/*
					 * Confira se a classe corrente possui um método cujo nome é
					 * 'ident' e que pode tomar os parâmetros de ExpressionList
					 */
					MethodDec method = currentClass.methodSearch(ident);
					if(method == null){
						signalError.show("Method '"+ident+"' was not found in class '"+currentClass.getName()+"' or its superclasses");
					};
					exprList = this.realParameters();
					if(exprList!=null){
						Iterator<Variable> itParam = method.gerParamList().elements();
						Iterator<Expr> it = exprList.elements();
						while(it.hasNext()){
							Expr elem = it.next();
							Variable var = itParam.next();
							if(elem instanceof VariableExpr){
								//VariableExpr temp = (VariableExpr) elem;
								if(elem.getType() != var.getType()){
									if(elem.getType() instanceof KraClass && var.getType() instanceof KraClass ){
										KraClass elemClass =(KraClass) elem.getType();//System.out.println(elem.getType().getName() +" "+var.getType().getName() );
										KraClass varClass =(KraClass) var.getType();
										
										if(elemClass.getSuperClass() != null){
											if(elemClass.getSuperClass().getName().equals(varClass.getName())){
												signalError.show("Type error: the type of the real parameter is not subclass of the type of the formal parameter");
											}
										}else{
											signalError.show("Type error: the type of the real parameter is not subclass of the type of the formal parameter");
										}
									}
								}
							}
						}
					}
					MessageSendToSelf msgsend1 = new MessageSendToSelf( method.getType(), exprList, ident,currentClass.getName(),method,currentClass);
					return msgsend1;
				} else if (lexer.token == Symbol.DOT) {
					// "this" "." Id "." Id "(" [ ExpressionList ] ")"
					//System.out.println(currentClass.getName());
					lexer.nextToken();
					if (lexer.token != Symbol.IDENT)
						signalError.show("Identifier expected");
					String methodName = lexer.getStringValue();
					//Variable v = symbolTable.getInLocal(ident);
					Variable v = symbolTable.getInInstance(ident);
					if(v==null) signalError.show("Intance variable"+ident+"doesnt exist");
					
					if( !(v.getType() instanceof KraClass) ) signalError.show("Instance variable "+ident+" doesnt Class");
						KraClass k =  (KraClass) v.getType();
						MethodDec method = k.methodSearch(methodName);
						if(method == null) {
							signalError.show("Method '"+ident+"' was not found in class '"+currentClass.getName()+"' or its superclasses");
						}
						
						//if(method.getQualifier().equals(Symbol.PRIVATE) ) 
						//System.out.println(method.getId());
						if(method.getQualifier() == Symbol.PRIVATE){
							signalError.show("Method '"+method.getId()+"' was not found in the public interface of '"+currentClass.getName()+"' or its superclasses");
						}
					//}
					
					lexer.nextToken();

					exprList = this.realParameters();
					//MessageSendToThisVariable msgsend1 = new MessageSendToThisVariable( method.getType(), exprList, ident, methodName);
					MessageSendToThisVariable msgsend1 = new MessageSendToThisVariable( method.getType(), exprList, ident, methodName, currentClass,k,method);
					return msgsend1;
				} else {
					// retorne o objeto da ASA que representa "this" "." Id
					/*
					 * confira se a classe corrente realmente possui uma
					 * variável de instância 'ident'
					 */
					//System.out.println(ident);
					//preciso colocar as variaveis de instancia numa symbolTable
					Variable v = symbolTable.getInInstance(ident);
					//Variable v = symbolTable.getInLocal(ident);
					return new ThisVariableExpr(new VariableExpr(v),currentClass);
				}
			}
		//	break;
		default:
			signalError.show("Expression expected");
		}
		return null;
	}

	private LiteralInt literalInt() {

		LiteralInt e = null;

		// the number value is stored in lexer.getToken().value as an object of
		// Integer.
		// Method intValue returns that value as an value of type int.
		int value = lexer.getNumberValue();
		lexer.nextToken();
		return new LiteralInt(value);
	}

	private static boolean startExpr(Symbol token) {

		return token == Symbol.FALSE || token == Symbol.TRUE || token == Symbol.NOT || token == Symbol.THIS
				|| token == Symbol.LITERALINT || token == Symbol.SUPER || token == Symbol.LEFTPAR
				|| token == Symbol.NULL || token == Symbol.IDENT || token == Symbol.LITERALSTRING;

	}
	
	private int countwhile;
	private KraClass currentClass;
	private MethodDec currentMethod;
	private SymbolTable symbolTable;
	private Lexer lexer;
	private SignalError signalError;

}
