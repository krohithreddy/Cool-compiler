lexer grammar CoolLexer;

tokens{
	ERROR,
	TYPEID,
	OBJECTID,
	BOOL_CONST,
	INT_CONST,
	STR_CONST,
	LPAREN,
	RPAREN,
	COLON,
	ATSYM,
	SEMICOLON,
	COMMA,
	PLUS,
	MINUS,
	STAR,
	SLASH,
	TILDE,
	LT,
	EQUALS,
	LBRACE,
	RBRACE,
	DOT,
	DARROW,
	LE,
	ASSIGN,
	CLASS,
	ELSE,
	FI,
	IF,
	IN,
	INHERITS,
	LET,
	LOOP,
	POOL,
	THEN,
	WHILE,
	CASE,
	ESAC,
	OF,
	NEW,
	ISVOID,
	NOT
}

/*
  DO NOT EDIT CODE ABOVE THIS LINE
*/

@members{

        static int max =1024;
/*
		YOU CAN ADD YOUR MEMBER VARIABLES AND METHODS HERE
	*/

	/**
	* Function to report errors.
	* Use this function whenever your lexer encounters any erroneous input
	* DO NOT EDIT THIS FUNCTION
	*/
	public void reportError(String errorString){
		setText(errorString);
		setType(ERROR);
	}

	public void processString() {
		Token t = _factory.create(_tokenFactorySourcePair, _type, _text, _channel, _tokenStartCharIndex, getCharIndex()-1, _tokenStartLine, _tokenStartCharPositionInLine);
		String text = t.getText();
                      if(text.length() > max) {
			reportError("Length of strings is too LONG");
			return;}
              StringBuilder buf = new StringBuilder(0);
		for(int i=1;i<text.length()-1;++i){
                       if(text.charAt(i) == '\\') {
				if(text.charAt(i+1) == 'n')
					buf.append('\n');
				else if(text.charAt(i+1) == 'f')
					buf.append('\f');
				else if(text.charAt(i+1) == 't')
					buf.append('\t');
				else if(text.charAt(i+1) == 'b')
					buf.append('\b');
				else if(text.charAt(i+1) == '\"')
					buf.append('\"');
				else if(text.charAt(i+1) == '\\')
					buf.append('\\');
				else
					buf.append(text.charAt(i+1));
				i++;
			}
			else {
				buf.append(text.charAt(i));
			}
                    }
               String ntext = buf.toString();
		//write your code to test strings here
                   
               setText(ntext);
		return;
	}
}

/*
	WRITE ALL LEXER RULES BELOW
*/
WS          : [\t\v\f\n\r ]+ ->skip ;  //remove all blanks
NESTED_COMMENT : '(*'(NESTED_COMMENT|.)*?'*)' ->skip;  //recursive function to remove nested comments

SINGLE_COMMENT : '--'~[\n\r]*'\n' ->skip;  //remove single line comment
ERROR     :      '*)' {reportError("No matching '(*'");} |
                 '(*' ~['*)']* .(EOF) {reportError("EOF IN COMMENT");} |
                  '"' (~[\n"])* .(EOF) {reportError("EOF IN STRING CONSTANT");} |
                  '"' ~["\nEOF]* '\n'  {reportError("unterminated string constant");} |
                  '"' ( ~[\u0000]* ('\\u0000') )+ ~["\nEOF]* ["\nEOF] { reportError("the given stirng contains null character"); } ;
STR_CONST   : '"'('\\"' | '\\\\'|.)*?'"'{processString();};   //calls the processString function
SEMICOLON   : ';';
DARROW      : '=>';
NOT 	    : [nN][oO][tT] ;
ISVOID      : [iI][sS][vV][oO][iI][dD];
NEW         : [nN][eE][wW];
OF          : [oO][fF];
ESAC        : [eE][sS][aA][cC];
CASE        : [cC][aA][sS][eE] ;
WHILE       : [wW][hH][iI][lL][eE];	
THEN        : [tT][hH][eE][nN];
POOL        : [pP][oO][oO][lL];
LOOP        : [lL][oO][oO][pP];
LET         : [lL][eE][tT] ;
INHERITS    : [iI][nN][hH][eE][rR][iI][tT][sS];
IN          : [iI][nN] ;
IF          : [iI][fF];
FI          : [fF][iI];  
ELSE        : [eE][lL][sS][eE] ;
CLASS       : [cC][lL][aA][sS][sS] ;
ASSIGN      : '<-' ;
LE          : '<=';
DOT         : '.' ;
RBRACE      : '}';
LBRACE      : '{' ;
EQUALS      : '=' ;
LT          : '<' ;
TILDE       : '~' ;
SLASH       : '/' ;
STAR        : '*' ;
MINUS       : '-' ;
PLUS        : '+' ;
COMMA       : ',' ;
ATSYM       : '@';
COLON       : ':';
RPAREN      : ')';
LPAREN      : '(';
TYPEID      : [A-Z][a-zA-Z0-9_]*;
OBJECTID    : [a-z][a-zA-Z0-9_]*;
INT_CONST   : [0-9]+;
BOOL_CONST  : [t][rR][uU][eE] | [f][aA][lL][sS][eE] ;
 
