
import fr.uha.hassenforder.jcupnflex.ErrorManager;
import fr.uha.hassenforder.jcupnflex.model.Grammar;


%%

%package reader
%class Lexer
%public 
%cupJHMH 

%{

	private Grammar grammar;
	
	public void setGrammar (Grammar grammar) {
	   this.grammar = grammar;
	}
	
	// a simple function to remove the double quotes
	@SuppressWarnings("unused")
	private String regexpCleaner (char toEscape) {
		StringBuilder tmp = new StringBuilder ();
		int current = zzStartRead;
		while (current < zzMarkedPos) {
			tmp.append(zzBuffer[current]);
			if (zzBuffer[current] == toEscape && zzBuffer[current+1] == toEscape) {
				++current;
			}
			++current;
		}
		return tmp.toString();
	}
		
	@SuppressWarnings("unused")
    private Symbol symbol(ETerminal symbol, int fromLine, int fromColumn, int toLine, int toColumn, Object lexem)		{
       AdvancedSymbolFactory.Location left = new AdvancedSymbolFactory.Location (fromLine, fromColumn);
       AdvancedSymbolFactory.Location right = new AdvancedSymbolFactory.Location (toLine, toColumn);
       return symbolFactory.newSymbol(symbol, left, right, lexem);
    }

	@SuppressWarnings("unused")
    private void emit_warning(String message){
		ErrorManager.getManager().emit_warning("Scanner at " + (yyline+1) + "(" + (yycolumn+1) + "): " + message);
    }

	@SuppressWarnings("unused")
    private void emit_error(String message){
		ErrorManager.getManager().emit_error("Scanner at " + (yyline+1) + "(" + (yycolumn+1) +  "): " + message);
    }
    
    private ETerminal getToken (String name) {
		if (grammar.getTerminal(name) != null) return ETerminal.SYMBOL_TERMINAL;
		if (grammar.getNonTerminal(name) != null) return ETerminal.SYMBOL_NONTERMINAL;
		return ETerminal.ID;
	}

    private void fallback () {
		emit_warning("Unrecognized character '"+yytext()+"' -- ignored");
	}    


%}
%{
  private class Region {
  	StringBuilder tmp;
  	int fromLine;
  	int fromColumn;

public Region() {
	super();
	this.tmp = new StringBuilder();
	this.fromLine = yyline;
	this.fromColumn = yycolumn;
}

  }

private java.util.Stack<Region> regions = new java.util.Stack<>();

private void startRegion (int state) {
	regions.push(new Region ());
	yybegin (state);
}

private void appendRegion (String content) {
 if (! regions.empty()) {
  Region region = regions.peek();
  region.tmp.append(content);
 }
}

private Region endRegion (int targetState) {
 Region region = null;
 if (! regions.empty()) {
  region = regions.pop();
 }
    yybegin (targetState);
    return region;
 }

 @SuppressWarnings("unused")
 private Symbol symbolRegion (Region region, ETerminal token) {
    if (region == null) {
  	  AdvancedSymbolFactory.Location position = new AdvancedSymbolFactory.Location (yyline+1, yycolumn+yylength());
  	  return symbolFactory.newSymbol(token, position, position, "");
    } else {
  	  String content = region.tmp.toString();
  	  AdvancedSymbolFactory.Location left = new AdvancedSymbolFactory.Location (region.fromLine+1, region.fromColumn+1);
  	  AdvancedSymbolFactory.Location right = new AdvancedSymbolFactory.Location (yyline+1, yycolumn+yylength());
  	  return symbolFactory.newSymbol(token, left, right, content);
    }
}

%}

Newline = \r | \n | \r\n
Whitespace = [ \t\f] | {Newline}
Comment = {TraditionalComment} | {EndOfLineComment}
EndOfLineComment = "//" [^\r\n]* {Newline}
ident = ([:jletter:] | "_" ) ([:jletterdigit:] | [:jletter:] | "_" )* | [:jletterdigit:]+
TraditionalComment = "/*" {CommentContent} \*+ "/"
CommentContent = ( [^*] | \*+[^*/] )*

%state CODE_STRING$State

%%

<YYINITIAL> {
  "%prec"		{ return symbol(ETerminal.__REGEXP_7__); }
  "("		{ return symbol(ETerminal.__REGEXP_11__); }
  ")"		{ return symbol(ETerminal.__REGEXP_12__); }
  "*"		{ return symbol(ETerminal.__REGEXP_8__); }
  "+"		{ return symbol(ETerminal.__REGEXP_9__); }
  ","		{ return symbol(ETerminal.__REGEXP_2__); }
  "."		{ return symbol(ETerminal.__REGEXP_14__); }
  ":"		{ return symbol(ETerminal.__REGEXP_13__); }
  "::="		{ return symbol(ETerminal.__REGEXP_4__); }
  ";"		{ return symbol(ETerminal.__REGEXP_1__); }
  "<"		{ return symbol(ETerminal.__REGEXP_17__); }
  "="		{ return symbol(ETerminal.__REGEXP_3__); }
  ">"		{ return symbol(ETerminal.__REGEXP_18__); }
  "?"		{ return symbol(ETerminal.__REGEXP_10__); }
  "["		{ return symbol(ETerminal.__REGEXP_15__); }
  "]"		{ return symbol(ETerminal.__REGEXP_16__); }
  "|"		{ return symbol(ETerminal.__REGEXP_5__); }
  "~"		{ return symbol(ETerminal.__REGEXP_6__); }
  "code"		{ return symbol(ETerminal.CODE, yytext()); }
  "init"		{ return symbol(ETerminal.INIT, yytext()); }
  "with"		{ return symbol(ETerminal.WITH, yytext()); }
  "left"		{ return symbol(ETerminal.LEFT, yytext()); }
  "super"		{ return symbol(ETerminal.SUPER, yytext()); }
  "right"		{ return symbol(ETerminal.RIGHT, yytext()); }
  "after"		{ return symbol(ETerminal.AFTER, yytext()); }
  "import"		{ return symbol(ETerminal.IMPORT, yytext()); }
  "option"		{ return symbol(ETerminal.OPTION, yytext()); }
  "action"		{ return symbol(ETerminal.ACTION, yytext()); }
  "parser"		{ return symbol(ETerminal.PARSER, yytext()); }
  "reduce"		{ return symbol(ETerminal.REDUCE, yytext()); }
  "package"		{ return symbol(ETerminal.PACKAGE, yytext()); }
  "extends"		{ return symbol(ETerminal.EXTENDS, yytext()); }
  "scanner"		{ return symbol(ETerminal.SCANNER, yytext()); }
  "nonassoc"		{ return symbol(ETerminal.NONASSOC, yytext()); }
  {ident}		{  return symbol(getToken(yytext()), yytext());  }
  {Comment}		{   }
  {Whitespace}		{   }
  `([^`]|``)*`		{  return symbol(ETerminal.REGEXP, regexpCleaner('`'));  }
  '([^']|'')*'		{  return symbol(ETerminal.REGEXP, regexpCleaner('\''));  }
  \"([^\"]|\"\")*\"		{  return symbol(ETerminal.REGEXP, regexpCleaner('"'));  }
  "{:"		{ startRegion (CODE_STRING$State); }
}

<CODE_STRING$State> {
  ":}"		{ Region r = endRegion (YYINITIAL); return symbolRegion (r, ETerminal.CODE_STRING); }
  [^]		{ appendRegion (yytext()); }
}


[^]			 { fallback(); }

