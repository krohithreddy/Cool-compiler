parser grammar CoolParser;

options {
	tokenVocab = CoolLexer;
}

@header{
	import cool.AST;
	import java.util.List;
}

@members{
	String filename;
	public void setFilename(String f){
		filename = f;
	}

/*
	DO NOT EDIT THE FILE ABOVE THIS LINE
	Add member functions, variables below.
*/

}

/*
	Add Grammar rules and appropriate actions for building AST below.
*/
	        
			class_fun : CLASS TYPEID (INHERITS TYPEID)? '{' (feature';' )* '}' ;
			feature : OBJECTID'(' (formal (',' formal)*)? ')' ':' TYPEID '{' expr '}' 
			        | OBJECTID ':' TYPEID ( '<-' expr )?;
			formal : OBJECTID ':' TYPEID;
			expr :OBJECTID '<-' expr
				| expr('@'TYPEID)?'.'OBJECTID'(' ( expr (',' expr)*)? ')'
				| OBJECTID'(' ( expr (',' expr)*)? ')'
				| IF expr THEN expr ELSE expr FI
				| WHILE expr LOOP expr POOL
				| '{' (expr';' )+ '}'
				| LET OBJECTID ':' TYPEID ( '<-' expr )? (',' OBJECTID ':' TYPEID ( '<-' expr )?)* IN expr
				| CASE expr OF (OBJECTID ':' TYPEID '=>' expr';' )+ ESAC
				| NEW TYPEID
				| ISVOID expr
				| expr '+' expr
				| expr '-' expr
				| expr '*' expr
				| expr '/' expr
				| '~'expr
				| expr '<' expr
				| expr '<=' expr
				| expr '=' expr
				| NOT expr
				| '(' expr ')'
				| OBJECTID
				| INT_CONST
				| STR_CONST
				| BOOL_CONST;

	program returns [AST.program value]:(class_fun ';')+ {$value=null;};  //since we wont do any AST we return null
/*
program returns [AST.program value]	: 
						cl=class_list EOF 
							{
								$value = new AST.program($cl.value, $cl.value.get(0).lineNo);
							}
					;
*/
