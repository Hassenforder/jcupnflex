package fr.uha.hassenforder.jcupnflex.writer;

import java.io.File;

import fr.uha.hassenforder.jcupnflex.model.Directive;
import fr.uha.hassenforder.jcupnflex.model.DirectiveSet;
import fr.uha.hassenforder.jcupnflex.model.Grammar;
import fr.uha.hassenforder.jcupnflex.model.RegExp;

public class FlexWriter extends AbstractWriter {

	public FlexWriter(Grammar grammar, DirectiveSet directives, File outputFile) {
		super(grammar, directives, outputFile);
	}

	private StringBuilder writeProperty(String header, String value) {
		return writeProperty (header, value, "");
	}

	private StringBuilder writeCode(String content) {
		return writeCode ("", "%{", content, "%}");
	}

	private void emitFreeCode() {
		// TODO Auto-generated method stub
		
	}

	private void emitHeader() {
		String packageName = directives.getSingleValue(Directive.PACKAGE_NAME);
		appendLine(writeProperty ("%package", packageName));
		appendLine(writeProperty ("%class", "Lexer"));
		appendLine(writeProperty ("%public", ""));
		appendLine(writeProperty ("%cupJHMH", ""));
	}

	private void emitFlexCodes() {
		String code = directives.getSingleValue(Directive.SCAN_CODE);
		appendLine(writeCode (code));
	}

	private void emitMacros() {
		// TODO Auto-generated method stub
		
	}

	private void emitStates() {
		// TODO Auto-generated method stub
		
	}

	private StringBuilder writeRegexp(RegExp regexp) {
		StringBuilder tmp = new StringBuilder();
		tmp.append("  ");
		String matcher = regexp.getRegexp();
		switch (matcher.charAt(0)) {
		case '\'' :
			tmp.append(matcher);
			break;
		case '\"' :
			tmp.append(matcher);
			break;
		case '`' :
			tmp.append(matcher.substring(1, matcher.length()-1));
			break;
		default :
			break;
		}
		tmp.append("\t\t");
		tmp.append("{ return symbol(ETerminal.");
		tmp.append(regexp.getName());
		if (regexp.getType() != null) {
			if ("String".equals(regexp.getType())) {
				tmp.append(", yytext()");
			}
			if ("Integer".equals(regexp.getType())) {
				tmp.append(", Integer.parseInt(yytext())");
			}
			if ("Long".equals(regexp.getType())) {
				tmp.append(", Long.parseLong(yytext())");
			}
			if ("Float".equals(regexp.getType())) {
				tmp.append(", Float.parseFloat(yytext())");
			}
			if ("Double".equals(regexp.getType())) {
				tmp.append(", Double.parseDouble(yytext())");
			}
		}
		tmp.append("); }");
		
		return tmp;
	}

	private void emitRules() {
		appendLine("<YYINITIAL> {");
		for (RegExp regexp : grammar.getRegexps().values()) {
			appendLine (writeRegexp(regexp));
		}
		appendLine("}");
	}

	public void generate() {
		if (! open()) return;
		emitFreeCode ();
		newLine();
		appendLine("%%");
		newLine ();
		emitHeader ();
		newLine ();
		emitFlexCodes ();
		newLine ();
		emitMacros ();
		newLine ();
		emitStates();
		newLine ();
		appendLine("%%");
		newLine ();
		emitRules();
		newLine ();
		close();
	}

}
