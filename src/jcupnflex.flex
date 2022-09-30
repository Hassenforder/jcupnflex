import fr.uha.hassenforder.jcupnflex.ErrorManager;
%%

%package fr.uha.hassenforder.jcupnflex.reader
%class Lexer
%public
%cupJHMH
%{
	// helpers to manage the whole code segment
	private StringBuffer cs;
    private int csline, cscolumn;

	@SuppressWarnings("unused")
	private String regexpCleaner (char toEscape) {
		StringBuilder tmp = new StringBuilder ();
		int current = zzStartRead+1;
		while (current < zzMarkedPos-1) {
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
    
%}

Newline = \r | \n | \r\n
Whitespace = [ \t\f] | {Newline}

/* comments */
Comment = {TraditionalComment} | {EndOfLineComment}
TraditionalComment = "/*" {CommentContent} \*+ "/"
EndOfLineComment = "//" [^\r\n]* {Newline}
CommentContent = ( [^*] | \*+[^*/] )*

ident = ([:jletter:] | "_" ) ([:jletterdigit:] | [:jletter:] | "_" )*
      | [:jletterdigit:]*  // Parse number as ident for options

%state CODESEG

%%  

<YYINITIAL> {

  {Whitespace}  { }
  {Comment}     { }
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
  "{:"          { cs = new StringBuffer(); csline=yyline+1; cscolumn=yycolumn+1; yybegin(CODESEG); }
  "package"     { return symbol(ETerminal.PACKAGE, yytext()); } 
  "import"      { return symbol(ETerminal.IMPORT, yytext()); }
  "option"      { return symbol(ETerminal.OPTION, yytext()); }
  "code"        { return symbol(ETerminal.CODE, yytext()); }
  "action"      { return symbol(ETerminal.ACTION, yytext()); }
  "parser"      { return symbol(ETerminal.PARSER, yytext()); }
  "terminal"    { return symbol(ETerminal.TERMINAL, yytext()); }
  "nonterminal" { return symbol(ETerminal.NONTERMINAL, yytext()); }
  "init"        { return symbol(ETerminal.INIT, yytext()); }
  "scan"        { return symbol(ETerminal.SCAN, yytext()); }
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
  {ident}       { return symbol(ETerminal.ID, yytext()); }
  
  `([^`]|``)*`	{ return symbol(ETerminal.REGEXP, regexpCleaner('`')); }
  '([^']|'')*'	{ return symbol(ETerminal.REGEXP, regexpCleaner('\'')); }
  \"([^\"]|\"\")*\"	{ return symbol(ETerminal.REGEXP, regexpCleaner('"')); }

}

<CODESEG> {
  ":}"          { yybegin(YYINITIAL); return symbol(ETerminal.CODE_STRING,csline, cscolumn, yyline+1, yycolumn+yylength(), yytext()); }
  .|\n          { cs.append(yytext()); }
}

// error fallback
.|\n			{ emit_warning("Unrecognized character '"+yytext()+"' -- ignored"); }
