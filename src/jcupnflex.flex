import fr.uha.hassenforder.jcupnflex.ErrorManager;
import fr.uha.hassenforder.jcupnflex.model.Grammar;
%%

%package fr.uha.hassenforder.jcupnflex.reader
%class Lexer
%public
%cupJHMH
%{
	private Grammar grammar;
	
	public void setGrammar (Grammar grammar) {
	   this.grammar = grammar;
	}

	// helpers to manage the whole code segment
	private StringBuilder cs;
    private int csline, cscolumn;

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
		if (grammar.getState(name) != null) return ETerminal.SYMBOL_STATE;
		return ETerminal.ID;
	}

%}

Newline = \r | \n | \r\n
Whitespace = [ \t\f] | {Newline}

/* comments */
Comment = {TraditionalComment} | {EndOfLineComment}
TraditionalComment = "/*" {CommentContent} \*+ "/"
EndOfLineComment = "//" [^\r\n]* {Newline}
CommentContent = ( [^*] | \*+[^*/] )*

ident = ([:jletter:] | "_" ) ([:jletterdigit:] | [:jletter:] | "_" )*
      | [:jletterdigit:]+  // Parse number as ident for options

%state CODESEG

%%  

<YYINITIAL> {

  {Whitespace}  { }
  {Comment}     { }
  "~"           { return symbol(ETerminal.TILDA); }
  "?"           { return symbol(ETerminal.QUESTION); }
  ";"           { return symbol(ETerminal.SEMICOLON); }
  ","           { return symbol(ETerminal.COMMA); }
  "*"           { return symbol(ETerminal.STAR); }
  "+"           { return symbol(ETerminal.PLUS); }
  "."           { return symbol(ETerminal.DOT); }
  "|"           { return symbol(ETerminal.BAR); }
  "["           { return symbol(ETerminal.LBRACK); }
  "]"           { return symbol(ETerminal.RBRACK); }
  "("           { return symbol(ETerminal.LPARENT); }
  ")"           { return symbol(ETerminal.RPARENT); }
  ":"           { return symbol(ETerminal.COLON); }
  "="           { return symbol(ETerminal.EQUALS); }
  ">"           { return symbol(ETerminal.GT); }
  "<"           { return symbol(ETerminal.LT); }
  "::="         { return symbol(ETerminal.COLON_COLON_EQUALS); }
  "%prec"       { return symbol(ETerminal.PERCENT_PREC); }
  "{:"          { cs = new StringBuilder(); csline=yyline; cscolumn=yycolumn; yybegin(CODESEG); }
  "package"     { return symbol(ETerminal.PACKAGE, yytext()); } 
  "import"      { return symbol(ETerminal.IMPORT, yytext()); }
  "option"      { return symbol(ETerminal.OPTION, yytext()); }
  "code"        { return symbol(ETerminal.CODE, yytext()); }
  "action"      { return symbol(ETerminal.ACTION, yytext()); }
  "parser"      { return symbol(ETerminal.PARSER, yytext()); }
  "terminal"    { return symbol(ETerminal.TERMINAL, yytext()); }
  "nonterminal" { return symbol(ETerminal.NONTERMINAL, yytext()); }
  "state"	    { return symbol(ETerminal.STATE, yytext()); }
  "init"        { return symbol(ETerminal.INIT, yytext()); }
  "scanner"     { return symbol(ETerminal.SCANNER, yytext()); }
  "with"        { return symbol(ETerminal.WITH, yytext()); }
  "start"       { return symbol(ETerminal.START, yytext()); }
  "expect"      { return symbol(ETerminal.EXPECT, yytext()); }
  "precedence"  { return symbol(ETerminal.PRECEDENCE, yytext()); }
  "left"        { return symbol(ETerminal.LEFT, yytext()); }
  "right"       { return symbol(ETerminal.RIGHT, yytext()); }
  "nonassoc"    { return symbol(ETerminal.NONASSOC, yytext()); }
  "extends"     { return symbol(ETerminal.EXTENDS, yytext()); }
  "super"       { return symbol(ETerminal.SUPER, yytext()); }
  "after"       { return symbol(ETerminal.AFTER, yytext()); }
  "reduce"      { return symbol(ETerminal.REDUCE, yytext()); }
  {ident}       { return symbol(getToken(yytext()), yytext()); }
  
  `([^`]|``)*`	{ return symbol(ETerminal.REGEXP, regexpCleaner('`')); }
  '([^']|'')*'	{ return symbol(ETerminal.REGEXP, regexpCleaner('\'')); }
  \"([^\"]|\"\")*\"	{ return symbol(ETerminal.REGEXP, regexpCleaner('"')); }

}

<CODESEG> {
  ":}"          { yybegin(YYINITIAL); return symbol(ETerminal.CODE_STRING, csline+1, cscolumn+1, yyline+1, yycolumn+1, cs.toString()); }
  [^]			{ cs.append(yytext()); }
}

// error fallback
[^]				{ emit_warning("Unrecognized character '"+yytext()+"' -- ignored"); }
